package com.project.bankassetor.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // AccessLogQueue 정의 및 Dead Letter 설정
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable("accessLogQueue")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetterQueue")
                .build();
    }

    // AccessLogQueue 정의 및 Dead Letter 설정
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("deadLetterQueue").build();
    }

    // Dead Letter Exchange 정의
    @Bean
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange("deadLetterExchange").durable(true).build();
    }

    // Dead Letter Queue와 Dead Letter Exchange 바인딩
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("deadLetterQueue")
                .noargs();
    }
}