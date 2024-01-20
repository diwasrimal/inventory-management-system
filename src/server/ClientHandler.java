package server;

import java.io.EOFException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.UUID;

import messages.ProductAddRequest;
import utils.ObjStreams;
import utils.Product;

class ClientHandler implements Runnable {
    Socket sock;
    ServerData db;
    ObjStreams streams;

    ClientHandler(Socket sock, ServerData db) {
        this.sock = sock;
        this.db = db;
        this.streams = ObjStreams.fromSocket(sock);
    }

    @Override
    public void run() {
        handle();
    }

    /**
     * Handles a connection to client by sending and receiving messages.
     * It invokes methods that interact with database while handling messages.
     */
    private void handle() {
        String id = UUID.randomUUID().toString().substring(0, 5);
        System.out.println("Handling connection for client " + id + "...");

        while (true) {
            try {
                Object msg = this.streams.receiveMessage();
                System.out.println(msg);

                if (msg instanceof ProductAddRequest req) {
                    Product prod = req.prod;
                    try {
                        this.db.addNewProduct(prod);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Error while adding new product to database");
                    }
                }
            } catch (EOFException e) {
                break;
            }
        }

        System.out.println("Not handling " + id + " anymore..");
    }
}