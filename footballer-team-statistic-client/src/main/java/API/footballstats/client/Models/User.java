package API.footballstats.client.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "users")
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;

    public User(){

    }
    public User(String username, String password, Boolean enabled){
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
