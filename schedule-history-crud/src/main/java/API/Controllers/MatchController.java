package API.Controllers;

import API.Exceptions.InAdmissibleFieldsException;
import API.Exceptions.NotFoundException;
import API.Models.Footballer;
import API.Models.Match;
import API.Models.ScoredGoal;
import API.Models.Team;
import API.Repositories.MatchRepository;
import API.Repositories.ScoredGoalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.groups.Default;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class MatchController {
    private String championshipUrl = "http://footballer-team-server:8100/championships/";
    private String teamUrl = "http://footballer-team-server:8100/teams/";
    private String footballerUrl = "http://footballer-team-server:8100/footballers/";


    private MatchRepository matchRepository;
    private ScoredGoalRepository goalRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public MatchController(MatchRepository matchRepository, ScoredGoalRepository goalRepository) {
        this.matchRepository = matchRepository;
        this.goalRepository = goalRepository;
    }

    @GetMapping("")
    public List<Match> matches(){
        return (List<Match>) matchRepository.findAll();
    }

    @GetMapping("{id}")
    public Match getMatch(@PathVariable UUID id){
        Optional<Match> res = matchRepository.findById(id);
        if (!res.isPresent())
            throw new NotFoundException("Match with id = \'" + id + "\' does not exist");
        return res.get();
    }

    @PostMapping("")
    public Match insert(@RequestBody Match match){
        if (match.isPlayed())
            throw new InAdmissibleFieldsException("Inserted match cannot be played");
        return matchRepository.save(match);
    }

    @PostMapping("{id}")
    public Match update(@PathVariable UUID id, @RequestBody Match match){
        if (!matchRepository.existsById(id) || !Objects.equals(id, match.getId()))
            throw new NotFoundException("Match with id = \'" + id + "\' does not exist");
        if (match.isPlayed())
            throw new InAdmissibleFieldsException("Cant change played match");
        return matchRepository.save(match);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id){
        if (!matchRepository.existsById(id))
            throw new NotFoundException("Match with id = \'" + id + "\' does not exist");
        if (!matchRepository.findById(id).get().isPlayed())
            throw new InAdmissibleFieldsException("Cant change played match");
        matchRepository.deleteById(id);
    }

    @PostMapping("{id}/generate")
    public Match generateMatch(@PathVariable UUID id) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<Match> optionalMatch = matchRepository.findById(id);
        if (!optionalMatch.isPresent())
            throw new NotFoundException("Match with id = \'" + id + "\' does not exist");
        Match match = optionalMatch.get();
        if (match.isPlayed())
            throw new InAdmissibleFieldsException("Match is already played");
        if (match.getHomeTeamObj() == null)
            throw new InAdmissibleFieldsException("Home team does not exist");
        if (match.getAwayTeamObj() == null)
            throw new InAdmissibleFieldsException("Away team does not exist");
        generateMatch(match);

        match.setPlayed(true);
        matchRepository.save(match);

        if (match.getHomeGoals() > match.getAwayGoals()) {
            Team homet = mapper.readValue(mapper.writeValueAsString(restTemplate.getForObject(teamUrl + match.getHomeTeam(), Team.class)), Team.class);
            homet.setPoints(homet.getPoints() + 3);
            PostOperation(homet, teamUrl + "update");
        } else if (match.getHomeGoals() < match.getAwayGoals()){
            Team awayt = mapper.readValue(mapper.writeValueAsString(restTemplate.getForObject(teamUrl + match.getAwayTeam(), Team.class)), Team.class);
            awayt.setPoints(awayt.getPoints() + 3);
            PostOperation(awayt, teamUrl + "update");
        } else {
            Team homet = mapper.readValue(mapper.writeValueAsString(restTemplate.getForObject(teamUrl + match.getHomeTeam(), Team.class)), Team.class);
            homet.setPoints(homet.getPoints() + 1);
            PostOperation(homet, teamUrl + "update");
            Team awayt = mapper.readValue(mapper.writeValueAsString(restTemplate.getForObject(teamUrl + match.getAwayTeam(), Team.class)), Team.class);
            awayt.setPoints(awayt.getPoints() + 1);
            PostOperation(awayt, teamUrl + "update");
        }

        return match;
    }

    private void generateMatch(Match match) throws IOException {
        if (match.isPlayed())
            return;
        Random r = new Random();
        for (short i = 0; i < 90; i++){
            int val = r.nextInt(100);
            if (val < 5){
                if (val < 3) {
                    generateGoal(match.getHomeTeamObj(), match, i, r);
                    match.setHomeGoals(match.getHomeGoals() + 1);
                } else {
                    generateGoal(match.getAwayTeamObj(), match, i, r);
                    match.setAwayGoals(match.getAwayGoals() + 1);
                }
            }
        }
        incrementFootballersStats(match);
    }

    private ScoredGoal generateGoal(Team team, Match match, short minute, Random r) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Footballer> lsit = restTemplate.getForObject(teamUrl + team.getId() + "/footballers", List.class);
        List<Footballer> footballers = mapper.readValue(mapper.writeValueAsString(lsit), (new ArrayList<Footballer>()).getClass());
        if (footballers == null)
            footballers = new ArrayList<>();
        if (!footballers.isEmpty()) {
            int fint = r.nextInt(footballers.size());
            Footballer f = mapper.readValue(mapper.writeValueAsString(footballers.get(fint)), Footballer.class);
//            Footballer f = footballers.get(fint);
            f.setGoals(f.getGoals() + 1);
            PostOperation(f, footballerUrl + "update");
            if (r.nextInt(50) < 30){
                int faint = -1;
                int count = 0;
                do {
                    faint = r.nextInt(footballers.size());
                    count++;
                } while ((faint != fint && faint >= 0) || count < 20);
                Footballer f2 = mapper.readValue(mapper.writeValueAsString(footballers.get(faint)), Footballer.class);
//                Footballer f2 = footballers.get(faint);
                f2.setAssists(f2.getAssists() + 1);
                PostOperation(f2, footballerUrl + "update");
            }

        }
        return new ScoredGoal(match, minute);
    }
    private void incrementFootballersStats(Match match){
        List<Footballer> footballers = restTemplate.getForObject(teamUrl + match.getHomeTeam() + "/footballers", List.class);
        if (footballers != null && !footballers.isEmpty())
            for (Footballer f : footballers)
            {
                f.setGames(f.getGames() + 1);
                PostOperation(f, footballerUrl + "update");
            }
        footballers = restTemplate.getForObject(teamUrl + match.getAwayTeam() + "/footballers", List.class);
        if (footballers != null && !footballers.isEmpty())
            for (Footballer f : footballers)
            {
                f.setGames(f.getGames() + 1);
                PostOperation(f, footballerUrl + "update");
            }
    }


    private ResponseEntity PostOperation(Team team, String url){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Team> request = new HttpEntity<>(team, headers);

        ResponseEntity entity = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

        return entity;

    }

    public ResponseEntity PostOperation(Footballer footballer, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(footballer.toString(), headers);

        ResponseEntity entity = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<ResponseEntity>() {
        });

        return entity;
    }

}
