package science.atlarge.opencraft.opencraft.util.config;

public enum ChannelType {

    CONCURRENT("concurrent"),
    GUAVA("guava"),
    READ_WRITE("read-write"),
    UNSAFE("unsafe");

    public static ChannelType parse(String name) {
        for (ChannelType type : values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown broker: " + name);
    }

    private final String name;

    ChannelType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
