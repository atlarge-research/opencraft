package net.glowstone.util.config.messaging;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

public final class Yaml {

    private static final ObjectMapper mapper;

    static {

        YAMLFactory factory = new YAMLFactory();
        factory.disable(WRITE_DOC_START_MARKER);

        mapper = new ObjectMapper(factory);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static <Value> void store(String filename, Value value) throws IOException {

        Path path = Paths.get(filename);

        if (Files.notExists(path)) {
            if (path.getParent() != null) {
                Files.createDirectories(path);
            }
            Files.createFile(path);
        }

        try (BufferedWriter output = Files.newBufferedWriter(path)) {
            mapper.writeValue(output, value);
        }
    }

    public static <Value> Value load(String filename, Class<Value> type) throws IOException {
        Path path = Paths.get(filename);
        try (BufferedReader input = Files.newBufferedReader(path)) {
            return mapper.readValue(input, type);
        }
    }

    public static <Value> Value loadOrCreate(String filename, Class<Value> type, Supplier<Value> supplier) {
        Path path = Paths.get(filename);
        try (BufferedReader input = Files.newBufferedReader(path)) {
            return mapper.readValue(input, type);
        } catch (IOException loadException) {
            Value value = supplier.get();
            try {
                store(filename, value);
            } catch (IOException storeException) {
                storeException.printStackTrace();
            }
            return value;
        }
    }

    private Yaml() {}
}
