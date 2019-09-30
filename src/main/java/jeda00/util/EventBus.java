package jeda00.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {

    private Map<Class<?>, List<Consumer<Object>>> handlers;

    private Map<Class<?>, List<Object>> sentEvents;

    public EventBus() {
        handlers = new HashMap<>();
        sentEvents = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> int subscribe(Class<T> event, Consumer<T> handler) {
        handlersFor(event).add((Consumer<Object>) handler);

        return handler.hashCode();
    }

    public synchronized <T> boolean unsubscribe(Class<T> event, int id) {
        return handlersFor(event).removeIf(h -> h.hashCode() == id);
    }

    public synchronized <T> void emit(T event) {
        sentEventsFor(event.getClass()).add(event);

        for (Consumer<Object> handler : handlersFor(event.getClass())) {
            handler.accept(event);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getSentEvents(Class<T> event) {
        return (List<T>) sentEventsFor(event);
    }

    /**
     * Returns the list of handlers for the given event type. If no handlers exists,
     * returns an empty list.
     *
     * @param type Type of the event for which handlers are returned
     * @return List of handlers for the given event type
     */
    private <T> List<Consumer<Object>> handlersFor(Class<T> type) {
        return handlers.computeIfAbsent(type, e -> new ArrayList<>());
    }

    /**
     * Returns list of sent events for the given type. If no sent events for the
     * given type exists, returns an empty list.
     *
     * @param type Type of the event for which list of sent events is returned
     * @return List of sent events for the given type
     */
    private <T> List<Object> sentEventsFor(Class<T> type) {
        return sentEvents.computeIfAbsent(type, t -> new ArrayList<>());
    }
}
