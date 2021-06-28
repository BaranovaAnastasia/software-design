package client.lib.request;

import java.util.Objects;

/**
 * Client's request for server.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public abstract class Request {
    /**
     * String containing request key-word.
     */
    private final String request;

    /**
     * Returns a string containing request key-word.
     */
    public String getRequest() {
        return request;
    }

    /**
     * Constructs a new Request.
     *
     * @param request A string containing request key-word.
     * @throws NullPointerException If request string is null.
     */
    public Request(String request) {
        this.request = Objects.requireNonNull(request, "Request string was null");
    }
}
