package API.footballstats.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static void sendKafkaMessage(String payload, KafkaProducer<String, String> producer, String topic){
        producer.send(new ProducerRecord<>(topic, payload));
    }


    @Bean
    public MetricsProperties.Web.Client client(){
        return new MetricsProperties.Web.Client();
    }
}

@RestController
class ServiceInstanceRestController {
    @Value("${kafka.topic.ftc}")
    private String ftcTopicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${zookeeper.groupId}")
    private String zookeeperGroupId;

    @Value("${zookeeper.groupId}")
    String zookeeperHost;

    KafkaProducer<String, String> producer;


    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment env;

    @Autowired
    public ServiceInstanceRestController(@Value("${kafka.bootstrap.servers}") String KafkaBootstrapServers){
        Properties producerProperties = new Properties();

        producerProperties.put("bootstrap.servers", KafkaBootstrapServers);
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(producerProperties);

    }

    @RequestMapping("/{applicationName}")
    public List<ServiceInstance> serviceInstanceList(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);

    }

    @GetMapping("/")
    public String hello(@Value("${name}") String name){
        sendKafkaMessage("calling 'Hello' method from client", producer, ftcTopicName);
        return "Hello " + name + "!";
    }

    @GetMapping("/inf")
    public String getProps(){
        sendKafkaMessage("calling 'Information' method from client", producer, ftcTopicName);

        Map<String, Object> props = new HashMap<>();
        CompositePropertySource bootstrapProps = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");

        for (String propertName : bootstrapProps.getPropertyNames())
            props.put(propertName, bootstrapProps.getProperty(propertName));

        CompositePropertySource appProps = (CompositePropertySource) ((AbstractEnvironment) env).getPropertySources().get("applicationProperties");

        if (appProps != null)
            for (String propertName : appProps.getPropertyNames())
                props.put(propertName, appProps.getProperty(propertName));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            sendKafkaMessage(mapper.writeValueAsString(props), producer, ftcTopicName);
            return mapper.writeValueAsString(props);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "Something gone wrong!!!";

    }
    private static void sendKafkaMessage(String payload, KafkaProducer<String, String> producer, String topic){
        producer.send(new ProducerRecord<>(topic, payload));
    }

}

@Configuration
class Config{

    //@LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}

