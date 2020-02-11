package mbank.payload.executionOrder;

public class AuthorizationId {

    public final String scaAuthorizationId;

    public AuthorizationId(VerificationTokenAndAuthorizationTokenWithoutTranId stageFour) {
        scaAuthorizationId = stageFour.scaAuthorizationId;
    }
}
