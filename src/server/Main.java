package server;

import java.io.IOException;
import java.sql.SQLException;

class Main {
    public static void main(String[] args) {
        final int serverPort = 6969;
        final String dbName = "inventory";
        final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
        final String dbUser = "diwas";
        final String dbPassword = "";

        try {
            ServerData db = new ServerData(dbUrl, dbUser, dbPassword);
            // db.addDummyProducts();

            ServerSock server = new ServerSock(serverPort, db);
            server.start();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Something wrong with database");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something wrong with server");
        }
    }
}