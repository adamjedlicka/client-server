package jeda00.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

public class Connection extends Thread {

    private static final Logger logger = LogManager.getLogger(Connection.class);

    private final UUID id = UUID.randomUUID();

    private Socket socket;

    private BufferedReader bufferedReader;

    private PrintStream printStream;

    private Consumer<Connection> closeConsumer;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        this.bufferedReader = new BufferedReader(isr);

        OutputStream os = socket.getOutputStream();
        this.printStream = new PrintStream(os, true);

        this.closeConsumer = null;
    }

    @Override
    public void run() {
        logger.info("New connection: {}", getID());

        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line.equals("QUIT") || line == null) {
                    printStream.println("QUIT");

                    logger.info("Connection closed: {}", getID());
                    socket.close();
                    if (closeConsumer != null)
                        closeConsumer.accept(this);
                    return;
                }

                printStream.println("OK");

                System.out.println(line);
            } catch (IOException e) {
                logger.error(e);
                return;
            }
        }
    }

    public void onClose(Consumer<Connection> closeConsumer) {
        this.closeConsumer = closeConsumer;
    }

    public String getID() {
        return id.toString();
    }

}
