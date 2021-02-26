package com.gini.challenge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import net.gini.challenge.model.RetryMockCounter;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class RetryMockCounterTest {
	
	
	RetryMockCounter retryCounter;
	
	@BeforeEach
	public void setUp() {
		
		retryCounter = new RetryMockCounter();
		retryCounter.setId(100);
		retryCounter.setRetryCount(100);
	}
	
	@Test
	public void checkRetryCounter() {
	
		assertEquals(100, retryCounter.getRetryCount());
	}
}
