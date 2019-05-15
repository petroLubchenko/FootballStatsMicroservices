package web.API.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumedMessageRep extends CrudRepository<ConsumedMessage, Long> {
}
