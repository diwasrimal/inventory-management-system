#!/bin/bash

[ -z "$1" ] && exit 1

shared="src/messages/*.java src/utils/*.java"

if [ "$1" = "server" ]; then
    set -xe
    javac -d bin src/server/*.java $shared && 
        java -cp bin:lib/mysql-connector-j-8.3.0.jar server.Main 

elif [ "$1" = "client" ]; then
    set -xe
    javac -d bin src/client/*.java $shared &&
        java -cp bin client.Main
fi
