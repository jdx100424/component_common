package com.maoshen.component.kafka.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TestKafkaConsumer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "127.0.0.1:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("group.id", "0");
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.offset.reset", "earliest");

		Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("topicEcho"));

		for (int i = 0; i < 2; i++) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			System.out.println(records.count());
			for (ConsumerRecord<String, String> record : records) {
				System.out.println(record);
				// consumer.seekToBeginning(new TopicPartition(record.topic(),
				// record.partition()));
			}
		}
	}
}
