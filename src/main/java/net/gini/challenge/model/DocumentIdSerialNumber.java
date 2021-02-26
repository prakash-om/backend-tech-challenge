package net.gini.challenge.model;

import java.io.Serializable;


public class DocumentIdSerialNumber implements Serializable{

	//This class is only present to rabbitMq communications
	private static final long serialVersionUID = -1611033413366552059L;
	
	private int id;
	private String searialNumber;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSearialNumber() {
		return searialNumber;
	}
	public void setSearialNumber(String searialNumber) {
		this.searialNumber = searialNumber;
	}
	
	public DocumentIdSerialNumber(int id, String searialNumber) {
		this.id = id;
		this.searialNumber = searialNumber;
	}
	

}
