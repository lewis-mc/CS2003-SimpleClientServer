import java.io.*;
import java.net.*;
import java.time.*;

public class SimpleServer {

    static int port;
    static ServerSocket server; 

    public static void main(String[] args) {
        
        /** Checks the right amount of arguements are provided. */
        if (args.length != 1) { 
            System.err.println("Usage - java SimpleServer <port>");
            System.exit(0);
        } else {
            try { /** Checking the port number entered is a number. */
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Port number must be a number");
                System.exit(0);
            }
        }


        /** Starting the server. */
        try {
            server = new ServerSocket(port);
            System.out.println("Started server - " + server.toString());
        } catch (IOException e) {
            System.err.println("IO Exception - " + e.getMessage());
        }

        
        try {
            Socket socket;
            /** Accepts connection from client. */
            socket = server.accept();

            BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true); /** autoFlush set to true. */

            /** Waits for input from client. */
            String line = fromClient.readLine();

            boolean greetingComplete = false;
            /** Until correct first message is recieved server continues to send 'PARDON'. */
            while (!greetingComplete) {
                if (line.equals("HELLO")) {
                    toClient.println("HI NICE TO MEET YOU");
                    greetingComplete = true;
                } else {
                    toClient.println("PARDON");
                    line = fromClient.readLine(); /** waits for next input. */
                }
            }

            /** Waits for next input. */
            line = fromClient.readLine();

            boolean endConversation = false;

            while (!endConversation) {
                /** When the end message is received, the conversatio ends. */
                if (line.equals("BYE")) {
                    endConversation = true;
                } 
                    /** When asked for the time */
                    else if (line.equals("WHAT IS THE TIME?")) {
                        OffsetDateTime dateTime = OffsetDateTime.now(); /** Gets the current in the correct format. */
                        toClient.println("THE TIME IS: " + dateTime); /** Prints to client */
                        line = fromClient.readLine(); /** Waits for next input. */
                    } 

                    /** When asked to add 'x' and 'y'. */
                    else if (line.startsWith("PLEASE ADD ")) {
                        int length = line.length(); /** Gets total length of line received. */
                        int afterPleaseAdd = 11;
                        String nums = line.substring(afterPleaseAdd, length); /** Creates a string of the input text after 'PLEASE ADD '. Starting at index 11. */
                        String[] numbers = nums.split(" "); /** Makes an array of the numbers at the end of the string. */

                            /** Validation to check there are only 2 numbers received. */
                            if (numbers.length != 2) { 
                                toClient.println("PARDON");
                            } else {
                                /** If anything but a number is entered as 'x' or 'y' then 'PARDON' is returned */
                                try {
                                    int total = Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
                                    toClient.println("THE SUM IS " + total);
                                } catch (NumberFormatException e) {
                                    toClient.println("PARDON");
                                }
                            }
                            line = fromClient.readLine(); /** waits for next input. */
                    } 

                /** For when the input isn't anything expected. */
                else {
                    toClient.println("PARDON");
                    line = fromClient.readLine(); /** waits for next input. */
                }
            }

            /** Sends final message before server exits */
            toClient.println("GOODBYE");
            
            /** Closing server, socket, buffered readers and print writers */
            fromClient.close();
            toClient.close();
            socket.close();
            server.close();

        } catch (IOException e) {
            System.err.println("IO Exception - " + e.getMessage());
        }
    }
}