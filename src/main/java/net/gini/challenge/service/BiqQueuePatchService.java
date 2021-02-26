package net.gini.challenge.service;

import net.gini.challenge.exception.GiniChallengeException;
import net.gini.challenge.model.DocumentIdPages;

public interface BiqQueuePatchService {

	public void pushDataToBigQueue(DocumentIdPages document) throws GiniChallengeException;
}
