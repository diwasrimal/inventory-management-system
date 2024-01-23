package client;

import java.util.List;

import messages.Disconnect;
import messages.FailureResponse;
import messages.ProductListResponse;
import messages.SuccessResponse;
import utils.Product;

/**
 * Takes in a GUI and reacts to incoming server messages,
 * invoking GUI handling methods if necessary.
 * GUI is closed on receiving a disconnect message.
 */
class Reactor {
    ClientGui gui;

    Reactor(ClientGui gui) {
        this.gui = gui;
        listenAndReact();
    }

    void listenAndReact() {
        if (this.gui.conn == null) {
            System.out.println("gui.conn == null");
            return;
        }

        System.out.println("Listening for server messages..");
        while (!this.gui.conn.isClosed()) {
            Object msg = this.gui.conn.receiveMessage();
            System.out.println(msg);

            if (msg instanceof Disconnect) {
                this.gui.close();
                break;
            }
            else if (msg instanceof ProductListResponse res) {
                System.out.println(res.products);
                this.gui.refillProductsPanel(res.products);
            }
            else if (msg instanceof SuccessResponse) {
                gui.showDialog("Success");
            }
            else if (msg instanceof FailureResponse) {
                gui.showDialog("Failure");
            }

            // TODO: Maybe use a message inside Success and Failure responses
            // TODO: handle other incoming messages
        }

        System.out.println("Not listening and reacting anymore...");
    }
}