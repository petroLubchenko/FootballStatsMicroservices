package API.footballstats.client.Security;

import API.footballstats.client.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthService extends JpaRepository<User, Integer> {
}
