package client.lib.request;

/**
 * Request that informs the server about the end of client's work.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class FinishRequest extends Request {
    /**
     * Constructs a new FinishRequest.
     */
    public FinishRequest() {
        super("finish");
    }
}
