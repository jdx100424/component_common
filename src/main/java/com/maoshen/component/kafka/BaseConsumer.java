package com.maoshen.component.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.dto.MessageDto;
import com.maoshen.component.kafka.dto.MessageVo;
import com.maoshen.component.kafka.util.KafkaUtil;
import com.maoshen.component.other.ResourceUtils;
import com.maoshen.component.rest.UserRestContext;

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
	
	private boolean isGray;	
	
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
		String kafkaServer = ResourceUtils.get("kafka.server","127.0.0.1:9092,127.0.0.1:9093");
		String kafkaGray = ResourceUtils.get("kafka.gray","false");
		isGray = KafkaUtil.iskafkaGray(kafkaGray);
		props.put("bootstrap.servers", kafkaServer);
		//props.put("bootstrap.servers", kafkaIp+":"+kafkaPort);
		LOGGER.info("class:" + this.getClass().getName() + ",kafkaIp:" + kafkaIp + ",kafkaPort:" + kafkaPort + ",kafkaServer:"+kafkaServer);

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
		
		//灰度环境判断
		if(isGray){
			consumer.subscribe(Arrays.asList(KafkaUtil.GRAY_KAFKA + topicName));
		}else{
			consumer.subscribe(Arrays.asList(topicName));
		}

		// 使用线程监控KAFKA接收信息
		new Thread() {
			public void run() {
				while(true){
					ConsumerRecords<String, String> records = consumer.poll(100);
					for (ConsumerRecord<String, String> record : records) {
						if(LOGGER.isDebugEnabled()){
							LOGGER.debug("receive messag,topic:{},partition:{},offset:{}",record.topic(),record.partition(),record.offset());
						}
						try {		
							String receiveStr = record.value();
							MessageDto dto = JSONObject.parseObject(receiveStr,MessageDto.class);
							UserRestContext userRestContext = UserRestContext.get();
							userRestContext.setAccessToken(dto.getUserRestContext().getAccessToken());
							userRestContext.setRequestId(dto.getUserRestContext().getRequestId());
							onMessage(dto);
						} catch (Exception e) {
							LOGGER.error(this.getClass().getName() + "  onMessageKafka error,topicName is:" + topicName,e);
						} finally{
							UserRestContext.clear();
						}
					}
				}
			}
		}.start();
		LOGGER.info(this.getClass().getName() + " kafka is start,topicName is:" + topicName);
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
}
