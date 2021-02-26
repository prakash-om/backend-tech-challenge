package net.gini.challenge.repo;

import org.springframework.data.repository.CrudRepository;

import net.gini.challenge.model.Document;

public interface DocumentRepository extends CrudRepository<Document, Integer>{

}
