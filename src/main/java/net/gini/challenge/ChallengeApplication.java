package net.gini.challenge;

import java.time.Duration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import net.gini.challenge.listeners.APIPatchUpdateRequestReceiver;
import net.gini.challenge.listeners.APIResultReceiver;
import net.gini.challenge.listeners.DBResultReceiver;
import net.gini.challenge.utils.Constants;

@SpringBootApplication
@EnableScheduling
public class ChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Bean
	Queue queuedbResult() {
		return new Queue(Constants.DB_RESULT_QUEUE, false);
	}

	@Bean
	TopicExchange exchangedbExchange() {
		return new TopicExchange(Constants.DB_RESULT_EXCHANGE);
	}

	@Bean
	Queue queueapiResult() {

		return new Queue(Constants.API_RESULT_QUEUE, false);
	}

	@Bean
	TopicExchange exchangeapiExchange() {
		return new TopicExchange(Constants.API_RESULT_EXCHANGE);
	}
	
	@Bean
	Queue queuePatchApiResult() {

		return new Queue(Constants.API_PATCH_RESULT_QUEUE, false);
	}

	@Bean
	TopicExchange exchangePatchApiExchange() {
		return new TopicExchange(Constants.API_PATCH_RESULT_EXCHANGE);
	}

	@Bean
	SimpleMessageListenerContainer containerdbResult(ConnectionFactory connectionFactory,
			MessageListenerAdapter dbListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Constants.DB_RESULT_QUEUE);
		container.setMessageListener(dbListenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter dbListenerAdapter(DBResultReceiver receiver) {
		return new MessageListenerAdapter(receiver, Constants.RECIEVE_MESSAGE);
	}

	@Bean
	SimpleMessageListenerContainer containerapiResult(ConnectionFactory connectionFactory,
			MessageListenerAdapter apiListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Constants.API_RESULT_QUEUE);
		container.setMessageListener(apiListenerAdapter);
		return container;
	}
	
	@Bean
	MessageListenerAdapter apiListenerAdapter(APIResultReceiver receiver) {
		return new MessageListenerAdapter(receiver, Constants.RECIEVE_MESSAGE);
	}
	
	
	@Bean
	SimpleMessageListenerContainer containerapiPatchUpdate(ConnectionFactory connectionFactory,
			MessageListenerAdapter apiPatchListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(Constants.API_PATCH_RESULT_QUEUE);
		container.setMessageListener(apiPatchListenerAdapter);
		container.setMaxConcurrentConsumers(50);
		return container;
	}
	
	@Bean
	MessageListenerAdapter apiPatchListenerAdapter(APIPatchUpdateRequestReceiver receiver) {
		return new MessageListenerAdapter(receiver, Constants.RECIEVE_MESSAGE);
	}

	
	@Bean
	Binding bindingdbResult(Queue queuedbResult, TopicExchange exchangedbExchange) {
		return BindingBuilder.bind(queuedbResult).to(exchangedbExchange).with(Constants.DB_RESULT_EXCHANGE_HASH);
	}

	@Bean
	Binding bindingapiResult(Queue queueapiResult, TopicExchange exchangeapiExchange) {
		return BindingBuilder.bind(queueapiResult).to(exchangeapiExchange).with(Constants.API_RESULT_EXCHANGE_HASH);
	}
	
	@Bean
	Binding bindingapiPatchRequest(Queue queuePatchApiResult, TopicExchange exchangePatchApiExchange) {
		return BindingBuilder.bind(queuePatchApiResult).to(exchangePatchApiExchange).with(Constants.API_PATCH_RESULT_EXCHANGE_HASH);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(5))
				.build();
	}

}
