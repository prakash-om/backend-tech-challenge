package com.gini.challenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.gini.challenge.model.DocumentIdPages;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class DocumentIdPagesTest {
	
	DocumentIdPages documentIdpages;
	
	@BeforeEach
	public void setUp() {
		documentIdpages = new DocumentIdPages();
		documentIdpages.setId(1);
		documentIdpages.setPages(5);
	}
	
	@Test
	public void checkId() {
		assertEquals(1, documentIdpages.getId());
	}
	
	@Test
	public void checkpages() {
		assertEquals(5, documentIdpages.getPages());
	}
	

}
