package web.API.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Time;
import java.util.Date;
import java.util.Properties;

@SpringBootApplication
public class ConsumerApplication {
    @Value("${kafka.topic.ftc}")
    private String ftcTopicName;

    @Value("${kafka.topic.footballers}")
    private String footballerTopicName;

    @Value("${kafka.topic.team}")
    private String teamTopicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${zookeeper.groupId}")
    private String zookeeperGroupId;

    @Value("${zookeeper.groupId}")
    String zookeeperHost;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(ConsumedMessageRep rep){
        return args -> {
            Properties consumerProperties = new Properties();
            consumerProperties.put("bootstrap.servers", kafkaBootstrapServers);
            consumerProperties.put("group.id", zookeeperGroupId);
            consumerProperties.put("zookeeper.session.timeout.ms", "6000");
            consumerProperties.put("zookeeper.sync.time.ms","2000");
            consumerProperties.put("auto.commit.enable", "false");
            consumerProperties.put("auto.commit.interval.ms", "1000");
            consumerProperties.put("consumer.timeout.ms", "-1");
            consumerProperties.put("max.poll.records", "1");
            consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            Thread footballerKafkaConsThread = new Thread(() -> {
                System.out.println(new Date() + "\tINFO\tStarting Kafka Consumer Thread");
                SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer(footballerTopicName, consumerProperties, rep);
                simpleKafkaConsumer.runSingleWorker();
            });
            Thread teamKafkaConsThread = new Thread(() -> {
                System.out.println(new Date() + "\tINFO\tStarting Kafka Consumer Thread");
                SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer(teamTopicName, consumerProperties, rep);
                simpleKafkaConsumer.runSingleWorker();
            });

            footballerKafkaConsThread.start();
            teamKafkaConsThread.start();

            //SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer(teamTopicName, consumerProperties);
            //simpleKafkaConsumer.runSingleWorker();

        } ;
    }
}
