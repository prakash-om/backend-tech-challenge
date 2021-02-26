package com.gini.challenge.model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.gini.challenge.model.QueryOffsetStatus;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class QueryOffsetStatusTest {
	
	QueryOffsetStatus qstatus;
	
	@BeforeEach
	public void setUp() {
		qstatus = new QueryOffsetStatus();
		qstatus.setId(1);
		qstatus.setStartIndex(0);
	}
	
	
	@Test
	public void checkId() {
		assertEquals(1,qstatus.getId());
	}
	
	@Test
	public void checkStart() {
		assertEquals(0, qstatus.getStartIndex());
	}

}
