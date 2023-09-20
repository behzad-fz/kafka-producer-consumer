package com.example.Kafka.stream;

import com.example.Kafka.stream.service.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(topics = "test-topic", partitions = 1)
class KafkaStreamApplicationTests {
	@BeforeAll
	public static void setup() {
		System.setProperty(EmbeddedKafkaBroker.BROKER_LIST_PROPERTY, "spring.kafka.bootstrap-servers");
	}

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	private Producer kafkaProducer;

	@Test
	public void testSendMessage() {
		String topic = "test-topic";
		String message = "Test message";

		kafkaProducer.send(message, topic);

		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer())
				.createConsumer();

		embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);

		ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
		ConsumerRecord<String, String> record = records.iterator().next();

		assertThat(records).hasSize(1);
		assertThat(record.value()).isEqualTo(message);
	}

}
