package client;

class Main {
    public static void main(String[] args) {
        final String host = "localhost";
        final int port = 6969;

        try {
            ClientSock csock = new ClientSock(host, port);
            ClientGui gui = new ClientGui(csock);
            gui.show();
            Reactor reactor = new Reactor(gui);
            reactor.listenAndReact();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception while making client socket");
        }
    }
}