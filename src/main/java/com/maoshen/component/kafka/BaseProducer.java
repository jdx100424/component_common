package com.maoshen.component.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.dto.MessageDto;
import com.maoshen.component.kafka.dto.MessageVo;
import com.maoshen.component.other.ResourceUtils;
import com.maoshen.component.rest.UserRestContext;

/**
 * KAFKA发送方
 * 
 * @author jdx
 *
 */
public class BaseProducer implements InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseProducer.class);

	Properties props;

	public void afterPropertiesSet() throws Exception {
		props = new Properties();
		String kafkaIp = ResourceUtils.get("kafka.ip", "localhost");
		String kafkaPort = ResourceUtils.get("kafka.port", "9092");
		String kafkaServer = ResourceUtils.get("kafka.server","127.0.0.1:9092,127.0.0.1:9093");
		//props.put("bootstrap.servers", kafkaIp+":"+kafkaPort);
		props.put("bootstrap.servers", kafkaServer);
		LOGGER.info("class:" + this.getClass().getName() + ",kafkaIp:" + kafkaIp + ",kafkaPort:" + kafkaPort + ",kafkaServer:"+kafkaServer);


		// The "all" setting we have specified will result in blocking on the
		// full commit of the record, the slowest but most durable setting.
		// “所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
		props.put("acks", "all");
		// 如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
		props.put("retries", 0);
		// The producer maintains buffers of unsent records for each partition.
		props.put("batch.size", 16384);
		// 默认立即发送，这里这是延时毫秒数
		props.put("linger.ms", 1);
		// 生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
		props.put("buffer.memory", 33554432);
		props.put("metadata.max.age.ms", 60000);
		// The key.serializer and value.serializer instruct how to turn the key
		// and value objects the user provides with their ProducerRecord into
		// bytes.
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

	public void send(String topicName, MessageDto dto) {
		Producer<String, String> producer = null;
		if (dto == null) {
			return;
		}

		try {
			UserRestContext userRestContext = UserRestContext.get();
			dto.setUserRestContext(userRestContext);
			producer = new KafkaProducer<String, String>(props);
			String info = JSONObject.toJSONString(dto);
			producer.send(new ProducerRecord<String, String>(topicName, null, info),
					new Callback() {
						public void onCompletion(RecordMetadata metadata, Exception ex) {
							if (ex != null) {
								LOGGER.error("kafka_send_fail,requestId=" + userRestContext.getRequestId() + ",topic="
										+ topicName, ex);
							} else {
								LOGGER.info("kafka_send_success,requestId=" + userRestContext.getRequestId()
										+ ", topic=" + topicName + ", partition=" + metadata.partition() + ", offset="
										+ metadata.offset());
							}
						}

					});
			producer.flush();
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + " send error,topicName is:" + topicName, e);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}

	public void send(MessageVo messageVo, MessageDto dto) {
		if (messageVo != null) {
			send(messageVo.getTopicName(), dto);
		}
	}
}
