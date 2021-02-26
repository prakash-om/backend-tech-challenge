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
import net.gini.challenge.model.DocumentIdPages;

@Service
public class BiqQueuePatchServiceImpl implements BiqQueuePatchService {

	private final String dir;

	private IBigQueue bigQueue;
	private int maxResultSize;

	@Autowired
	DocumentDAOImpl documentRepoImpl;

	@Autowired
	public BiqQueuePatchServiceImpl(@Value("${inmemory.max.store}") int maxResultSize,
			@Value("${java.p.queue.patch.q.name}") String queueName) throws GiniChallengeException {

		dir = System.getProperty("user.dir");
		try {
			bigQueue = new BigQueueImpl(dir, queueName);
			this.maxResultSize = maxResultSize;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new GiniChallengeException(e.getMessage());
		}
	}

	@Override
	public void pushDataToBigQueue(DocumentIdPages document) throws GiniChallengeException {
		// TODO Auto-generated method stub

		byte[] data = SerializationUtils.serialize(document);

		try {
			bigQueue.enqueue(data);

			if (bigQueue.size() >= maxResultSize) {

				documentRepoImpl.batchPatchOperation(getDataFromBiqQueue());
			}
		} catch (IOException e) {
			throw new GiniChallengeException(e.getMessage());
		}

	}

	@Transactional
	private List<DocumentIdPages> getDataFromBiqQueue() throws GiniChallengeException {
		long count = bigQueue.size();
		List<DocumentIdPages> list = new ArrayList<DocumentIdPages>(maxResultSize);

		for (long i = 0; i < count; i++) {

			DocumentIdPages doc;
			try {
				doc = (DocumentIdPages) SerializationUtils.deserialize(bigQueue.dequeue());
				list.add(doc);
			} catch (IOException e) {
				throw new GiniChallengeException(e.getMessage());
			}

		}
		return list;

	}
}
