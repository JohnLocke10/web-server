package com.tolik4.webserver;

import com.tolik4.webserver.request.Request;
import com.tolik4.webserver.requesthandler.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestParserTest {

    @ParameterizedTest
    @DisplayName("Check parse return correct uri for valid first request line")
    @CsvSource({
            "GET /index.html HTTP/1.1, /index.html",
            "GET /styles.css HTTP/1.1, /styles.css",
            "  GET   /users?id=123    HTTP/1.1 , /users",
            "GET /index.html, /index.html",
            "GET /styles.css , /styles.css",
            "GET /users?id=12345, /users",
    })
    void checkParseReturnCorrectUriForValidFirstRequestLine(String requestLine, String expectedPath) throws IOException {
        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestLine + System.lineSeparator() + System.lineSeparator()).getBytes()))));
        assertEquals(expectedPath, actualRequest.getUri());
    }

    @ParameterizedTest
    @DisplayName("Check parse return null uri for non valid first request line")
    @CsvSource({
            "/index.html HTTP/1.1",
            "GETTT /styles.css HTTP/1.1",
            "GET  234 /users?id=123 HTTP/1.1",
            "GET ",
            "\"\"",
            "GET /styles.css  /styles.css /styles.css",
    })
    void checkParseReturnNullUriForNonValidFirstRequestLine(String requestLine) throws IOException {
        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestLine + System.lineSeparator() + System.lineSeparator()).getBytes()))));
        assertNull(actualRequest.getUri());
    }

    @ParameterizedTest
    @DisplayName("Check parse return correct httpmethod for valid first request line")
    @CsvSource({
            "GET /index.html HTTP/1.1, GET",
            "POST /styles.css HTTP/1.1, POST",
            "    GET   /users?id=123 , GET",
    })
    void checkParseReturnCorrectHttpMethodForValidFirstRequestLine(String requestLine, String expectedHttpMethod) throws IOException {
        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestLine + System.lineSeparator() + System.lineSeparator()).getBytes()))));
        assertEquals(expectedHttpMethod, actualRequest.getHttpMethod().name());
    }

    @ParameterizedTest
    @DisplayName("Check parse return null httpmethod for non valid first request line")
    @CsvSource({
            "GETTer /index.html HTTP/1.1",
            "POSTer /styles.css HTTP/1.1",
            "    get   /users?id=123",
            "    post   /users?id=123",
            "       /users?id=123",
    })
    void checkParseReturnNullHttpMethodForNonValidFirstRequestLine(String requestLine) throws IOException {
        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestLine + System.lineSeparator() + System.lineSeparator()).getBytes()))));
        assertNull(actualRequest.getHttpMethod());
    }
}
