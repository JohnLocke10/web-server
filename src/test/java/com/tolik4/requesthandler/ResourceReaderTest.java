package com.tolik4.requesthandler;

import com.tolik4.webserver.requesthandler.ResourceReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceReaderTest {

    public static final String TEST_WEB_APP_PATH = "src/test/resources/";
    public static final String TMP_FOLDER_PATH = "src/test/resources/ResourceReaderTestFolder";
    public static final String TMP_FILE_PATH = "src/test/resources/ResourceReaderTestFolder/testIndex.html";
    static File TMPFolder = new File(TMP_FOLDER_PATH);

    static String fileContent = "<body>\n" +
            "    <h1>HelloWorld</h1>\n" +
            "</body>";

    @BeforeAll
    public static void setUpResources() throws IOException {
        assertTrue(TMPFolder.mkdirs());
        File firstFile = new File(TMP_FILE_PATH);
        if (!firstFile.exists()) {
            assertTrue(firstFile.createNewFile());
            try (FileOutputStream fileOutputStream = new FileOutputStream(firstFile)) {
                fileOutputStream.write((fileContent).getBytes());
            }
        }
    }

    @Test
    @DisplayName("Check reading content from existing non empty file")
    public void checkReadingContentFromExistingNonEmptyFile() throws IOException {
        String actualContent =
                new ResourceReader(TEST_WEB_APP_PATH).readResource("/ResourceReaderTestFolder/testIndex.html");
        assertEquals(fileContent, actualContent);
    }

    @Test
    @DisplayName("Check return null for non existing file")
    public void checkReturnNullForNonExistingFile() throws IOException {
        String actualContent =
                new ResourceReader(TEST_WEB_APP_PATH).readResource("/ResourceReaderTestFolder/NonExistingFile.html");
        assertNull(actualContent);
    }


    @AfterAll
    public static void clearFileTrees() throws IOException {
        assertTrue(Files.deleteIfExists(Path.of(TMP_FILE_PATH)));
        assertTrue(Files.deleteIfExists(Path.of(TMP_FOLDER_PATH)));
        assertFalse(new File(TMP_FOLDER_PATH).exists());
    }

}