package mbank.payload.executionOrder;

public class Stage2fa {

    public final String xRequestVerificationToken;
    public final String scaAuthorizationId;
    public final String tranId;

    public Stage2fa(StageTwo stageTwo, String tranId) {
        xRequestVerificationToken = stageTwo.xRequestVerificationToken;
        scaAuthorizationId = stageTwo.scaAuthorizationId;
        this.tranId = tranId;
    }
}
