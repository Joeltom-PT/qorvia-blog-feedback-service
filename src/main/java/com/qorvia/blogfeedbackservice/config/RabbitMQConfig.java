package com.qorvia.blogfeedbackservice.config;

import com.qorvia.blogfeedbackservice.utils.AppConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // --- Event Management Service RPC Queue, Exchange, and Routing ---

    @Bean
    public Queue eventManagementServiceRpcQueue() {
        return new Queue(AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_QUEUE, true);
    }

    @Bean
    public Exchange eventManagementServiceRpcExchange() {
        return new DirectExchange(AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_EXCHANGE, true, false);
    }

    @Bean
    public Binding eventManagementServiceRpcBinding() {
        return BindingBuilder
                .bind(eventManagementServiceRpcQueue())
                .to(eventManagementServiceRpcExchange())
                .with(AppConstants.EVENT_MANAGEMENT_SERVICE_RPC_ROUTING_KEY)
                .noargs();
    }


    // Configure the RPC Listener Container for the RPC queues
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        return factory;
    }
}