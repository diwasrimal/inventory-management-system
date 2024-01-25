## Inventory Management System

This is a simple inventory management system, which uses Swing
for the GUI, JDBC for managing MySQL database and socket programming
for handling client-server communications.

### Demo
https://github.com/diwasrimal/inventory-management-system/assets/84910758/1acc0fa5-bbdb-41b0-92a4-bf6e597bacfe

### Setup
```
$ git clone https://github.com/diwasrimal/inventory-management-system.git
$ cd inventory-management-system
$ javac -d bin src/*/*.java
$ java -cp bin:lib/mysql-connector-j-8.3.0.jar server.Main
$ java -cp bin client.Main
```

Much quicker if you have a UNIX shell, no need to compile manually
```
$ ./run.sh server
$ ./run.sh client
```

### Generate documentation
```
javadoc -private -d doc src/*/*.java
```
Then open `doc/index.html` in browser.

### Structure
```
.
├── ...
├── lib (jar files)
└── src
    ├── client
    ├── server
    ├── messages (serializable classes for client-server communication)
    └── utils (common classes for client and server)
```

### Working
#### Server

The server side contains four files
```
``src/server
├── ClientHandler.java
├── Main.java
├── ServerData.java
└── ServerSock.java
```

`Main.java` is the entry point of server, which initializes `ServerData` and `ServerSock` objects.
The database is handled by `ServerData` which is provided as an argument to `ServerSock`, the
which is responsible for handling clients by creating a new `ClientHandler` objects for each connected
client. The `ClientHandler` object implements the `Runnable` interface which allows each client to
be handled in separate thread.

`ClientHandler` also listens for client messages, and reacts to those messages by invoking some method
provided by `ServerData`. It also sends messages to client.


#### Client
```
src/client
├── ClientGui.java
├── ClientSock.java
├── Main.java
└── Reactor.java
```

Again `Main.java` is the entry point. `Main.java` first makes a connection to server on port `4000`
with `ClientSock`. `ClientSock` establishes the server connection and contains object streams for communication.
It also contains methods that are invoked by `ClientGui` object. Allowing communication from GUI to server.

`Reactor` listens for server messages and reacts to those messages, by invoking some GUI methods. It takes `ClientGui`
as an argument and changes the GUI based on incoming server messages.

#### Utils
```
src/utils
├── ObjStreams.java
└── Product.java
```
This package contains common class definitions that are used by both client and server. It contains `ObjStreams` which
makes a pair of `ObjectInputStream` and `ObjectOutputStream` for communication.

#### Messages
```
src/messages
├── Disconnect.java
├── FailureResponse.java
├── ProductAddRequest.java
├── ProductDeleteRequest.java
├── ProductEditRequest.java
├── ProductListRequest.java
├── ProductListResponse.java
└── SuccessResponse.java
```
These are serializable classes used for client server communication. Each message type implements the `java.io.Serializable`
interface.

| Message Type                | Sender            | Usage                                                                       |
|-----------------------------|-------------------|-----------------------------------------------------------------------------|
| `Disconnect`                | server <-> client | Notifies that one side has disconnected.                                    |
| `FailureResponse`           | server -> client  | If some requested operation (mostly database related) fails on server side. |
| `SuccessResponse`           | server -> client  | If some requested operation was successful.                                 |
| `ProductListResponse`       | server -> client  | Response for `ProductListRequest`.                                          |
| `ProductAddRequest`         | client -> server  | Request to server for recording new product to database.                    |
| `ProductDeleteRequest`      | client -> server  | Request to server for deleting existing product.                            |
| `ProductEditRequest`        | client -> server  | Request for editing existing product.                                       |
| `ProductListRequest`        | client -> server  | Request to send back products in database.                                  |

### License
Licensed under MIT License, © Diwas Rimal, 2024
