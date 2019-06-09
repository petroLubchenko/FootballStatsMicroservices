package API.Repositories;

import API.Models.ScoredGoal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScoredGoalRepository extends CrudRepository<ScoredGoal, UUID> {

}
