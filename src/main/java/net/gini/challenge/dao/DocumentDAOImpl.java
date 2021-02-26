package net.gini.challenge.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.gini.challenge.model.Document;
import net.gini.challenge.model.DocumentIdPages;
import net.gini.challenge.model.DocumentIdSerialNumber;
import net.gini.challenge.repo.DocumentRepository;

@Component
public class DocumentDAOImpl implements DocumentDAO {

	@Autowired
	DocumentRepository documentRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;

	private static final Logger LOG = LoggerFactory.getLogger(DocumentDAOImpl.class);

	@Override
	@Transactional
	public void batchUpdateSerialNumbers(List<DocumentIdSerialNumber> list) {

		for (int i = 0; i < list.size(); i++) {

			LOG.info("Batching for id " + list.get(i).getId());

			if (i > 0 && i % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			Document document = findDocumentById(list.get(i).getId());
			document.setSerial_number(list.get(i).getSearialNumber());
			entityManager.persist(document);
		}
	}

	@Override
	public List<Integer> findDocumentBySerialNumbers(int start, int end) {

		String sql = "SELECT d.id from Document d where d.serial_number is null";
		TypedQuery<Integer> query = entityManager.createQuery(sql, Integer.class);
		List<Integer> docIds = query.setFirstResult(start).setMaxResults(end).getResultList();
		return docIds;
	}

	@Override
	public Document findDocumentById(int id) {
		return documentRepo.findById(id).get();
	}

	@Override
	@Transactional
	public void batchPatchOperation(List<DocumentIdPages> list) {

		for (int i = 0; i < list.size(); i++) {

			LOG.info("Batching for id " + list.get(i).getId());

			if (i > 0 && i % batchSize == 0) {
				entityManager.flush();
				entityManager.clear();
			}
			Document document = findDocumentById(list.get(i).getId());
			document.setPages(list.get(i).getPages());
			entityManager.persist(document);
		}
	}

	@Override
	public long getCountOfSerialNumberWithNull() {
		String sql = "SELECT COUNT (d.id) from Document d where d.serial_number is null";
		TypedQuery<Long> query = entityManager.createQuery(sql, Long.class);
		return query.getResultList().get(0);

	}
}
