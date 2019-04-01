import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            new Client();
        } else if (args.length == 2) {
            new Client(args[0], Integer.parseInt(args[1]));
        } else {
            System.out.println("Usage : either without parameters, either with <ip address> <port>");
        }
    }
}
