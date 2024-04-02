package com.tolik4.webserver.requesthandler;

import com.tolik4.webserver.exceptions.PageNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceReader {
    String webAppPath;

    public ResourceReader(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public String readResource(String uri) throws IOException {
        File file = new File(webAppPath, uri);
        if (file.exists()) {
            return new String(Files.readAllBytes(Path.of(file.getPath())));
        } else {
            throw new PageNotFoundException("Page not found!");
        }
    }
}
