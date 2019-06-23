package API.footballstats.client.Controllers;

import API.footballstats.client.Application;
import API.footballstats.client.Exceptions.EntityNotFoundException;
import API.footballstats.client.Exceptions.InadmissiblefieldsException;
import API.footballstats.client.Exceptions.InternalServerErrorException;
import API.footballstats.client.Exceptions.ServiceIsUnavailable;
import API.footballstats.client.Models.Footballer;
import API.footballstats.client.Models.Message;
import API.footballstats.client.Models.Team;
import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Controller("FootballerController")
@RequestMapping("/footballers")
@EnableWebSecurity
public class FootballerStatisticController {
    @Value("${kafka.topic.footballer}")
    private String topicName;

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${zookeeper.groupId}")
    private String zookeeperGroupId;

    @Value("${zookeeper.host}")
    String zookeeperHost;

    KafkaProducer<String, String> producer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    public FootballerStatisticController(@Value("${kafka.bootstrap.servers}") String KafkaBootstrapServers){
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

    //private String url = "http://tfcservice:8100/footballers/";
    private String url = "http://footballer-team-server:8100/footballers/";
    private String teamurl = "http://footballer-team-server:8100/teams/";

    @GetMapping("/all")
    public String getAll(Model model) throws IOException {
        try {
            List<Footballer> object = restTemplate.getForObject(url, List.class);
            Message message = new Message("Getting all objects from database", HttpMethod.GET, HttpStatus.OK, (Footballer) null, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();

            model.addAttribute("name", name);
            System.out.println(object);
            model.addAttribute("footballer", object);

            return "footballerlist";
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }
        return "mainpage";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable long id, Model model){
        String getoneUrl = url + id;
        Footballer object = null;
        try {
            object = restTemplate.getForObject(getoneUrl, Footballer.class);
            Message message;

            if (object instanceof Footballer)
                message = new Message("Getting a footballer with id = " + id, HttpMethod.GET, HttpStatus.OK, (Footballer)object, "");
            else
                message = new Message("Getting an object with id = " + id, HttpMethod.GET, HttpStatus.OK, (Footballer) null, "Received object is not a Footballer");

            Application.sendKafkaMessage(message.toString(), producer, topicName);

            model.addAttribute("footballerobj", object);
            model.addAttribute("teams", ((List<Team>)(restTemplate.getForObject(teamurl, List.class))));

            return "footballer";
        }
        catch (HttpClientErrorException ex){
            HttpClientErrorExceptionHandler(ex, id, HttpMethod.GET);
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }
        model.addAttribute("footballer", object);
        model.addAttribute("teams", (List<Team>)(restTemplate.getForObject(teamurl, List.class)));

        return "footballer";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable long id){
        String delurl = url + id;

        try{
            restTemplate.delete(delurl);
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

        return new ModelAndView("redirect:/footballers/all");
    }

    @PostMapping("/add")
    public ModelAndView create(@ModelAttribute Footballer footballer){
        String createUrl = url + "/add";

        ResponseEntity re = PostOperation(footballer, createUrl);

        return new ModelAndView("redirect:/footballers/all");
    }

    @PostMapping("/update/{id}")
    public ModelAndView update(@PathVariable long id, @ModelAttribute Footballer footballer){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        String getoneUrl = url + id;

        ModelAndView model = new ModelAndView("footballer");

        if (footballer != null && footballer.getTeam() != null && footballer.getTeam().getId() <= 0)
            footballer.setTeam(null);

        PostOperation(footballer, url + "update/");

        Footballer object = restTemplate.getForObject(getoneUrl, Footballer.class);
        model.addObject("teams", ((List<Team>)(restTemplate.getForObject(teamurl, List.class))));

        model.addObject("name", name);

        model.addObject("footballerobj", object);
        return model;
    }



    public ResponseEntity PostOperation(Footballer footballer, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(footballer.toString(), headers);

        ResponseEntity entity = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

        Message message;

        if (entity.getStatusCode().is2xxSuccessful())
        {
            message = new Message("Operation of insert/update successful", HttpMethod.POST, entity.getStatusCode(), footballer, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        } else {
            message = new Message("Operation of insert/update unsuccessful", HttpMethod.POST, entity.getStatusCode(), footballer, entity.getBody().toString());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        }


        return entity;
    }

    private void IllegalStateExceptionHandler(IllegalStateException ex){
        Message message = new Message("Service is unavailable", HttpMethod.GET, HttpStatus.INTERNAL_SERVER_ERROR, (Footballer) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new InternalServerErrorException("Working with database server is not available");
    }

    private void HttpClientErrorExceptionHandler(HttpClientErrorException ex, long id, HttpMethod httpMethod){
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
        {
            Message message = new Message("Instance not found", httpMethod, HttpStatus.NOT_FOUND, (Footballer) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new EntityNotFoundException("Footballer with id = \'" + id + "\' does not exist");
        }
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST)
        {
            Message message = new Message("Bad request", httpMethod, HttpStatus.BAD_REQUEST, (Footballer) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new InadmissiblefieldsException(ex.getMessage());
        }
        if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
        {
            Message message = new Message("The Service is unavailable", httpMethod, HttpStatus.SERVICE_UNAVAILABLE, (Footballer) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new ServiceIsUnavailable("Service unavailable - can not get response");
        }
    }

    private void HttpServerErrorExceptionHandler(HttpServerErrorException ex, long id, HttpMethod httpMethod){
        Message message = new Message("Something went wrong", httpMethod, HttpStatus.NOT_FOUND, (Footballer) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new EntityNotFoundException("Footballer with id = \\'\" + id + \"\\' does not exist");
    }
}
