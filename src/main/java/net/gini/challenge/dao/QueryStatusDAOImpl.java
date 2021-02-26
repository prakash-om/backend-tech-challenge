package net.gini.challenge.dao;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.gini.challenge.model.QueryOffsetStatus;
import net.gini.challenge.repo.QueryStatusRepository;

@Service
public class QueryStatusDAOImpl implements QueryStatusDAO {

	@Autowired
	QueryStatusRepository queryStatusRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${db.max.rows}")
	private int maxRows;

	@Transactional
	public int getQueryStatus() {

		String sql = "SELECT q from QueryOffsetStatus q";
		TypedQuery<QueryOffsetStatus> query = entityManager.createQuery(sql, QueryOffsetStatus.class);
		QueryOffsetStatus queryOffset = query.getSingleResult();
		int offset = queryOffset.getStartIndex();
		queryOffset.setStartIndex(offset + maxRows);
		queryStatusRepo.save(queryOffset);
		return offset;

	}

	@Transactional
	public void reset() {
		String sql = "SELECT q from QueryOffsetStatus q";
		TypedQuery<QueryOffsetStatus> query = entityManager.createQuery(sql, QueryOffsetStatus.class);
		QueryOffsetStatus queryOffset = query.getResultList().get(0);
		queryOffset.setStartIndex(0);
		queryStatusRepo.save(queryOffset);
	}

}
