package jeda00.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class AsyncReader extends Thread {

    private static final Logger logger = LogManager.getLogger(AsyncReader.class);

    private BufferedReader bufferedReader;

    private Consumer<String> consumer;

    private Consumer<AsyncReader> closer;

    private String line;

    public AsyncReader(InputStream inputStream, Consumer<String> consumer) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.consumer = consumer;
        this.line = null;

        start();
    }

    public void onInput(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void onClose(Consumer<AsyncReader> closer) {
        this.closer = closer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                line = bufferedReader.readLine();
            } catch (Exception e) {
                System.err.println(e.getMessage());

                return;
            }

            if (line == null) {
                if (closer != null) {
                    closer.accept(this);
                }

                return;
            }

            consumer.accept(line);
        }
    }

}
