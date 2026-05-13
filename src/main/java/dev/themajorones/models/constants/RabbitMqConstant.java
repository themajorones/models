package dev.themajorones.models.constants;

public final class RabbitMqConstant {

    public static final String DIRECT_EXCHANGE = "amq.direct";

    private RabbitMqConstant() {
    }

    public static final class Queue {

        public static final class Message {
            public static final String NAME = "autotest.messages";

            private Message() {
            }
        }

        private Queue() {
        }
    }

    public static final class RoutingKey {

        public static final class Message {
            public static final String NAME = "autotest.message";

            private Message() {
            }
        }

        private RoutingKey() {
        }
    }
}
