package API.footballstats.client;

import API.footballstats.client.Exceptions.EntityNotFoundException;
import API.footballstats.client.Exceptions.InadmissiblefieldsException;
import API.footballstats.client.Exceptions.InternalServerErrorException;
import API.footballstats.client.Exceptions.ServiceIsUnavailable;
import API.footballstats.client.Models.Message;
import API.footballstats.client.Models.Team;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@RequestMapping("/teamsfuck")
@RestController
public class TeamStatisticController {
    @Value("${kafka.topic.team}")
    private String topicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${zookeeper.groupId}")
    private String zookeeperGroupId;

    @Value("${zookeeper.groupId}")
    String zookeeperHost;
    KafkaProducer<String, String> producer;

    @Autowired
    RestTemplate restTemplate;
    public TeamStatisticController(@Value("${kafka.bootstrap.servers}") String KafkaBootstrapServers){
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

    private String url = "http://tfcservice:8100/teams/";

    @GetMapping("/all")
    public List getAll() throws IOException {
        try {
            List<Object> object = restTemplate.getForObject(url, List.class);
            Message message = new Message("Getting all teams from database", HttpMethod.GET, HttpStatus.OK, (Team) null, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            return object;
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }
        return null;
    }

    @GetMapping("/{id}")
    public Object getOne(@PathVariable long id){
        String getoneUrl = url + id;
        try {
            Object object = restTemplate.getForObject(getoneUrl, Team.class);
            Message message;

            if (object instanceof Team)
                message = new Message("Getting a team with id = " + id, HttpMethod.GET, HttpStatus.OK, (Team) object, "");
            else
                message = new Message("Getting an object with id = " + id, HttpMethod.GET, HttpStatus.OK, (Team) null, "Received object is not a Footballer");

            Application.sendKafkaMessage(message.toString(), producer, topicName);
            return object;
        }
        catch (HttpClientErrorException ex){
            HttpClientErrorExceptionHandler(ex, id, HttpMethod.GET);
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id){
        String delurl = url + id;

        try{
            restTemplate.delete(delurl);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (HttpClientErrorException ex) {
            HttpClientErrorExceptionHandler(ex, id, HttpMethod.DELETE);
        }
        catch (HttpServerErrorException ex){
            HttpServerErrorExceptionHandler(ex, id, HttpMethod.DELETE);
        }
        catch (IllegalStateException ex){
            throw new InternalServerErrorException("Working with database server is not available");
        }
        catch (Exception e){
            throw e;
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add")
    public ResponseEntity create(@RequestBody Team team){
        String createUrl = url + "/add";

        ResponseEntity re = PostOperation(team, createUrl);

        return re;
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Team team){
        String updurl = url + "update";

        ResponseEntity re = PostOperation(team, updurl);

        return re;
    }



    private ResponseEntity PostOperation(Team team, String url){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Team> request = new HttpEntity<>(team, headers);

        ResponseEntity entity = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

        Message message;

        if (entity.getStatusCode().is2xxSuccessful())
        {
            message = new Message("Operation of team insert/update successful", HttpMethod.POST, entity.getStatusCode(), team, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        } else {
            message = new Message("Operation of team insert/update unsuccessful", HttpMethod.POST, entity.getStatusCode(), team, entity.getBody().toString());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        }
        return entity;

    }

    private void IllegalStateExceptionHandler(IllegalStateException ex){
        Message message = new Message("Service is unavailable", HttpMethod.GET, HttpStatus.INTERNAL_SERVER_ERROR, (Team) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new InternalServerErrorException("Working with database server is not available");
    }

    private void HttpClientErrorExceptionHandler(HttpClientErrorException ex, long id, HttpMethod httpMethod){
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
        {
            Message message = new Message("Instance team not found", httpMethod, HttpStatus.NOT_FOUND, (Team) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new EntityNotFoundException("Team with id = \'" + id + "\' does not exist");
        }
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST)
        {
            Message message = new Message("Bad request", httpMethod, HttpStatus.BAD_REQUEST, (Team) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new InadmissiblefieldsException(ex.getMessage());
        }
        if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
        {
            Message message = new Message("The Service is unavailable", httpMethod, HttpStatus.SERVICE_UNAVAILABLE, (Team) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new ServiceIsUnavailable("Service unavailable - can not get response");
        }
    }

    private void HttpServerErrorExceptionHandler(HttpServerErrorException ex, long id, HttpMethod httpMethod){
        Message message = new Message("Something went wrong", httpMethod, HttpStatus.NOT_FOUND, (Team) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new EntityNotFoundException("Team with id = \\'\" + id + \"\\' does not exist");
    }
}


