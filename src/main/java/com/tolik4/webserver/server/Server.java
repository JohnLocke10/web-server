package com.tolik4.webserver.server;

import com.tolik4.webserver.requesthandler.RequestHandler;
import com.tolik4.webserver.requesthandler.ResourceReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private String webAppPath;

    private static final int DEFAULT_PORT = 4000;
    private static final String DEFAULT_WEB_APP_PATH = "src/main/resources/";

    public Server() {
        this.port = DEFAULT_PORT;
        this.webAppPath = DEFAULT_WEB_APP_PATH;
    }

    public void start() throws IOException {
        ResourceReader resourceReader = new ResourceReader(webAppPath);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));) {
                    new RequestHandler(bufferedReader, bufferedWriter, resourceReader).handle();
                }
            }
        }
    }
}
