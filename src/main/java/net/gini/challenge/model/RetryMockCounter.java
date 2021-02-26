package net.gini.challenge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RetryMockCounter {

	@Id
	private int id;
	
	
	@Column
	private int retryCount;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getRetryCount() {
		return retryCount;
	}


	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	
}
