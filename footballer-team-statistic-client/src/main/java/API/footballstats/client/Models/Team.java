package API.footballstats.client.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team implements Comparable<Team> {
    private long id;
    private String name;
    private int seasonscount;
    private String city;
    private String stadiumname;
    private int points;
    private int scored;
    private int concended;
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private Championship championship;

    public Team(){
        super();
    }

    public Team(String name, String city){
        this.name = name;
        this.city = city;
        this.seasonscount = 0;
        this.stadiumname = null;
    }

    public Team(String name, String city, String stadiumname, int seasonscount){
        this.name = name;
        this.city = city;
        this.stadiumname = stadiumname;
        this.seasonscount = seasonscount;
    }

    public Team(long id, String name, String city, String stadiumname, int seasonscount){
        this.id = id;
        this.name = name;
        this.city = city;
        this.stadiumname = stadiumname;
        this.seasonscount = seasonscount;
    }


    public boolean isValid(boolean checkId){
        if (checkId && id != 0L)
            return true;
        if (name != null && stadiumname != null)
            return true;
        return false;
    }

    @Override
    public boolean equals(Object object){
        if (object != null)
            if (object instanceof Team)
                return this.id == ((Team) object).id && Objects.equals(this.name, ((Team) object).name) &&
                        Objects.equals(this.city, ((Team) object).city) && Objects.equals(this.stadiumname, ((Team) object).stadiumname) &&
                        Objects.equals(this.seasonscount, ((Team) object).seasonscount);

        return false;
    }

    @Override
    public String toString(){
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException ex){
            ex.printStackTrace();
        }
        return jsonString;
    }

    public long getId() {
        return id;
    }

    public int getSeasonscount() {
        return seasonscount;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getStadiumname() {
        return stadiumname;
    }

    public Championship getChampionship() {
        return championship;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeasonscount(int seasonscount) {
        this.seasonscount = seasonscount;
    }

    public void setStadiumname(String stadiumname) {
        this.stadiumname = stadiumname;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getScored() {
        return scored;
    }

    public void setScored(int scored) {
        this.scored = scored;
    }

    public int getConcended() {
        return concended;
    }

    public void setConcended(int concended) {
        this.concended = concended;
    }

    @Override
    public int compareTo(Team o) {
        if (points > o.getPoints())
            return 1;
        if (points < o.getPoints())
            return -1;
        if (scored > o.getScored())
            return 1;
        if (scored < o.getScored())
            return -1;
        return 0;
    }
}
