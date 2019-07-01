package RegalisatorEnterprise.application.exceptions;

/**
 * Created by lback on 28.06.17.
 */
public class PaketNichtGefundenException extends RuntimeException{
    public PaketNichtGefundenException() {
        super();
    }

    public PaketNichtGefundenException(String message) {
        super(message);
    }
}
