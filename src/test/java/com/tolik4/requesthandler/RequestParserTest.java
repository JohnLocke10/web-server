package com.tolik4.requesthandler;

import com.tolik4.webserver.exceptions.BadRequestException;
import com.tolik4.webserver.request.Request;
import com.tolik4.webserver.requesthandler.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Check parse throws bad request exception for non valid first request line")
    @CsvSource({
            "/index.html HTTP/1.1",
            "GETTT /styles.css HTTP/1.1",
            "GET  234 /users?id=123 HTTP/1.1",
            "GET ",
            "\"\"",
            "GET /styles.css  /styles.css /styles.css",
            "GETTer /index.html HTTP/1.1",
            "POSTer /styles.css HTTP/1.1",
            "    get   /users?id=123",
            "    post   /users?id=123",
            "       /users?id=123",
    })
    void checkParseThrowsBadRequestExceptionForNonValidFirstRequestLine(String requestLine) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream((requestLine
                                + System.lineSeparator()
                                + System.lineSeparator()).getBytes())));) {

            RequestParser requestParser = new RequestParser();

            BadRequestException actualException = assertThrows(BadRequestException.class, () -> {
                requestParser.parse(bufferedReader);
            });
            assertEquals("Bad request!", actualException.getMessage());
        }
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

    @Test
    @DisplayName("Check correctness of defining headers when they are present")
    void checkCorrectnessOfDefiningHeadersWhenTheyArePresent() throws IOException {
        String requestWithHeaders =
                "GET /users?id=123 HTTP/1.1\n" +
                        "Host: test.com\n" +
                        "Connection: keep-alive  " + System.lineSeparator() + System.lineSeparator();

        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestWithHeaders).getBytes()))));
        assertTrue(actualRequest.getHeaders().containsKey("Host"));
        assertTrue(actualRequest.getHeaders().containsKey("Connection"));
        assertEquals(actualRequest.getHeaders().get("Host"), "test.com");
        assertEquals(actualRequest.getHeaders().get("Connection"), "keep-alive");
    }

    @Test
    @DisplayName("check parse return request with empty headers when they are not present")
    void checkParseReturnRequestWithEmptyHeadersWhenTheyAreNotPresent() throws IOException {
        String requestWithHeaders =
                "GET /users?id=123 HTTP/1.1\n" + System.lineSeparator() + System.lineSeparator();

        Request actualRequest =
                new RequestParser().parse(
                        new BufferedReader(
                                new InputStreamReader(
                                        new ByteArrayInputStream((requestWithHeaders).getBytes()))));
        assertTrue(actualRequest.getHeaders().isEmpty());
    }
}
