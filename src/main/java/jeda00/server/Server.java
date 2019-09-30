package jeda00.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jeda00.util.EventBus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private ServerSocket serverSocket;

    private Map<String, Connection> connections;

    private EventBus eventBus;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(8888);
        this.connections = new HashMap<>();
        this.eventBus = new EventBus();
    }

    public void start() {
        logger.info("Server starting");

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                Connection connection = new Connection(socket, eventBus);
                connections.put(connection.getID(), connection);
                connection.onClose(c -> connections.remove(c.getID()));
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

}
