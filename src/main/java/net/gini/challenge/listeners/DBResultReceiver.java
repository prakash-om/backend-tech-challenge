package net.gini.challenge.listeners;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.gini.challenge.dao.DocumentDAOImpl;
import net.gini.challenge.dao.RetryMockCounterDAOImpl;
import net.gini.challenge.mock.ExpensiveClientAPIMock;
import net.gini.challenge.model.DocumentIdSerialNumber;
import net.gini.challenge.model.RetryMockCounter;
import net.gini.challenge.senders.PushDataSetToApiExchangeService;
import net.gini.challenge.senders.PushDataSetToDBExchangeService;
import net.gini.challenge.utils.Constants;

@Component
public class DBResultReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(DBResultReceiver.class);

	@Autowired
	ExpensiveClientAPIMock expesiveClientAPIMock;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	RetryMockCounterDAOImpl retryDAOimpl;

	@Autowired
	PushDataSetToApiExchangeService pushDataSetToApiExchangeService;

	@Autowired
	PushDataSetToDBExchangeService pushDataSetToDBExchangeService;

	@Value("${client.api.uri}")
	String url;

	@Value("${mock.retry.counter}")
	int retryCounter;

	ExecutorService executorService;

	@Autowired
	public DBResultReceiver(@Value("${thread.pool.size.interact.mock}") int poolSize) {
		executorService = Executors.newFixedThreadPool(poolSize);
	}

	public void receiveMessage(Integer id) {
		LOG.info("Message Received and doc id is " + id);
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				//Connecting to mock service
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				headers.set(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<String> str = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

				LOG.info(str.getBody());

				JSONObject obj = new JSONObject(str.getBody());

				String status = obj.getString(Constants.PROCESSING_STATUS);
				String serialNumber = obj.getString(Constants.INVOICE_ID);

				if (status.equals(Constants.PROCESSING_STATUS_OK)) {
					LOG.info("Success for id " + id);
					pushDataSetToApiExchangeService.sendToApiExchange(new DocumentIdSerialNumber(id, serialNumber));

				} else {

					//Retry 
					LOG.info("Retrying for id " + id);
					if (retryDAOimpl.ifExists(id)) {

						RetryMockCounter retryMock = retryDAOimpl.findById(id);
						int count = retryMock.getRetryCount();
						if (count <= retryCounter) {
							retryMock.setRetryCount(count + 1);
							retryDAOimpl.update(retryMock);
							LOG.info("Updating the retry counter and trying again");
							pushDataSetToDBExchangeService.sendToDBExchange(id);
						} else {

							LOG.info("Max Retry has been tried for id " + id + " please try in next schedule");
						}

					} else {
						RetryMockCounter retryMock = new RetryMockCounter();
						retryMock.setId(id);
						retryMock.setRetryCount(1);
						retryDAOimpl.update(retryMock);
						pushDataSetToDBExchangeService.sendToDBExchange(id);
					}

				}

			}
		});
	}
}
