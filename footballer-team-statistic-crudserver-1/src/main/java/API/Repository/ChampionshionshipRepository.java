package API.Repository;

import API.Models.Championship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionshionshipRepository extends CrudRepository<Championship, Long> {

}
