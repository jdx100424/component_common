package com.maoshen.component.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.constant.KafkaConstant;
import com.maoshen.component.kafka.dto.MessageDto;
import com.maoshen.component.kafka.dto.MessageVo;
import com.maoshen.component.other.LsDigestUtils;
import com.maoshen.component.other.ResourceUtils;

/**
 * KAFKA接收端的代码
 * 
 * @author jdx
 *
 */
public abstract class BaseConsumer implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseConsumer.class);
	// 组ID
	private String groupId;
	// KAKFA消息接收名称
	private String topicName;

	// KAFKA REQUSETID默认保存时间（在这个时间段里相同请求不会接收）
	private static final long REQUEST_EXPIRE_TIME = 60 * 60 * 24 * 7;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate jedisTemplate;
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate jedisTemplateLong;
	
	@Autowired
	@Qualifier("baseProducer")
	private BaseProducer baseProducer;

	public BaseConsumer() {
		super();
		this.groupId = getGroupIdANdTopicName().getGroupId();
		this.topicName = getGroupIdANdTopicName().getTopicName();
	}

	@SuppressWarnings("resource")
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isEmpty(groupId) || StringUtils.isEmpty(topicName)) {
			throw new Exception("groupId and topicName is not allow empty,class is " + this.getClass().getName());
		}
		Properties props = new Properties();
		String kafkaIp = ResourceUtils.get("kafka.ip", "localhost");
		String kafkaPort = ResourceUtils.get("kafka.port", "9092");
		props.put("bootstrap.servers", kafkaIp + ":" + kafkaPort);
		LOGGER.info("class:" + this.getClass().getName() + ",kafkaIp:" + kafkaIp + ",kafkaPort:" + kafkaPort);

		// 消费者的组id
		props.put("group.id", groupId);
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		//props.put("auto.offset.reset", "earliest"); 
		// 从poll(拉)的回话处理时长
		props.put("session.timeout.ms", "30000");
		// poll的数量限制
		//props.put("max.poll.records", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		// 订阅主题列表topic
		consumer.subscribe(Arrays.asList(topicName));

		// 使用线程监控KAFKA接收信息
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				while(true){
					ConsumerRecords<String, String> records = consumer.poll(100);
					for (ConsumerRecord<String, String> record : records) {
						//LOGGER.info("receive messag,info is:" + JSONObject.toJSONString(record));
						String receiveStr = record.value();

						Object receiveObject = JSONObject.parse(receiveStr);
						MessageDto dto = new MessageDto();
						dto.setMessageInfo(receiveObject);
						dto.setRequestId(record.key());
						
						String md5Id = LsDigestUtils.md5(dto);
						
						//定义运行KEY和失败的KEY
						String requestIdKafkaRun = md5Id + "_" + topicName + "_kafka_run";
						String requestIdKafkaFail = md5Id + "_" + topicName + "_kafka_fail";
						LOGGER.error("MesageDto:{},requestIdKafkaRun:{},requestIdKafkaFail:{}",JSONObject.toJSONString(dto),requestIdKafkaRun,requestIdKafkaFail );
						try {					
							//运行前先检测是否有重发标记限制
							boolean isDoing = true;
							if (StringUtils.isNotBlank(dto.getRequestId()) && isResend()){
								if(jedisTemplate.opsForValue().setIfAbsent(requestIdKafkaRun, "true")) {
									jedisTemplate.expire(getName(), REQUEST_EXPIRE_TIME, TimeUnit.SECONDS);	
								}else{
									isDoing = false;
								}
							}
							
							if(isDoing){
								onMessage(dto);
							}else{
								LOGGER.warn(requestIdKafkaRun + " has been run,do not doing again");
							}
						} catch (Exception e) {
							LOGGER.error(this.getClass().getName() + "  onMessageKafka error,topicName is:" + topicName + ",requestId:" + dto.getRequestId(), e);
							//重发校验
							if (StringUtils.isNotBlank(dto.getRequestId()) && isResend()){
								try{
									resend(requestIdKafkaFail, requestIdKafkaRun, receiveObject, dto.getRequestId());
								}catch(Exception e1){
									LOGGER.error(this.getClass().getName() + " onMessageKafka resend error,topicName is:" + topicName + ",requestId:" + dto.getRequestId(), e1);
								}
							}
						}
					}
				}
			}
		}.start();
		LOGGER.info(this.getClass().getName() + " kafka is start,topicName is:" + topicName);
	}

	/**
	 * 重发
	 * @param requestIdKafkaFail
	 * @param requestIdKafkaRun
	 * @param allowKafkaResendAnnotation
	 * @param receiveObject
	 */
	@SuppressWarnings("unchecked")
	private void resend(String requestIdKafkaFail,String requestIdKafkaRun,Object receiveObject,String requestId){
		boolean isOver = false;		
		
		jedisTemplateLong.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
		Long times = (Long) jedisTemplateLong.opsForValue().get(requestIdKafkaFail);
		if(times != null && Long.parseLong(times.toString()) >= KafkaConstant.KAFKA_RESEND_DEFAULT){
			isOver = true;
		}
		jedisTemplateLong.opsForValue().increment(requestIdKafkaFail, 1L);
		jedisTemplateLong.expire(requestIdKafkaFail, REQUEST_EXPIRE_TIME, TimeUnit.SECONDS);
		
		//重发
		if(isOver==false){
			//重发前需要删除运行REDIS状态
			jedisTemplate.delete(requestIdKafkaRun);
			baseProducer.send(topicName, receiveObject,requestId);
		}else{
			LOGGER.warn(requestIdKafkaRun + " resend is over " + KafkaConstant.KAFKA_RESEND_DEFAULT);
		}
	}
	
	/**
	 * KAFKA接收信息后实际的操作逻辑
	 * 
	 * @param dto
	 */
	public abstract void onMessage(MessageDto dto);

	/**
	 * 组ID,KAFKA名称
	 */
	public abstract MessageVo getGroupIdANdTopicName();
	
	/**
	 * 是否可以重发
	 * @return
	 */
	public abstract boolean isResend();
}
