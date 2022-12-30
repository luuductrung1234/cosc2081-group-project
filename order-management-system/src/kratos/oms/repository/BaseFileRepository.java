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

public abstract class BaseFileRepository {
    private final String directoryUrl;

    public BaseFileRepository(String directoryUrl) {
        this.directoryUrl = directoryUrl;
    }

    /**
     * Read a list of domain objects (of type TDomain) from data file
     *
     * @param fileName data file name
     * @param clazz Class object of type TDomain
     * @return a list of domain objects (of type TDomain)
     * @throws IOException data file could not be read
     */
    public <TDomain extends Domain<?>> List<TDomain> read(String fileName, Class<TDomain> clazz) throws IOException {
        Path path = Paths.get(this.directoryUrl + File.separator + fileName);
        if (!Files.isReadable(path))
            return new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        return lines.stream()
                .filter(line -> !Helpers.isNullOrEmpty(line))
                .map(line -> lineDeserialize(line, clazz))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Write a list of domain objects (of type TDomain) to data file
     *
     * @param fileName data file name
     * @param records a list of domain objects (of type TDomain)
     * @throws IOException data file could not be written
     */
    public <TDomain extends Domain<?>> void write(String fileName, List<TDomain> records) throws IOException {
        List<String> lines = records.stream()
                .map(Domain::serialize)
                .collect(Collectors.toList());
        String data = String.join("\n", lines);
        Path path = Paths.get(this.directoryUrl + File.separator + fileName);
        if(Helpers.isNullOrEmpty(data) && Files.exists(path)) {
            Files.delete(path);
            return;
        }
        Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    /**
     * Deserialize given line of text into an instance of clazz
     *
     * @param line text of serialized data
     * @param clazz type of instance after deserialized
     * @return an instance of clazz
     * @param <TDomain> type of instance after deserialized
     */
    @SuppressWarnings("unchecked")
    private <TDomain extends Domain<?>> TDomain lineDeserialize(String line, Class<TDomain> clazz) {
        try {
            Method deserialize = clazz.getMethod("deserialize", String.class);
            return (TDomain) deserialize.invoke(null, line);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.printError(this.getClass().getName(), "lineDeserialize", e);
            return null;
        }
    }
}
