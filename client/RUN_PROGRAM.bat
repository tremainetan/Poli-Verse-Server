javac -d bin/ -cp src src/database/*.java
javac -d bin/ -cp src src/datastructures/*.java
javac -d bin/ -cp src src/framework/*.java
javac -d bin/ -cp src src/main/*.java

java -cp bin main.ClientMain
exit