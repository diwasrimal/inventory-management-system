## Inventory Management System

This is a simple inventory management system, which uses Swing
for the GUI, JDBC for managing MySQL database and socket programming
for handling client-server communications.

Work In Progress...

### Setup
```
$ git clone https://github.com/diwasrimal/inventory-management-system.git
$ cd inventory-management-system
$ javac -d bin src/*/*.java
$ java -cp bin:lib/mysql-connector-j-8.3.0.jar server.Main
$ java -cp bin client.Main
```

Much quicker if you have a unix shell, no need to compile manually
```
$ ./run.sh server
$ ./run.sh client
```
