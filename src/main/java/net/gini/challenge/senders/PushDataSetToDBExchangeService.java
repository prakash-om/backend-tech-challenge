package net.gini.challenge.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import net.gini.challenge.listeners.DBResultReceiver;
import net.gini.challenge.utils.Constants;

@Service
public class PushDataSetToDBExchangeService {

	private final RabbitTemplate rabbitTemplate;
	private final DBResultReceiver dbResultreceiver;
	private static final Logger LOG = LoggerFactory.getLogger(PushDataSetToDBExchangeService.class);

	public PushDataSetToDBExchangeService(RabbitTemplate rabbitTemplate, DBResultReceiver dbResultreceiver) {
		this.rabbitTemplate = rabbitTemplate;
		this.dbResultreceiver = dbResultreceiver;
	}

	public void sendToDBExchange(Integer id) {
		rabbitTemplate.convertAndSend(Constants.DB_RESULT_EXCHANGE, Constants.DB_RESULT_EXCHANGE_KEY, id);
	}

}
