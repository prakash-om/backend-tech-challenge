package com.gini.challenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.gini.challenge.model.Document;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class DocumentTest {

	Document document;
	
	@BeforeEach
	public void setUp() {
		document = new Document();
		document.setId(1);
		document.setPages(4);
		document.setSerial_number("test");
		document.setType("PDF");
	}
	
	@Test
	public void checkID() {
		assertEquals(1, document.getId());
	}
	
	@Test
	public void checkPages() {
		assertEquals(4, document.getPages());
	}
	
	@Test
	public void checkSerialNumber() {
		assertEquals("test", document.getSerial_number());
	}
	
	@Test
	public void checkType() {
		assertEquals("PDF", document.getType());
	}
	
	
	
	
}
