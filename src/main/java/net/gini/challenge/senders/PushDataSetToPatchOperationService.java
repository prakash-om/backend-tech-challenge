package net.gini.challenge.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import net.gini.challenge.listeners.APIPatchUpdateRequestReceiver;
import net.gini.challenge.model.DocumentIdPages;
import net.gini.challenge.utils.Constants;

@Service
public class PushDataSetToPatchOperationService {

	private final RabbitTemplate rabbitTemplate;
	private final APIPatchUpdateRequestReceiver apiPatchUpdateReceiver;
	
	private static final Logger LOG = LoggerFactory.getLogger(PushDataSetToPatchOperationService.class);

	public PushDataSetToPatchOperationService(RabbitTemplate rabbitTemplate, APIPatchUpdateRequestReceiver apiPatchUpdateReceiver) {
		this.rabbitTemplate = rabbitTemplate;
		this.apiPatchUpdateReceiver = apiPatchUpdateReceiver;
	}

	public void sendToPatchExchange(DocumentIdPages document) {
		rabbitTemplate.convertAndSend(Constants.API_PATCH_RESULT_EXCHANGE, Constants.API_PATCH_RESULT_EXCHANGE_KEY, document);
	}
}
