import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    int port;
    final static Logger LOG = Logger.getLogger(Server.class.getName());


    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            return;
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerThread(clientSocket)).start();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This class represents the server threads
     */
    private class ServerThread implements Runnable {

        Socket clientSocket;
        BufferedReader in = null;
        PrintWriter out = null;


        public ServerThread(Socket clientSocket) {
            try {
                this.clientSocket = clientSocket;
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.out = new PrintWriter(clientSocket.getOutputStream());

                this.out.println("Welcome to the calculation server. Please send your requests.");
                this.out.flush();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {

            boolean shouldRun = true;
            String line;
            String operators = "+-*/";

            try {
                LOG.info("Reading until client sends 'q' or closes the connection...");

                while ((shouldRun) && (line = in.readLine()) != null) {

                    if (line.equalsIgnoreCase("q")) {
                        shouldRun = false;
                    }

                    Double result = null;
                    String operand1 = "";
                    String operand2 = "";
                    String operator = "";
                    boolean beforeOperator = true;
                    boolean validOperator = false;
                    boolean error = false;

                    /* Spaces in the input are removed */
                    String lineWithoutSpaces = line.replaceAll(" ", "");

                    /* Process string character by character */
                    for (char c : lineWithoutSpaces.toCharArray()) {

                        /* First operand */
                        if (beforeOperator && (Character.isDigit(c) || c == '.')) {
                            operand1 += (char) c;

                        /* Second operand */
                        } else if (!beforeOperator && (Character.isDigit(c) || c == '.')) {
                            operand2 += (char) c;

                        /* Operator */
                        } else {

                            /* Check if the character is effectively an operator */
                            for (char op : operators.toCharArray()) {

                                /* A valid operator is found */
                                if (c == op && validOperator == false) {
                                    operator += (char) c;
                                    validOperator = true;
                                    beforeOperator = false;

                                /* If a valid operator has already been found, a second one is not allowed */
                                } else if (c == op && validOperator == true) {
                                    error = true;
                                    break;

                                /* If the character is a letter, the operation is not valid */
                                } else if (Character.isLetter(c)) {
                                    error = true;
                                    break;
                                }
                            }

                            /* If no operand has been found, the operation is not valid */
                            if (!validOperator && error == false) {
                                break;
                            }
                        }
                    }

                    if (error || line.isEmpty() || operator.length() > 1 || operand1.isEmpty() || operand2.isEmpty() ||
                            countOccurrences(operand1, '.') > 1 || countOccurrences(operand2, '.') > 1) {
                        this.out.println("Invalid request. Try again.");
                        out.flush();
                        continue;
                    }

                    LOG.info("Calculation : " + operand1 + " " + operator + " " + operand2);

                    switch (operator) {
                        case "+":
                            result = Double.parseDouble(operand1) + Double.parseDouble(operand2);
                            break;
                        case "-":
                            result = Double.parseDouble(operand1) - Double.parseDouble(operand2);
                            break;
                        case "*":
                            result = Double.parseDouble(operand1) * Double.parseDouble(operand2);
                            break;
                        case "/":
                            result = Double.parseDouble(operand1) / Double.parseDouble(operand2);
                            break;
                    }

                    if (result != null) {
                        LOG.info("Result : " + result);
                        this.out.println("Result : " + result);
                        this.out.flush();
                    }
                }

                LOG.info("Cleaning up resources...");
                clientSocket.close();
                in.close();
                out.close();

            } catch (IOException ex) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }


    public static int countOccurrences(String str, char c) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Server srv = new Server(1234);
        srv.run();
    }

}
