package com.project.bankassetor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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


    // RabbitTemplate에 메시지 컨버터 설정
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // 메시지 컨버터로 Jackson을 사용하여 JSON 변환
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 교환기(Exchange) 선언
    @Bean
    public TopicExchange accessLogExchange() {
        return new TopicExchange("accessLogExchange");
    }

    // 교환기와 큐를 바인딩
    @Bean
    public Binding bindingAccessLogQueue(Queue mainQueue, TopicExchange accessLogExchange) {
        return BindingBuilder.bind(mainQueue).to(accessLogExchange).with("access.log");
    }
}