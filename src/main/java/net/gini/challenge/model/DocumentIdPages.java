package net.gini.challenge.model;

import java.io.Serializable;

public class DocumentIdPages implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	int pages;
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	

}
