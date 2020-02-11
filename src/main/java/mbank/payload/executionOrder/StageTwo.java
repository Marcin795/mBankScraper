package mbank.payload.executionOrder;

public class StageTwo {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;

    public StageTwo(String xRequestVerificationToken, String scaAuthorizationId) {
        this.xRequestVerificationToken = xRequestVerificationToken;
        this.scaAuthorizationId = scaAuthorizationId;
    }
}
