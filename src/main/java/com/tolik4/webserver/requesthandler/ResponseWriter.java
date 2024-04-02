package com.tolik4.webserver.requesthandler;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseWriter {
    public static final String STATUS_200_OK = "HTTP/1.1 200 OK";
    public static final String STATUS_400_BAD_REQUEST = "HTTP/1.1 400 Bad Request";
    public static final String STATUS_404_NOT_FOUND = "HTTP/1.1 404 Not Found";

    public void writeSuccessResponse(String content, BufferedWriter writer) throws IOException {
        writer.write(STATUS_200_OK);
        writer.newLine();
        writer.newLine();
        writer.write(content);
        writer.flush();
    }

    public void writePageNotFoundResponse(BufferedWriter writer) throws IOException {
        writeUnsuccessfulResponse(writer, STATUS_404_NOT_FOUND);
    }

    public void writeBadRequestResponse(BufferedWriter writer) throws IOException {
        writeUnsuccessfulResponse(writer, STATUS_400_BAD_REQUEST);
    }

    private void writeUnsuccessfulResponse(BufferedWriter writer, String httpStatus) throws IOException {
        writer.write(httpStatus);
        writer.newLine();
        writer.newLine();
        writer.flush();
    }
}
