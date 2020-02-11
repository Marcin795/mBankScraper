package mbank.payload.executionOrder;

public class VerificationTokenAndAuthorizationId {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;

    public VerificationTokenAndAuthorizationId(String xRequestVerificationToken, String scaAuthorizationId) {
        this.xRequestVerificationToken = xRequestVerificationToken;
        this.scaAuthorizationId = scaAuthorizationId;
    }
}
