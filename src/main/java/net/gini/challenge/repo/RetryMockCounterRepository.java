package net.gini.challenge.repo;
import org.springframework.data.repository.CrudRepository;
import net.gini.challenge.model.RetryMockCounter;

public interface RetryMockCounterRepository extends CrudRepository<RetryMockCounter, Integer>{

}
