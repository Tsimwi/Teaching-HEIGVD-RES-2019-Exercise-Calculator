### What transport protocol do we use?
We use TCP protocol.
### How does the client find the server (addresses and ports)?
The server's IP address and port are given to the client as arguments when it is started.
### Who speaks first?
The server first speaks to the client.
### What is the sequence of messages exchanged by the client and the server?
The client sends a calculation data, the server processes the calculation then sends its response to the client.
### What happens when a message is received from the other party?
Server : The message is parsed and treated.
Client : The response is read and printed on the terminal.
### What is the syntax of the messages? How we generate and parse them?
The message is a string containing numbers and operators. The server reads the message character by character and check if the message is valid or not.
### Who closes the connection and when?
The client closes the connection whenever it finished sending requests. As soon the server receives the closure, it does
the same.
