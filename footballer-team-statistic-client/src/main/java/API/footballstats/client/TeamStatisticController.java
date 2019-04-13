package API.footballstats.client;

import API.footballstats.client.Exceptions.EntityNotFoundException;
import API.footballstats.client.Exceptions.ServiceIsUnavailable;
import API.footballstats.client.Models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.POST;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/teams")
@RestController
public class TeamStatisticController {

    @Autowired
    RestTemplate restTemplate;

    private String url = "http://footballer-team-server/teams/";

    @GetMapping("/all")
    public List getAll() throws IOException {

        return restTemplate.getForObject(url, List.class);

    }

    @GetMapping("/{id}")
    public Object getOne(@PathVariable long id){
        String getoneUrl = url + id;
        try {
            return restTemplate.getForObject(getoneUrl, Object.class);
        }
        catch (HttpClientErrorException e){
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new EntityNotFoundException("team with id = \'" + id + "\' does not exist");
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
        catch (HttpClientErrorException ex){
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new EntityNotFoundException("Team with id = \'" + id + "\' does not exist");
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

        return restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

    }
}
