package net.gini.challenge.repo;

import org.springframework.data.repository.CrudRepository;

import net.gini.challenge.model.QueryOffsetStatus;

public interface QueryStatusRepository extends CrudRepository<QueryOffsetStatus, Integer>{

}
