package com.symbo.assignment.service;

import com.symbo.assignment.model.exchange.AccountOpenedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

    private RabbitTemplate rabbitTemplate;

    private String accountExchange;

    private String routingKey;

    @Autowired
    public EventDispatcher(final RabbitTemplate rabbitTemplate, @Value("${account.exchange}") final String exchangeName, @Value("${account.opened.routingKey}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.accountExchange = exchangeName;
        this.routingKey = routingKey;
    }

    public void send(final AccountOpenedEvent accountOpenedEvent) {
        rabbitTemplate.convertAndSend(accountExchange,routingKey,accountOpenedEvent);
    }

}
