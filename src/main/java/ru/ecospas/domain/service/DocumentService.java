package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.web.dto.response.document.DocumentResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private static final Path ROOT =
            Path.of(System.getProperty("user.home"), "documents");

    public List<DocumentResponse> findAll() throws IOException {
        if (!Files.exists(ROOT)) {
            return List.of();
        }

        try (Stream<Path> organizations = Files.list(ROOT)) {
            return organizations
                    .filter(Files::isDirectory)
                    .flatMap(this::documents)
                    .sorted(Comparator
                            .comparing(DocumentResponse::organizationName)
                            .thenComparing(DocumentResponse::fileName))
                    .toList();
        }
    }

    private Stream<DocumentResponse> documents(Path organizationDir) {
        try (Stream<Path> files = Files.list(organizationDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".docx"))
                    .map(path -> {
                        try {
                            return new DocumentResponse(
                                    organizationDir.getFileName().toString(),
                                    path.getFileName().toString(),
                                    Files.size(path)
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList()
                    .stream();
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    public Path getDocument(String organization, String fileName) {
        Path file = ROOT.resolve(organization).resolve(fileName);
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Документ не найден");
        }
        return file;
    }

    public void delete(String organization, String fileName) throws IOException {
        Path file = getDocument(organization, fileName);
        Files.delete(file);
    }
}