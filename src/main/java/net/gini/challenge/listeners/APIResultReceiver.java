package net.gini.challenge.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.gini.challenge.dao.DocumentDAOImpl;
import net.gini.challenge.exception.GiniChallengeException;
import net.gini.challenge.model.Document;
import net.gini.challenge.model.DocumentIdSerialNumber;
import net.gini.challenge.service.BiqQueueUpdateSerialNumberServiceImpl;

@Component
public class APIResultReceiver {

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	BiqQueueUpdateSerialNumberServiceImpl bigBiqQueueUpdateSerialNumberService;

	private static final Logger LOG = LoggerFactory.getLogger(APIResultReceiver.class);

	List<DocumentIdSerialNumber> list;
	int maxResultSize;

	@Autowired
	public APIResultReceiver(@Value("${db.max.rows}") int maxResultSize) {
		this.maxResultSize = maxResultSize;
		list = new ArrayList<>(maxResultSize);
	}

	public void receiveMessage(DocumentIdSerialNumber documentIdSerialNum) throws GiniChallengeException {
		// write back to database
		LOG.info("Message received from APIQueue for doc " + documentIdSerialNum.getId());

		bigBiqQueueUpdateSerialNumberService.push(documentIdSerialNum);
	}

}
