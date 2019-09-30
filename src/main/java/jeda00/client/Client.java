package jeda00.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jeda00.util.AsyncReader;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final Logger logger = LogManager.getLogger(Client.class);

    private Socket socket;

    private AsyncReader socketAsyncReader;

    private PrintStream printStream;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 8888);

        this.socketAsyncReader = new AsyncReader(socket.getInputStream(), this::onSocketInput);

        this.printStream = new PrintStream(socket.getOutputStream(), true);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();

            printStream.println(line);
        }
    }

    private void onSocketInput(String data) {
        System.out.println(data);
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
