package API.footballstats.client.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

public class ScoredGoal {
    private long id;
    private Match match;
    private short minute;

    public ScoredGoal() {
    }

    public ScoredGoal(Match match, short minute) {
        this.match = match;
        this.minute = minute;
    }

    public ScoredGoal(long id, Match match, short minute) {
        this.id = id;
        this.match = match;
        this.minute = minute;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public short getMinute() {
        return minute;
    }

    public void setMinute(short minute) {
        this.minute = minute;
    }
}
