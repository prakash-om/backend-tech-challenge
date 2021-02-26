package net.gini.challenge.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.leansoft.bigqueue.BigQueueImpl;
import com.leansoft.bigqueue.IBigQueue;

import net.gini.challenge.dao.DocumentDAOImpl;
import net.gini.challenge.exception.GiniChallengeException;
import net.gini.challenge.model.DocumentIdSerialNumber;

@Service
public class BiqQueueUpdateSerialNumberServiceImpl implements BiqQueueUpdateSerialNumberService{

	private final String dir;

	private IBigQueue bigQueue;
	private int maxResultSize;

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	public BiqQueueUpdateSerialNumberServiceImpl(@Value("${inmemory.max.store}") int maxResultSize,
			@Value("${java.p.queue.update.q.name}") String queueName) throws GiniChallengeException {

		dir = System.getProperty("user.dir");
		this.maxResultSize = maxResultSize;

		try {
			bigQueue = new BigQueueImpl(dir, queueName);
		} catch (IOException e) {
			throw new GiniChallengeException(e.getMessage());
		}
	}

	@Transactional
	private List<DocumentIdSerialNumber> pushBackDataToDatabase() throws GiniChallengeException {
		long count = bigQueue.size();
		List<DocumentIdSerialNumber> list = new ArrayList<>(maxResultSize);

		for (long i = 0; i < count; i++) {

			DocumentIdSerialNumber doc;
			try {
				doc = (DocumentIdSerialNumber) SerializationUtils.deserialize(bigQueue.dequeue());
				list.add(doc);
			} catch (IOException e) {
				throw new GiniChallengeException(e.getMessage());
			}

		}
		return list;

	}
	
	@Override
	public void push(DocumentIdSerialNumber documentIdSerialNum) throws GiniChallengeException {

		byte[] data = SerializationUtils.serialize(documentIdSerialNum);

		try {
			bigQueue.enqueue(data);

			if (bigQueue.size() >= maxResultSize) {
				documentRepoImpl.batchUpdateSerialNumbers(pushBackDataToDatabase());
			}
		} catch (IOException e) {
			throw new GiniChallengeException(e.getMessage());
		}

	}
}
