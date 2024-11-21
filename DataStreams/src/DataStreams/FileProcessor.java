package DataStreams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {
    private Path filePath;
    //
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public List<String> readAllLines() throws IOException {
        if (filePath == null) {
            throw new IllegalStateException("File path is not set.");
        }
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.collect(Collectors.toList());
        }
    }

    public List<String> filterLines(String searchString) throws IOException {
        if (filePath == null) {
            throw new IllegalStateException("File path is not set.");
        }
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.filter(line -> line.contains(searchString)).collect(Collectors.toList());
        }
    }
}
