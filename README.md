# Usage

## Server
To start the server
~~~shell
java -jar ./rmi_test-1.0.jar server
~~~

## Client
To start the client
~~~shell
java -jar ./rmi_test-1.0.jar client
~~~
the available commands are:
~~~
add [number]
result
close
~~~

## Test
To test N random clients
~~~shell
java -jar ./rmi_test-1.0.jar [N]
~~~

# Build
## Requirements
- Maven (tested with version 3.9.6)
- Java (tested with version 21)

## Instruction

~~~shell
git clone https://github.com/daniele-ieva/rmi_test.git && cd rmi_test 
mvn package
~~~
The jar archive will be in the target folder
