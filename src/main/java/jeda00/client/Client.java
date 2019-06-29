package jeda00.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final Logger logger = LogManager.getLogger(Client.class);

    private Socket socket;

    private PrintStream printStream;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 8888);
        OutputStream os = socket.getOutputStream();
        this.printStream = new PrintStream(os, true);
    }

    public void start() {
        logger.info("Client starting");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            printStream.println(line);
        }
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
