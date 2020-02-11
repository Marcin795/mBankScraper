package mbank.payload.executionOrder;

public class VerificationTokenAndAuthorizationTokenWithoutTranId {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;

    public VerificationTokenAndAuthorizationTokenWithoutTranId(VerificationTokenAndAuthorizationTokenAndTranId stage2fa) {
        xRequestVerificationToken = stage2fa.xRequestVerificationToken;
        scaAuthorizationId = stage2fa.scaAuthorizationId;
    }
}
