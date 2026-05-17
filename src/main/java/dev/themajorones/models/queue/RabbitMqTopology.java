package dev.themajorones.models.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;

import dev.themajorones.models.constants.RabbitMqConstant;

public final class RabbitMqTopology {

    private RabbitMqTopology() {
    }

    public static DirectExchange directExchange() {
        return new DirectExchange(RabbitMqConstant.DIRECT_EXCHANGE);
    }

    public static Queue connectionManagerQueue() {
        return QueueBuilder.durable(RabbitMqConstant.Queue.ConnectionManager.NAME).build();
    }

    public static Binding connectionManagerBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(RabbitMqConstant.Queue.ConnectionManager.ROUTING_KEY);
    }
}
