import java.io.*;
import java.net.*;


public class SimpleClient {

    static int port;
    static String hostname;

    public static void main(String[] args) {

        
        if (args.length != 2) {
            System.err.println("Usage - java SimpleClient <servername> <port>");
            System.exit(0);
        } else {
            try { /** Validates port is  number */
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Port number must be a number");
            }
        }

        try {
            /** Client connects to server */
            Socket socket;
            socket = startClient(hostname, port);

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

            String input = ""; /** Input from server. */
            String line = inFromUser.readLine(); /** Input from user/client.  */
            boolean exit = false;

            /**
             * Prints line from user/client side
             * Gets input from server, if the input is GOODBYE the client exits
             * otherwise the next input from user/client side is read
             */
            while (!exit) {
                toServer.println(line);
                input = inFromServer.readLine();
                if (input.equals("GOODBYE")) {
                    exit = true;
                } else {
                    line = inFromUser.readLine();
                }
            }
            
            /** Closing client, buffered readers and print writer */
            inFromServer.close();
            inFromUser.close();
            toServer.close();
            socket.close();

        } catch (SocketTimeoutException e) {

        } 
        catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } 


    }

    /** Client connects to the server */
    static Socket startClient(String hostname, int port) {
        Socket socket = null;

        try {
            InetAddress address;
            address = InetAddress.getByName(hostname);

            socket = new Socket(address, port);
            System.out.println("Connecting to - " + socket.toString());

        } catch (IOException e) {
            System.err.println("IO Exception - " + e.getMessage());
        }

        return socket;
    }
}
  