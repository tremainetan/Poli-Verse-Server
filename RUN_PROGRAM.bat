javac -d bin/ -cp src src/datastructures/*.java
javac -d bin/ -cp src src/server/*.java

java -cp bin server.ServerMain