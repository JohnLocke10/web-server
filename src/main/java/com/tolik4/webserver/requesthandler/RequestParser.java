package com.tolik4.webserver.requesthandler;

import com.tolik4.webserver.request.HttpMethod;
import com.tolik4.webserver.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestParser {
    public Request parse(BufferedReader reader) throws IOException {
        Request request = new Request();
        String requestLine = reader.readLine();
        if (isFirstRequestLineValid(requestLine)) {
            injectUriAndHttpMethod(requestLine, request);
            injectHeaders(reader, request);
        }
        return request;
    }

    private void injectUriAndHttpMethod(String firstRequestLine, Request request) {
        String[] firstLineParams = firstRequestLine.split("\\s+");
        request.setHttpMethod(findRequestHttpMethod(firstLineParams[0]));
        String uri = firstLineParams[1];
        int indexOfQuestionMark = uri.indexOf("?");
        if (indexOfQuestionMark == -1) {
            request.setUri(uri);
        } else {
            request.setUri(uri.substring(0, indexOfQuestionMark));
        }
    }

    private void injectHeaders(BufferedReader reader, Request request) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        int indexOfSeparator;

        while (!(headerLine = reader.readLine()).isEmpty()) {
            indexOfSeparator = headerLine.indexOf(":");
            String key = headerLine.substring(0, indexOfSeparator).trim();
            String value = headerLine.substring(indexOfSeparator + 1).trim();
            headers.put(key, value);
        }
        request.setHeaders(headers);
    }

    private HttpMethod findRequestHttpMethod(String methodFromRequest) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (Objects.equals(methodFromRequest, httpMethod.name())) {
                return httpMethod;
            }
        }
        return null;
    }

    private boolean isFirstRequestLineValid(String requestLine) {
        if (requestLine == null) {
            return false;
        }
        String[] firstLineParams = requestLine.split("\\s+");

        return findRequestHttpMethod(firstLineParams[0]) != null &&
                (firstLineParams.length == 2 || firstLineParams.length == 3);
    }
}
