package API.footballstats.client.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;
import java.util.UUID;

public class Match {
    @JsonIgnore
    private static RestTemplate restTemplate = new RestTemplate();
    @JsonIgnore
    private String teamUrl = "http://services:8100/teams/";

    private UUID id;
    private long homeTeam;
    private long awayTeam;
    private int homeGoals;
    private int awayGoals;
    private boolean played;

    public boolean isPlayed() {
        return played;
    }

    public Match() {
    }

    public Match(UUID id, Team homeTeam, Team awayTeam, int homeGoals, int awayGoals) {
        this.id = id;
        this.homeTeam = homeTeam.getId();
        this.awayTeam = awayTeam.getId();
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam.getId();
        this.awayTeam = awayTeam.getId();
        homeGoals = 0;
        awayGoals = 0;
    }

    public Match(UUID id, Team homeTeam, Team awayTeam) {
        this.id = id;
        this.homeTeam = homeTeam.getId();
        this.awayTeam = awayTeam.getId();
        homeGoals = 0;
        awayGoals = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonIgnore
    public Team getHomeTeamObj() {
        return homeTeam != 0 ? restTemplate.getForObject(teamUrl + homeTeam, Team.class) : null;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam.getId();
        homeGoals = 0;
    }

    @JsonIgnore
    public Team getAwayTeamObj() {
        return awayTeam != 0 ? restTemplate.getForObject(teamUrl + awayTeam, Team.class) : null;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam.getId();
        awayGoals = 0;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public void setHomeTeam(long homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(long awayTeam) {
        this.awayTeam = awayTeam;
    }

    public long getHomeTeam() {
        return homeTeam;
    }

    public long getAwayTeam() {
        return awayTeam;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }
}
