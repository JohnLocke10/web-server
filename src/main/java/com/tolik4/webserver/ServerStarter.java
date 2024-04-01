package com.tolik4.webserver;

import com.tolik4.webserver.server.Server;

import java.io.IOException;

public class ServerStarter {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
