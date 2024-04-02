package com.tolik4.webserver.requesthandler;

import com.tolik4.webserver.exceptions.BadRequestException;
import com.tolik4.webserver.exceptions.PageNotFoundException;
import com.tolik4.webserver.request.HttpMethod;
import com.tolik4.webserver.request.Request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

public class RequestHandler {

    private BufferedReader socketReader;
    private BufferedWriter socketWriter;
    private ResourceReader resourceReader;

    public RequestHandler(BufferedReader socketReader, BufferedWriter socketWriter, ResourceReader resourceReader) {
        this.socketReader = socketReader;
        this.socketWriter = socketWriter;
        this.resourceReader = resourceReader;
    }

    public void handle() throws IOException {
        ResponseWriter responseWriter = new ResponseWriter();
        try {
            Request request = new RequestParser().parse(socketReader);
            if (Objects.equals(request.getHttpMethod(), HttpMethod.GET)) {
                String content = resourceReader.readResource(request.getUri());
                responseWriter.writeSuccessResponse(content, socketWriter);
            }
        } catch (BadRequestException e) {
            responseWriter.writeBadRequestResponse(socketWriter);
        } catch (PageNotFoundException e) {
            responseWriter.writePageNotFoundResponse(socketWriter);
        }
    }

}
