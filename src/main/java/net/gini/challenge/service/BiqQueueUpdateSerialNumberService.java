package net.gini.challenge.service;

import net.gini.challenge.exception.GiniChallengeException;
import net.gini.challenge.model.DocumentIdSerialNumber;

public interface BiqQueueUpdateSerialNumberService {

	public void push(DocumentIdSerialNumber documentIdSerialNum) throws GiniChallengeException;
}
