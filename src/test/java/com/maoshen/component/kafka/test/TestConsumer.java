package com.maoshen.component.kafka.test;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.alibaba.fastjson.JSONObject;
import com.maoshen.component.kafka.dto.MessageVo;

public class TestConsumer {
	public static void main(String[] args) {
		Properties props = new Properties();

		props.put("bootstrap.servers", "127.0.0.1:9092");
		System.out.println("this is the group part test 1");
		// 消费者的组id
		props.put("group.id", MessageVo.ECHO_MESSAGE.getGroupId());// 这里是GroupA或者GroupB

		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");

		// 从poll(拉)的回话处理时长
		props.put("session.timeout.ms", "30000");
		// poll的数量限制
		// props.put("max.poll.records", "100");

		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		// 订阅主题列表topic
		consumer.subscribe(Arrays.asList(MessageVo.ECHO_MESSAGE.getTopicName()));

		
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(10000);
			for (ConsumerRecord<String, String> record : records) {
				
				// 正常这里应该使用线程池处理，不应该在这里处理
				System.out.println(
						"offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
				//TestVO vo = JSONObject.parseObject(record.value(), TestVO.class);
				//System.out.println("offset = " + record.offset() + ", key = " + record.key() + ", value = "
				//		+ record.value() + "|" + vo.getId() + "," + vo.getName());
			}
		}
	}
}
