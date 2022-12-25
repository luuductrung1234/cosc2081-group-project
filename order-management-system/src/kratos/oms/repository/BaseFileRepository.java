package kratos.oms.repository;

import kratos.oms.domain.Domain;
import kratos.oms.seedwork.Helpers;
import kratos.oms.seedwork.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BaseFileRepository<TId, T extends Domain<TId>> {
    private final String fileUrl;

    public BaseFileRepository(String directoryUrl, String fileName) {
        this.fileUrl = directoryUrl + File.separator + fileName;
    }

    public List<T> read(Class<T> clazz) throws IOException {
        Path path = Paths.get(fileUrl);
        if (!Files.isReadable(path))
            return new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        return lines.stream()
                .filter(line -> !Helpers.isNullOrEmpty(line))
                .map(line -> lineDeserialize(line, clazz))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private T lineDeserialize(String line, Class<T> clazz) {
        try {
            Method deserialize = clazz.getMethod("deserialize", String.class);
            return (T) deserialize.invoke(null, line);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.printError(this.getClass().getName(), "lineDeserialize", e);
            return null;
        }
    }

    public void write(List<T> records) throws IOException {
        List<String> lines = records.stream()
                .map(Domain::serialize)
                .collect(Collectors.toList());
        String data = String.join("\n", lines);
        Path path = Paths.get(fileUrl);
        Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }
}
