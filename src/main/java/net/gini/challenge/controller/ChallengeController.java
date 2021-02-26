package net.gini.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.gini.challenge.model.DocumentIdPages;
import net.gini.challenge.senders.PushDataSetToPatchOperationService;
import net.gini.challenge.utils.Constants;

@RestController
public class ChallengeController {

	private static final Logger LOG = LoggerFactory.getLogger(ChallengeController.class);

	@Autowired
	PushDataSetToPatchOperationService pushDataSetToPatchQueue;

	/**
	 * This is API which is exposed for patch operations
	 * 
	 * @param documentIdPages which contains document id and pages count which needs to be updated
	 * @return accepted response
	 */
	@RequestMapping(method = RequestMethod.PATCH, value = Constants.PATH)
	public ResponseEntity update(@RequestBody DocumentIdPages document) {
		
		pushDataSetToPatchQueue.sendToPatchExchange(document);
		return new ResponseEntity<>("Patch Request has been accepted",HttpStatus.ACCEPTED);
	}
}
