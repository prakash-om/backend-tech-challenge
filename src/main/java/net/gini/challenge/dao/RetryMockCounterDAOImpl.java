package net.gini.challenge.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gini.challenge.model.RetryMockCounter;
import net.gini.challenge.repo.RetryMockCounterRepository;

@Service
public class RetryMockCounterDAOImpl implements RetryMockCounterDAO{

	
	@Autowired
	RetryMockCounterRepository retryRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public boolean ifExists(int id) {
		return retryRepo.existsById(id);
	}

	@Override
	public void update(RetryMockCounter retryCounter) {
		retryRepo.save(retryCounter);
		
	}

	@Override
	public RetryMockCounter findById(int id) {
		return retryRepo.findById(id).get();
		
	}

	@Override
	@Transactional
	public void reset() {
		entityManager.flush();
		entityManager.clear();
		retryRepo.deleteAll();
		
	}

}
