package mbank.payload.executionOrder;

public class StageFour {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;

    public StageFour(Stage2fa stage2fa) {
        xRequestVerificationToken = stage2fa.xRequestVerificationToken;
        scaAuthorizationId = stage2fa.scaAuthorizationId;
    }
}
