package mbank.exceptions;

public class CommunicationFailed extends RuntimeException {

    public CommunicationFailed(Throwable cause) {
        super(cause);
    }

    public CommunicationFailed(String message) {
        super(message);
    }

}
