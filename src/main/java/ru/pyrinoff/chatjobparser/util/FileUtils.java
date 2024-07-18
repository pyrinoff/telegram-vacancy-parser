package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface FileUtils {

    static void filePutContent(@Nullable String filename, @Nullable List<String> content) throws IOException {
        if (filename == null || filename.isEmpty()) throw new RuntimeException("Filename is null or empty!");
        if (content == null) throw new RuntimeException("Filename is null! Should be at least empty!");
        Files.write(Path.of(filename), content);
    }

    static @Nullable List<String> fileGetContent(@Nullable String filename) throws IOException {
        if (filename == null) throw new RuntimeException("Filename is null!");
        @NotNull final Path path = Path.of(filename);
        if (!Files.exists(path)) throw new FileNotFoundException("File not found!");
        return Files.readAllLines(path);
    }

    static @Nullable List<String> fileGetContentFromJar(@Nullable String filename) throws IOException {
        if (filename == null) throw new RuntimeException("Filename is null!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(filename)
                .getInputStream(),
                StandardCharsets.UTF_8));
        List<String> words = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) words.add(line);
        reader.close();
        return words;
    }

    static @Nullable List<String> fileGetContent(@Nullable File file) throws IOException {
        if (file == null) throw new RuntimeException("File null!");
        if (!file.exists()) throw new FileNotFoundException("File not found!");
        return Files.readAllLines(file.toPath());
    }

    static @Nullable String fileGetContentAsString(@Nullable String filename) throws IOException {
        if (filename == null) throw new RuntimeException("Filename is null!");
        @NotNull final Path path = Path.of(filename);
        if (!Files.exists(path)) throw new FileNotFoundException("File not found!");
        return Files.readString(path);
    }

    static @Nullable String getFilenameFromPath(@Nullable final String filepath) {
        if (filepath == null) return null;
        @NotNull final Path path = Path.of(filepath);
        return path.getFileName().toString();
    }

}
