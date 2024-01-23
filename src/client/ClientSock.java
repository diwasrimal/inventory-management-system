package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utils.ObjStreams;
import utils.Product;
import messages.ProductAddRequest;
import messages.ProductDeleteRequest;

class ClientSock {
    private Socket sock;
    private ObjStreams streams;

    ClientSock(String host, int port) throws UnknownHostException, IOException {
        this.sock = new Socket(host, port);
        this.streams = ObjStreams.fromSocket(this.sock);
    }

    /**
     * Closes client connection, invoked mainly when a disconnect message
     * is received.
     */
    void close() {
        try {
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while trying to close connection from client side");
        }
    }

    boolean isClosed() {
        return this.sock.isClosed();
    }

    /**
     * Sends message to server for adding a new product
     */
    void sendProductAddRequest(Product prod) {
        this.streams.sendMessage(new ProductAddRequest(prod));
    }

    /**
     * Sends message to server for deleting a product
     */
    void sendProductDeleteRequest(int prodId) {
        this.streams.sendMessage(new ProductDeleteRequest(prodId));
    }

    void sendMessage(Object msg) {
        this.streams.sendMessage(msg);
    }

    Object receiveMessage() {
        return this.streams.receiveMessage();
    }
}