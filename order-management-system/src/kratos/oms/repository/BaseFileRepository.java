package kratos.oms.repository;

import kratos.oms.domain.Domain;
import kratos.oms.seedwork.Helpers;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseFileRepository<TId, T extends Domain<TId>> {
    private final String fileUrl;

    public BaseFileRepository(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public List<T> read() throws IOException {
        Path path = Paths.get(fileUrl);
        List<String> lines = Files.readAllLines(path);
        return lines.stream()
                .filter(line -> !Helpers.isNullOrEmpty(line))
                .map(line -> (T) T.deserialize(line)).collect(Collectors.toList());
    }

    public void write(List<T> records) throws IOException {
        List<String> lines = records.stream()
                .map(Domain::serialize)
                .collect(Collectors.toList());
        String data = String.join("\n", lines);
        Path path = Paths.get(fileUrl);
        Files.write(path, data.getBytes(), StandardOpenOption.WRITE);
    }
}
