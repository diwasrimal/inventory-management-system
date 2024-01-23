package server;

import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import messages.Disconnect;
import messages.FailureResponse;
import messages.ProductListRequest;
import messages.ProductListResponse;
import messages.ProductAddRequest;
import messages.ProductDeleteRequest;
import messages.ProductEditRequest;
import messages.SuccessResponse;
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
            Object msg = this.streams.receiveMessage();
            System.out.println(msg);

            if (msg instanceof Disconnect) {
                break;
            }
            else if (msg instanceof ProductAddRequest req) {
                Product prod = req.prod;
                try {
                    this.db.addNewProduct(prod);
                    this.streams.sendMessage(new SuccessResponse());
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error while adding new product to database");
                    this.streams.sendMessage(new FailureResponse());
                }
            }
            else if (msg instanceof ProductDeleteRequest req) {
                try {
                    this.db.deleteProduct(req.prodId);
                    this.streams.sendMessage(new SuccessResponse());
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error while deleting product from database");
                    this.streams.sendMessage(new FailureResponse());
                }
            }
            else if (msg instanceof ProductListRequest) {
                try {
                    List<Product> products = this.db.getProducts();
                    this.streams.sendMessage(new ProductListResponse(products));
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error while retreiving products from database");
                }
            }
            else if (msg instanceof ProductEditRequest req) {
                try {
                    this.db.editProduct(req.prodId, req.newProd);
                    this.streams.sendMessage(new SuccessResponse());
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error while retreiving products from database");
                    this.streams.sendMessage(new FailureResponse());
                }
            }
        }

        System.out.println("Not handling " + id + " anymore..");
    }
}