package API.footballstats.client.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "authirities")
@Table(name = "authorities")
public class Authorities {
    @Id
    private String username;
    private String authority;

    public Authorities(){

    }
    public Authorities(String username, String role){
        this.username = username;
        this.authority = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return authority;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.authority = role;
    }
}
