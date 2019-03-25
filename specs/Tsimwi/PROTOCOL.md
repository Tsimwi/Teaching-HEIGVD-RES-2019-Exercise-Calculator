### What transport protocol do we use?
We use TCP protocol.
### How does the client find the server (addresses and ports)?
The server's IP address and port are given to the client as arguments when it is started.
### Who speaks first?
The client first speak to the server.
### What is the sequence of messages exchanged by the client and the server?
The client sends a calculation data, the server processes the calculation then sends its response to the client.
### What happens when a message is received from the other party?
Server : The message is parsed and treated.
Client : The response is read and printed on the terminal.
### What is the syntax of the messages? How we generate and parse them?
The message is a string containing numbers and operators. The way the server does the parsing is to be determined.
### Who closes the connection and when?
The server closes the connection as soon as it finished sending its response. The client can close the connection when it received all the data.
