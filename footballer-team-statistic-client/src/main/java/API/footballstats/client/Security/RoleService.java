package API.footballstats.client.Security;

import API.footballstats.client.Models.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleService extends JpaRepository<Authorities, Integer> {
}
