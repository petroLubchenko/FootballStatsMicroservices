package API.Models;

import javax.persistence.*;
import java.util.List;

@Entity(name = "championship")
public class Championship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String abbreviatedname;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams;

    public Championship(){}
    public Championship(long id, String name, String abbreviatedname, List<Team> teams){
        this.id = id;
        this.name = name;
        this.abbreviatedname = abbreviatedname;
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public String getAbbreviatedname() {
        return abbreviatedname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAbbreviatedname(String abbreviatedname) {
        this.abbreviatedname = abbreviatedname;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    public void addTeam(Team team){
        teams.add(team);
    }
    public  void removeTeam(Team team){
        teams.remove(team);
    }

    public boolean isValid(boolean checkId){
        if (checkId && id == 0L)
            return false;
        return !(name == null || abbreviatedname == null);
    }
}
