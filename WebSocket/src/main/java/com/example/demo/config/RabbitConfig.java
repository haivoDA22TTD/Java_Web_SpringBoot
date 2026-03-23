package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LESSON 9: RabbitMQ Configuration
 * Cấu hình RabbitMQ cho message queues
 */
@Configuration
@EnableRabbit
public class RabbitConfig {
    
    // Queue Names
    public static final String STUDENT_CREATED_QUEUE = "student.created";
    public static final String STUDENT_UPDATED_QUEUE = "student.updated";
    public static final String STUDENT_DELETED_QUEUE = "student.deleted";
    public static final String EMAIL_NOTIFICATION_QUEUE = "email.notification";
    public static final String AUDIT_LOG_QUEUE = "audit.log";
    
    // Exchange Names
    public static final String STUDENT_EXCHANGE = "student.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    
    // Routing Keys
    public static final String STUDENT_CREATED_ROUTING_KEY = "student.created";
    public static final String STUDENT_UPDATED_ROUTING_KEY = "student.updated";
    public static final String STUDENT_DELETED_ROUTING_KEY = "student.deleted";
    public static final String EMAIL_SEND_ROUTING_KEY = "email.send";
    public static final String AUDIT_LOG_ROUTING_KEY = "audit.log";
    
    // Exchanges
    @Bean
    public TopicExchange studentExchange() {
        return new TopicExchange(STUDENT_EXCHANGE);
    }
    
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }
    
    // Queues
    @Bean
    public Queue studentCreatedQueue() {
        return QueueBuilder.durable(STUDENT_CREATED_QUEUE).build();
    }
    
    @Bean
    public Queue studentUpdatedQueue() {
        return QueueBuilder.durable(STUDENT_UPDATED_QUEUE).build();
    }
    
    @Bean
    public Queue studentDeletedQueue() {
        return QueueBuilder.durable(STUDENT_DELETED_QUEUE).build();
    }
    
    @Bean
    public Queue emailNotificationQueue() {
        return QueueBuilder.durable(EMAIL_NOTIFICATION_QUEUE).build();
    }
    
    @Bean
    public Queue auditLogQueue() {
        return QueueBuilder.durable(AUDIT_LOG_QUEUE).build();
    }
    
    // Bindings
    @Bean
    public Binding studentCreatedBinding() {
        return BindingBuilder
            .bind(studentCreatedQueue())
            .to(studentExchange())
            .with(STUDENT_CREATED_ROUTING_KEY);
    }
    
    @Bean
    public Binding studentUpdatedBinding() {
        return BindingBuilder
            .bind(studentUpdatedQueue())
            .to(studentExchange())
            .with(STUDENT_UPDATED_ROUTING_KEY);
    }
    
    @Bean
    public Binding studentDeletedBinding() {
        return BindingBuilder
            .bind(studentDeletedQueue())
            .to(studentExchange())
            .with(STUDENT_DELETED_ROUTING_KEY);
    }
    
    @Bean
    public Binding emailNotificationBinding() {
        return BindingBuilder
            .bind(emailNotificationQueue())
            .to(notificationExchange())
            .with(EMAIL_SEND_ROUTING_KEY);
    }
    
    @Bean
    public Binding auditLogBinding() {
        return BindingBuilder
            .bind(auditLogQueue())
            .to(studentExchange())
            .with(AUDIT_LOG_ROUTING_KEY);
    }
    
    // RabbitTemplate with JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
    
    // Message converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}