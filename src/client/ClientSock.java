package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.ObjStreams;
import utils.Product;
import messages.ProductAddRequest;

class ClientSock {
    private Socket sock;
    private ObjStreams streams;

    ClientSock(String host, int port) throws UnknownHostException, IOException {
        this.sock = new Socket(host, port);
        this.streams = ObjStreams.fromSocket(this.sock);
    }

    /**
     * Closes client connection by sending di
     */
    void close() throws IOException {
        this.sock.close();
    }

    /**
     * Sends message to server for adding a new product
     */
    void sendProductAddRequest(Product prod) {
        this.streams.sendMessage(new ProductAddRequest(prod));
    }
}