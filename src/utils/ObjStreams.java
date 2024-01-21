package utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Disconnect;

/**
 * Initializes a pair of {@code ObjectInputStream} and {@code ObjectOutputStream}
 * for communicating to other socket endpoint.
 */
public class ObjStreams {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // NOTE: must create ObjectOutputStream first
    // https://forums.oracle.com/ords/apexds/post/problems-creating-objectinputstream-from-socket-inputstream-4275
    public static ObjStreams fromSocket(Socket sock) {
        try {
            ObjStreams streams = new ObjStreams();
            streams.out = new ObjectOutputStream(sock.getOutputStream());
            streams.in = new ObjectInputStream(sock.getInputStream());
            return streams;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while initializing object streams");
        }
        return null;
    }

    /**
     * Sends message to other endpoint
     */
    public void sendMessage(Object msg) {
        try {
            this.out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while writing object.");
        }
    }

    /**
     * Receives message sent from other endpoint.
     * If EOFException is thrown while reading, a Disconnect message is returned.
     */
    public Object receiveMessage() {
        try {
            return this.in.readObject();
        } catch (EOFException e) {
            return new Disconnect();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            System.out.println("Error while reading object.");
        }
        return null;
    }
}