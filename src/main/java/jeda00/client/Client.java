package jeda00.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final Logger logger = LogManager.getLogger(Client.class);

    private Socket socket;

    private BufferedReader bufferedReader;

    private PrintStream printStream;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 8888);

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        this.bufferedReader = new BufferedReader(isr);

        OutputStream os = socket.getOutputStream();
        this.printStream = new PrintStream(os, true);
    }

    public void start() throws IOException {
        logger.info("Client starting");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            printStream.println(line);

            String response = bufferedReader.readLine();
            System.out.println(response);

            if (response.equals("QUIT")) {
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (IOException e) {
            logger.error(e);
        }
    }

}
