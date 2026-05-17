package dev.themajorones.models.constants;

public interface RabbitMqConstant {

    public static final String DIRECT_EXCHANGE = "amq.direct";

    public interface Queue {
        public interface ConnectionManager {
            public static final String NAME = "connection-manager";
            public static final String ROUTING_KEY = "connection-manager.create-android-vm";
        }
    }
}
