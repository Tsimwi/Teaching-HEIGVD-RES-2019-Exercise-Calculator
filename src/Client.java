import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    Scanner reader;


    public Client() throws IOException {
        this("127.0.0.1", 1234);
    }

    public Client(String serverIP, int serverPort) {
        try {
            /* Create a socket with the right parameters, open streams */
            socket = new Socket(serverIP, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            reader = new Scanner(System.in);
            String line;

            /* Display a welcome message */
            line = in.readLine();
            System.out.println(line);

            compute();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }

    private void compute() {
        while (true) {
            String line;
            System.out.println("Enter an operation (press q to leave) : ");
            String input = reader.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Bye");
                break;
            }

            /* send input to server*/
            out.println(input);
            /* wait for the response */
            try {
                line = in.readLine();
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
