package science.atlarge.opencraft.opencraft.util.config;

public enum BrokerType {

    ACTIVEMQ("activemq"),
    CONCURRENT("concurrent"),
    RABBITMQ("rabbitmq"),
    READ_WRITE("read-write"),
    UNSAFE("unsafe");

    public static BrokerType parse(String name) {
        for (BrokerType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown broker: " + name);
    }

    private final String name;

    BrokerType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
