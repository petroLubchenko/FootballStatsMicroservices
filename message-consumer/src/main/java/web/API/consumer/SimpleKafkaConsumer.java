package web.API.consumer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;


public class SimpleKafkaConsumer {

    ConsumedMessageRep rep ;

    private KafkaConsumer<String, String> kafkaConsumer;

    public SimpleKafkaConsumer(String topicName, Properties consumerProps, ConsumedMessageRep rep){
        kafkaConsumer = new KafkaConsumer<>(consumerProps);
        kafkaConsumer.subscribe(Arrays.asList(topicName));
        this.rep = rep;
    }

    public void runSingleWorker(){
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

            if (records != null)
                for (ConsumerRecord<String,String> record : records){
                    if (record != null) {
                        String message = record.value();
                        System.out.println(message);

                        Message msg;

                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            msg = mapper.readValue(message, Message.class);
                            ConsumedMessage consumedMessage = new ConsumedMessage(msg.getDescription(), msg.getHttpMethod(), msg.getHttpStatus(), msg.getObject() == null ? null : msg.getObject(), msg.getError() == null ? null : msg.getError());
                            rep.save(consumedMessage);
                        } catch (IOException e) {
                            rep.save(new ConsumedMessage("", null, 0, message, "Can not deserialize object"));
                        }
                    }

                }

        }
    }
}
