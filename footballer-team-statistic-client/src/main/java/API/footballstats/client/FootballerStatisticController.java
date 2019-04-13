package API.footballstats.client;

import API.footballstats.client.Exceptions.EntityNotFoundException;
import API.footballstats.client.Exceptions.InadmissiblefieldsException;
import API.footballstats.client.Models.Footballer;
import API.footballstats.client.Models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/footballers")
public class FootballerStatisticController {
    @Autowired
    RestTemplate restTemplate;

    private String url = "http://footballer-team-server/footballers/";

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
                throw new EntityNotFoundException("Fotballer with id = \'" + id + "\' does not exist");
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
                throw new EntityNotFoundException("Footballer with id = \'" + id + "\' does not exist");
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST)
                throw new InadmissiblefieldsException(ex.getMessage());


        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add")
    public ResponseEntity create(@RequestBody Footballer footballer){
        String createUrl = url + "/add";

        ResponseEntity re = PostOperation(footballer, createUrl);

        return re;
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Footballer footballer){
        String updurl = url + "update";

        ResponseEntity re = PostOperation(footballer, updurl);

        return re;
    }



    private ResponseEntity PostOperation(Footballer footballer, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Footballer> request = new HttpEntity<>(footballer, headers);

        return restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

    }
}
