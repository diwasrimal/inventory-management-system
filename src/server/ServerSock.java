package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSock {
    int serverPort;
    ServerSocket ss;
    ServerData db;

    ServerSock(int serverPort, ServerData db) throws IOException {
        this.serverPort = serverPort;
        this.ss = new ServerSocket(serverPort);
        this.db = db;
    }

    void start() throws IOException {
        System.out.println("Listening on port " + this.serverPort + "...");
        while (true) {
            Socket sock = this.ss.accept();
            ClientHandler handler = new ClientHandler(sock, this.db);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
        }
    }
}