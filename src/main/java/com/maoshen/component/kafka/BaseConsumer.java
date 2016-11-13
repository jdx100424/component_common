package com.maoshen.component.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.dto.MessageDto;
import com.maoshen.component.other.ResourceUtils;

/**
 * KAFKA接收端的代码
 * 
 * @author jdx
 *
 */
public abstract class BaseConsumer implements InitializingBean {
	private static final Logger LOGGER = Logger.getLogger(BaseConsumer.class);
	// 组ID
	private String groupId;
	// KAKFA消息接收名称
	private String topicName;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
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
		// 从poll(拉)的回话处理时长
		props.put("session.timeout.ms", "30000");
		// poll的数量限制
		props.put("max.poll.records", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		// 订阅主题列表topic
		consumer.subscribe(Arrays.asList(topicName));

		// 使用线程监控KAFKA接收信息
		new Thread() {
			public void run() {
				ConsumerRecords<String, String> records = consumer.poll(10000);
				for (ConsumerRecord<String, String> record : records) {
					LOGGER.info("receive messag,info is:" + JSONObject.toJSONString(record));
					try {
						String receiveStr = record.value();
						Object receiveObject = JSONObject.parse(receiveStr);

						MessageDto dto = new MessageDto();
						dto.setMessageInfo(receiveObject);
						dto.setRequestId(record.key());
						onMessage(dto);
					} catch (Exception e) {
						LOGGER.error(this.getClass().getName() + "  onMessageKafka error,topicName is:" + topicName, e);
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
}
