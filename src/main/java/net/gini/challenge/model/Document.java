package net.gini.challenge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Document {
	
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private int id;
	 
	 @Column
	 private String type;
	 
	 @Column
	 private String serial_number;
	 
	 @Column
	 private int pages;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
	 
	 
	 
	 
	 
	 
}
