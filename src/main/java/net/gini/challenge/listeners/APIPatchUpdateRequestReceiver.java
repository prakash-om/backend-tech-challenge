package net.gini.challenge.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.gini.challenge.dao.DocumentDAOImpl;
import net.gini.challenge.exception.GiniChallengeException;
import net.gini.challenge.model.DocumentIdPages;
import net.gini.challenge.service.BiqQueuePatchServiceImpl;

@Service
public class APIPatchUpdateRequestReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(APIPatchUpdateRequestReceiver.class);

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	BiqQueuePatchServiceImpl bigQueueService;

	public void receiveMessage(DocumentIdPages document) throws GiniChallengeException {
		// write back to database
		LOG.info("Patch Request has been received for id " + document.getId());
		bigQueueService.pushDataToBigQueue(document);
	}
}
