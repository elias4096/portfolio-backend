package com.eliasdetlefsen.portfolio_backend.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class ImageService {
    private static final Path STORAGE_PATH = Paths.get("storage");

    public ResponseEntity<String> createImage(HttpHeaders headers, byte[] body) {
        MediaType contentType = headers.getContentType();

        if (contentType == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        if (!MimeTypeUtils.IMAGE_PNG.equalsTypeAndSubtype(contentType)) {
            return ResponseEntity
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .build();
        }

        UUID id = UUID.randomUUID();

        try {
            Files.createDirectories(STORAGE_PATH);
            Path imagePath = STORAGE_PATH.resolve(id + ".png");
            Files.write(imagePath, body, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(id.toString());
    }

    public ResponseEntity<byte[]> readImage(UUID id) {
        Path imagePath = STORAGE_PATH.resolve(id + ".png");

        if (!Files.exists(imagePath)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        try {
            byte[] bytes = Files.readAllBytes(imagePath);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ResponseEntity<Void> deleteImage(UUID id) {
        Path imagePath = STORAGE_PATH.resolve(id + ".png");

        if (!Files.exists(imagePath)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        try {
            Files.delete(imagePath);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
