package com.tolik4.requesthandler;

import com.tolik4.webserver.requesthandler.ResponseWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceWriterTest {

    public static final String TMP_FOLDER_PATH = "src/test/resources/ResourceWriterTestFolder";
    public static final String TMP_FILE_PATH_200 = "src/test/resources/ResourceWriterTestFolder/testWriterIndex200.html";
    public static final String TMP_FILE_PATH_404 = "src/test/resources/ResourceWriterTestFolder/testWriterIndex404.html";
    public static final String TMP_FILE_PATH_400 = "src/test/resources/ResourceWriterTestFolder/testWriterIndex400.html";
    static File TMPFolder = new File(TMP_FOLDER_PATH);

    static String fileContent = "<body>\n" +
            "    <h1>HelloWorldFromWriter</h1>\n" +
            "</body>";

    @BeforeAll
    public static void setUpResources() {
        assertTrue(TMPFolder.mkdirs());
    }

    @Test
    @DisplayName("Check written content after write success response method")
    public void checkWrittenContentAfterWriteSuccessResponseMethod() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(TMP_FILE_PATH_200)));) {

            ResponseWriter responseWriter = new ResponseWriter();
            responseWriter.writeSuccessResponse(fileContent, bufferedWriter);
        }
        String actualContent = new String(Files.readAllBytes(Path.of(TMP_FILE_PATH_200)));
        assertEquals("HTTP/1.1 200 OK" + System.lineSeparator() + System.lineSeparator() + fileContent,
                actualContent);
    }

    @Test
    @DisplayName("Check response after write page not found response method")
    public void checkResponseAfterWritePageNotFoundResponseMethod() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(TMP_FILE_PATH_404)));) {

            ResponseWriter responseWriter = new ResponseWriter();
            responseWriter.writePageNotFoundResponse(bufferedWriter);
        }
        String actualContent = new String(Files.readAllBytes(Path.of(TMP_FILE_PATH_404)));
        assertEquals("HTTP/1.1 404 Not Found" + System.lineSeparator() + System.lineSeparator(),
                actualContent);
    }

    @Test
    @DisplayName("Check response after write bad request response method")
    public void checkResponseAfterWriteBadRequestResponseMethod() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(TMP_FILE_PATH_400)));) {

            ResponseWriter responseWriter = new ResponseWriter();
            responseWriter.writeBadRequestResponse(bufferedWriter);
        }
        String actualContent = new String(Files.readAllBytes(Path.of(TMP_FILE_PATH_400)));
        assertEquals("HTTP/1.1 400 Bad Request" + System.lineSeparator() + System.lineSeparator(),
                actualContent);
    }


    @AfterAll
    public static void clearFileTrees() throws IOException {
        assertTrue(Files.deleteIfExists(Path.of(TMP_FILE_PATH_200)));
        assertTrue(Files.deleteIfExists(Path.of(TMP_FILE_PATH_404)));
        assertTrue(Files.deleteIfExists(Path.of(TMP_FILE_PATH_400)));
        assertTrue(Files.deleteIfExists(Path.of(TMP_FOLDER_PATH)));
        assertFalse(new File(TMP_FOLDER_PATH).exists());
    }
}