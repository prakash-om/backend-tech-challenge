package net.gini.challenge.dao;

import java.util.List;

import net.gini.challenge.model.Document;
import net.gini.challenge.model.DocumentIdPages;
import net.gini.challenge.model.DocumentIdSerialNumber;

public interface DocumentDAO {

	public void batchUpdateSerialNumbers(List<DocumentIdSerialNumber> list);

	public List<Integer> findDocumentBySerialNumbers(int start, int end);

	public Document findDocumentById(int id);

	public void batchPatchOperation(List<DocumentIdPages> list);
	
	public long getCountOfSerialNumberWithNull();
}
