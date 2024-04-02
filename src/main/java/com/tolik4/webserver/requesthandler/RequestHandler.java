package com.tolik4.webserver.requesthandler;

import com.tolik4.webserver.request.HttpMethod;
import com.tolik4.webserver.request.Request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class RequestHandler {

    private BufferedReader socketReader;
    private BufferedWriter socketWriter;
    private String webAppPath;

    public RequestHandler(BufferedReader socketReader, BufferedWriter socketWriter, String webAppPath) {
        this.socketReader = socketReader;
        this.socketWriter = socketWriter;
        this.webAppPath = webAppPath;
    }

    public void handle() throws IOException {
        Request request = new RequestParser().parse(socketReader);
        ResponseWriter responseWriter = new ResponseWriter();
        if (Objects.equals(request.getHttpMethod(), HttpMethod.GET)) {
            handleGetRequest(request, responseWriter);
            return;
        }
        responseWriter.writeBadRequestResponse(socketWriter);
    }

    private void handleGetRequest(Request request, ResponseWriter responseWriter) throws IOException {
        if (Objects.equals(null, request.getUri())) {
            responseWriter.writeBadRequestResponse(socketWriter);
            return;
        }

        String content = new ResourceReader(webAppPath).readResource(request.getUri());

        if (Objects.equals(null, content)) {
            responseWriter.writePageNotFoundResponse(socketWriter);
            return;
        }

        responseWriter.writeSuccessResponse(content, socketWriter);
    }
}
