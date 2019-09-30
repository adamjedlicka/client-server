package jeda00.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jeda00.events.NewMessage;
import jeda00.util.AsyncReader;
import jeda00.util.EventBus;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

public class Connection {

    private static final Logger logger = LogManager.getLogger(Connection.class);

    private final UUID id = UUID.randomUUID();

    private Socket socket;

    private EventBus eventBus;

    private AsyncReader asyncReader;

    private PrintStream printStream;

    private Consumer<Connection> closer;

    private int onNewMessageId;

    public Connection(Socket socket, EventBus eventBus) throws IOException {
        this.socket = socket;
        this.eventBus = eventBus;

        this.asyncReader = new AsyncReader(socket.getInputStream(), this::onSocketInput);
        this.asyncReader.onClose(this::onSocketClose);

        this.printStream = new PrintStream(socket.getOutputStream(), true);

        this.onNewMessageId = this.eventBus.subscribe(NewMessage.class, this::onNewMessage);
    }

    public void onClose(Consumer<Connection> closer) {
        this.closer = closer;
    }

    private void onSocketInput(String data) {
        eventBus.emit(new NewMessage(data));
    }

    private void onSocketClose(AsyncReader asyncReader) {
        this.eventBus.unsubscribe(NewMessage.class, onNewMessageId);

        if (closer != null) {
            closer.accept(this);
        }
    }

    private void onNewMessage(NewMessage message) {
        printStream.println(message.getData());
    }

    public String getID() {
        return id.toString();
    }

    public void test(Consumer<AsyncReader> consumer) {
        System.out.println(consumer.hashCode());
    }

}
