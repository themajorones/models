package dev.themajorones.models.constants;

public interface RabbitMqConstant {

    public static final String DIRECT_EXCHANGE = "amq.direct";

    public interface Queue {
        public interface Test {
            public static final String NAME = "test";
            public static final String ROUTING_KEY = "test_key";
        }
    }
}
