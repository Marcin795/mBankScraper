package mbank.payload.executionOrder;

public class VerificationTokenAndAuthorizationTokenAndTranId {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;
    public final String tranId;

    public VerificationTokenAndAuthorizationTokenAndTranId(VerificationTokenAndAuthorizationId stageTwo, String tranId) {
        xRequestVerificationToken = stageTwo.xRequestVerificationToken;
        scaAuthorizationId = stageTwo.scaAuthorizationId;
        this.tranId = tranId;
    }
}
