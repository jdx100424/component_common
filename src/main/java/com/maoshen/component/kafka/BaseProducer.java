package com.maoshen.component.kafka;

import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.dto.MessageVo;
import com.maoshen.component.other.ResourceUtils;

/**
 * KAFKA发送方
 * @author jdx
 *
 */
public class BaseProducer implements InitializingBean {
	private static final Logger LOGGER = Logger.getLogger(BaseProducer.class);

	Properties props;

	public void afterPropertiesSet() throws Exception {
		props = new Properties();
		String kafkaIp = ResourceUtils.get("kafka.ip", "localhost");
		String kafkaPort = ResourceUtils.get("kafka.port", "9092");
		props.put("bootstrap.servers", kafkaIp + ":" + kafkaPort);

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
		// The key.serializer and value.serializer instruct how to turn the key
		// and value objects the user provides with their ProducerRecord into
		// bytes.
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

	public void send(String topicName, Object obj) {
		Producer<String, String> producer = null;
		try {
			// 创建kafka的生产者类
			// 生产者的主要方法
			// close();//Close this producer.
			// close(long timeout, TimeUnit timeUnit); //This method waits up to
			// timeout for the producer to complete the sending of all incomplete
			// requests.
			// flush() ;所有缓存记录被立刻发送
			producer = new KafkaProducer<String, String>(props);
			String info = JSONObject.toJSONString(obj);
			producer.send(new ProducerRecord<String, String>(topicName, UUID.randomUUID().toString(), info));
			producer.flush();
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + " send error,topicName is:" + topicName,e);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}
	
	public void send(MessageVo messageVo, Object obj) {
		Producer<String, String> producer = null;
		try {
			// 创建kafka的生产者类
			// 生产者的主要方法
			// close();//Close this producer.
			// close(long timeout, TimeUnit timeUnit); //This method waits up to
			// timeout for the producer to complete the sending of all incomplete
			// requests.
			// flush() ;所有缓存记录被立刻发送
			producer = new KafkaProducer<String, String>(props);
			String info = JSONObject.toJSONString(obj);
			producer.send(new ProducerRecord<String, String>(messageVo.getTopicName(), UUID.randomUUID().toString(), info));
			producer.flush();
		} catch (Exception e) {
			LOGGER.error(this.getClass().getName() + " send error,topicName is:" + messageVo.getTopicName(),e);
		} finally {
			if (producer != null) {
				producer.close();
			}
		}
	}
}
