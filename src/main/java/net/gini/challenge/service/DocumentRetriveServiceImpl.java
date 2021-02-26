package net.gini.challenge.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.gini.challenge.dao.DocumentDAOImpl;
import net.gini.challenge.dao.QueryStatusDAOImpl;
import net.gini.challenge.senders.PushDataSetToDBExchangeService;

@Service
public class DocumentRetriveServiceImpl implements DocumentRetriveService{

	@Value("${db.max.rows}")
	private int max;

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	PushDataSetToDBExchangeService pushToQueue;

	@Autowired
	QueryStatusDAOImpl queryStatusRepoImpl;
	


	private static final Logger LOG = LoggerFactory.getLogger(DocumentRetriveServiceImpl.class);

	@Override
	@Transactional
	public List<Integer> retriveDocumentsWithNullSerialNumber(int start) {

		// Needs to be fetched from cache
		return documentRepoImpl.findDocumentBySerialNumbers(start, max);

	}
	
	@Override
	public void pushMessagesToQueue(List<Integer> docIds) {
		
		LOG.info("Pushing messages to DB exchange "+docIds.size());
		docIds.parallelStream().forEach(id -> {
			pushToQueue.sendToDBExchange(id);
		});
	}
	
	@Override
	public long getCountOfNullRecords() {
		return documentRepoImpl.getCountOfSerialNumberWithNull();
	}

}
