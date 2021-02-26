package net.gini.challenge.dao;

import net.gini.challenge.model.RetryMockCounter;

public interface RetryMockCounterDAO {

	public boolean ifExists(int id);
	
	public void update(RetryMockCounter retryCounter);
	
	public RetryMockCounter findById(int id);

	public void reset();
}
