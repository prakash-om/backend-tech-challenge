package net.gini.challenge.service;

import java.util.List;

public interface DocumentRetriveService {

	/**
	 * This method will retrieve the documents whose serial number are null also
	 * limit the query to start and max result
	 */
	List<Integer> retriveDocumentsWithNullSerialNumber(int start);

	void pushMessagesToQueue(List<Integer> docIds);

	long getCountOfNullRecords();

}
