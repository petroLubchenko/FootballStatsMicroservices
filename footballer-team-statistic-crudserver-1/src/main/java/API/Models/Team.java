package API.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.hibernate.annotations.ManyToAny;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;



@Entity
@Table(name = "TEAM")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private long id;
    @Column(name = "TEAM_NAME")
    private String name;
    @Column(name = "TEAM_SEASONSCOUNT")
    private int seasonscount;
    @Column(name = "TEAM_CITY")
    private String city;
    @Column(name = "TEAM_STADIUMNAME")
    private String stadiumname;
    @JsonIgnore
    private boolean deleted;
    private int points;
    private int scored;
    private int concended;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne(cascade = CascadeType.REMOVE, optional = true, targetEntity = Championship.class)
    private Championship championship;

    public Team(){
        super();
    }

    public Team(String name, String city){
        this.name = name;
        this.city = city;
        this.seasonscount = 0;
        this.stadiumname = null;
        deleted = false;
    }

    public Team(String name, String city, String stadiumname, int seasonscount){
        this.name = name;
        this.city = city;
        this.stadiumname = stadiumname;
        this.seasonscount = seasonscount;
        deleted = false;
    }

    public Team(long id, String name, String city, String stadiumname, int seasonscount){
        this.id = id;
        this.name = name;
        this.city = city;
        this.stadiumname = stadiumname;
        this.seasonscount = seasonscount;
        deleted = false;
    }
    public Team(long id, String name, String city, String stadiumname, int seasonscount, boolean deleted){
        this.id = id;
        this.name = name;
        this.city = city;
        this.stadiumname = stadiumname;
        this.seasonscount = seasonscount;
        this.deleted = deleted;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public boolean getDeleted(){
        return deleted;
    }

    public Championship getChampionship() {
        return championship;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }
}
