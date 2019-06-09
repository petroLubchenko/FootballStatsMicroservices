package API.footballstats.client.Controllers;

import API.footballstats.client.Application;
import API.footballstats.client.Exceptions.EntityNotFoundException;
import API.footballstats.client.Exceptions.InadmissiblefieldsException;
import API.footballstats.client.Exceptions.InternalServerErrorException;
import API.footballstats.client.Exceptions.ServiceIsUnavailable;
import API.footballstats.client.Models.Championship;
import API.footballstats.client.Models.Message;
import API.footballstats.client.Models.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/championships")
public class ChampionshipController {
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
    public ChampionshipController(@Value("${kafka.bootstrap.servers}") String KafkaBootstrapServers){
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

    private String url = "http://footballer-team-server/championships/";
    private String teamurl = "http://footballer-team-server/teams/";
    private String matchurl = "http://match-history-service/";

    @GetMapping("/")
    public String getAll(Model model) throws IOException {
        try {
            List<Championship> object = restTemplate.getForObject(url, List.class);
            Message message = new Message("Getting all objects from database", HttpMethod.GET, HttpStatus.OK, (Championship) null, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();

            model.addAttribute("name", name);
            System.out.println(object);
            model.addAttribute("championships", object);

            return "championshiplist";
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }
        return "mainpage";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable long id, Model model){
        String getoneUrl = url + id;
        Championship object = null;
        try {
            object = restTemplate.getForObject(getoneUrl, Championship.class);
            Message message;

            if (object instanceof Championship)
                message = new Message("Getting a footballer with id = " + id, HttpMethod.GET, HttpStatus.OK, (Championship)object, "");
            else
                message = new Message("Getting an object with id = " + id, HttpMethod.GET, HttpStatus.OK, (Championship) null, "Received object is not a Footballer");

            Application.sendKafkaMessage(message.toString(), producer, topicName);

            model.addAttribute("championship", object);
            model.addAttribute("teams", ((List<Team>)(restTemplate.getForObject(teamurl, List.class))));

            return "championship";
        }
        catch (HttpClientErrorException ex){
            HttpClientErrorExceptionHandler(ex, id, HttpMethod.GET);
        }
        catch (IllegalStateException ex){
            IllegalStateExceptionHandler(ex);
        }

        return "championship";
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

        return new ModelAndView("redirect:/championships/");
    }

    @PostMapping("/add")
    public ModelAndView create(@ModelAttribute Championship championship){
        String createUrl = url + "/add";

        ResponseEntity re = PostOperation(championship, createUrl);

        return new ModelAndView("redirect:/championships/");
    }

    @PostMapping("/update/{id}")
    public ModelAndView update(@PathVariable long id, @ModelAttribute Championship championship){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        String getoneUrl = url + id;

        ModelAndView model = new ModelAndView("championship");

        PostOperation(championship, url + "update/");

        Championship object = restTemplate.getForObject(getoneUrl, Championship.class);
        model.addObject("teams", ((List<Team>)(restTemplate.getForObject(teamurl, List.class))));

        model.addObject("name", name);

        model.addObject("footballer", object);
        return model;
    }

    @GetMapping("/{id}/teams")
    public String getChTeams(@PathVariable long id, Model model) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Team> teams = restTemplate.getForObject(url + id + "/teams", List.class);

        if (teams == null)
            return "teamsch";

        List<Team> realteams = new ArrayList<>();

        for (Object t : teams)
            realteams.add(mapper.readValue(mapper.writeValueAsString(t), Team.class));

        realteams.sort(Comparator.reverseOrder());
        model.addAttribute("teams", realteams);
        model.addAttribute("championship", (Championship)restTemplate.getForObject(url + id, Championship.class));
        return  "teamsch";
    }

    @GetMapping("/{id}/teams/remove/{tid}")
    public ModelAndView removeTeam(@PathVariable long id, @PathVariable long tid) throws IOException {
        Team f = restTemplate.getForObject(teamurl + tid, Team.class);
        if (f == null)
            return new ModelAndView("redirect:/championships/" + id + "/teams");

        f.setChampionship(null);

        String s = f.toString();

        Team ff = new ObjectMapper().readValue(s, Team.class);

        System.out.println(f.toString());
        PostOperation(ff, teamurl + "update/");
        return new ModelAndView("redirect:/championships/" + id + "/teams");

    }

    @GetMapping("/{id}/teams/{tid}/match")
    public String matchMake(@PathVariable long id, @PathVariable long tid Model model) throws IOException {
        Team f = restTemplate.getForObject(teamurl + tid, Team.class);
       /* if (f == null)
            return new ModelAndView("redirect:/championships/" + id + "/teams");*/

        f.setChampionship(null);

        String s = f.toString();

        Team ff = new ObjectMapper().readValue(s, Team.class);

        System.out.println(f.toString());
        PostOperation(ff, teamurl + "update/");
        return "matchcreate";

    }


    private ResponseEntity PostOperation(Championship championship, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Championship> request = new HttpEntity<>(championship, headers);

        ResponseEntity entity = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

        Message message;

        if (entity.getStatusCode().is2xxSuccessful())
        {
            message = new Message("Operation of insert/update successful", HttpMethod.POST, entity.getStatusCode(), championship, "");
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        } else {
            message = new Message("Operation of insert/update unsuccessful", HttpMethod.POST, entity.getStatusCode(), championship, entity.getBody().toString());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
        }


        return entity;
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
        Message message = new Message("Service is unavailable", HttpMethod.GET, HttpStatus.INTERNAL_SERVER_ERROR, (Championship) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new InternalServerErrorException("Working with database server is not available");
    }

    private void HttpClientErrorExceptionHandler(HttpClientErrorException ex, long id, HttpMethod httpMethod){
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
        {
            Message message = new Message("Instance not found", httpMethod, HttpStatus.NOT_FOUND, (Championship) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new EntityNotFoundException("Footballer with id = \'" + id + "\' does not exist");
        }
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST)
        {
            Message message = new Message("Bad request", httpMethod, HttpStatus.BAD_REQUEST, (Championship) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new InadmissiblefieldsException(ex.getMessage());
        }
        if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
        {
            Message message = new Message("The Service is unavailable", httpMethod, HttpStatus.SERVICE_UNAVAILABLE, (Championship) null, ex.getMessage());
            Application.sendKafkaMessage(message.toString(), producer, topicName);
            throw new ServiceIsUnavailable("Service unavailable - can not get response");
        }
    }

    private void HttpServerErrorExceptionHandler(HttpServerErrorException ex, long id, HttpMethod httpMethod){
        Message message = new Message("Something went wrong", httpMethod, HttpStatus.NOT_FOUND, (Championship) null, ex.getMessage());
        Application.sendKafkaMessage(message.toString(), producer, topicName);
        throw new EntityNotFoundException("Footballer with id = \\'\" + id + \"\\' does not exist");
    }

}
