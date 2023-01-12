package ru.pyrinoff.chatjobparser.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
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
