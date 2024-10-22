package dev.lpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class Main {
  
  public static void main(String[] args) {
    
    Path path = Path.of("public");
    Path filePath = Path.of("index.txt");
    Path copyPath = Path.of("indexCopy.txt");
    
    try {
      recurseCopyFile(path, filePath, copyPath);
      recurseCreateFile(path, filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void recurseCopyFile(Path dir, Path filePath, Path copyPath) throws IOException {
    
    if (Files.isDirectory(dir)) {
      Path filePathFull = dir.resolve(filePath);
      Path copyPathFull = dir.resolve(copyPath);
      if (Files.exists(filePathFull)) {
        Files.copy(filePathFull, copyPathFull, StandardCopyOption.REPLACE_EXISTING);
      }
      
      try (var stream = Files.newDirectoryStream(dir)) {
        for (var p : stream) {
          recurseCopyFile(p, filePath, copyPath);
        }
      }
    }
  }
  
  private static void recurseCreateFile(Path dir, Path filePath) throws IOException {
    
    if (Files.isDirectory(dir)) {
      Path filePathFull = dir.resolve(filePath);
      Files.deleteIfExists(filePathFull);
      Files.createFile(filePathFull);
      
      try (var stream = Files.newDirectoryStream(dir)) {
        for (var p : stream) {
          recurseCreateFile(p, filePath);
          Files.writeString(filePathFull, p.toString() + "\n", StandardOpenOption.APPEND);
          Path fullPath = p.resolve(filePath);
          if (Files.isDirectory(p) && Files.exists(fullPath)) {
            Files.writeString(filePathFull, Files.readString(fullPath), StandardOpenOption.APPEND);
          }
        }
      }
    }
  }
}
