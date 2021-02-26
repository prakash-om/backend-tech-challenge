package net.gini.challenge.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.gini.challenge.dao.QueryStatusDAOImpl;
import net.gini.challenge.dao.RetryMockCounterDAOImpl;

@Service
public class SchedulerService {

	@Autowired
	DocumentRetriveServiceImpl documentRetriverService;
	
	@Autowired
	RetryMockCounterDAOImpl retryMockCounterDAOImpl;

	private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);

	ExecutorService executorService;

	@Autowired
	QueryStatusDAOImpl queryStatusRepo;

	@Autowired
	public SchedulerService(@Value("${thread.pool.size.db.query}") int poolSize) {
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Value("${db.max.rows}")
	int maxRows;

	/**
	 * This is the weekly scheduler which run and kick off the update process
	 * As of now it's for 3 minute we can put cron and run for weekly basis.
	 * 3 Minute is kept only for testing purpose
	 */
	@Scheduled(fixedDelay = 180000)
	public void schedule() {

		LOG.info("Schedule");
		long count = documentRetriverService.getCountOfNullRecords();
		if (count < 1) {
			LOG.info("Nothing to update");
			return;
		}

		LOG.info("Number of Records which needs to be updated " + count);
		for (long i = 0; i <= count / maxRows; i++) {
			LOG.info("Retriving the docs ..."+i);
			List<Integer> listIds;
			int start = queryStatusRepo.getQueryStatus();
			listIds = documentRetriverService.retriveDocumentsWithNullSerialNumber(start);
			documentRetriverService.pushMessagesToQueue(listIds);
		}

		LOG.info("Done with scheduler");
		queryStatusRepo.reset();
		retryMockCounterDAOImpl.reset();

	}

}
