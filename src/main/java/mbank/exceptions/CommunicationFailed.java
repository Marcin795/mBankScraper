package mbank.exceptions;

public class CommunicationFailed extends RuntimeException {

    public CommunicationFailed() {
    }

    public CommunicationFailed(String message) {
        super(message);
    }
}
