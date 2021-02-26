package net.gini.challenge.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import net.gini.challenge.listeners.APIResultReceiver;
import net.gini.challenge.model.DocumentIdSerialNumber;
import net.gini.challenge.utils.Constants;

@Service
public class PushDataSetToApiExchangeService {

	private final RabbitTemplate rabbitTemplate;
	private final APIResultReceiver apiResultReceiver;
	private static final Logger LOG = LoggerFactory.getLogger(PushDataSetToApiExchangeService.class);

	public PushDataSetToApiExchangeService(RabbitTemplate rabbitTemplate, APIResultReceiver apiResultReceiver) {
		this.rabbitTemplate = rabbitTemplate;
		this.apiResultReceiver = apiResultReceiver;
	}
	
	public void sendToApiExchange(DocumentIdSerialNumber document) {
		LOG.info("Putting sucess result to API exchange "+document.getId());
		rabbitTemplate.convertAndSend(Constants.API_RESULT_EXCHANGE, Constants.API_RESULT_EXCHANGE_KEY, document);
	}
	

}
