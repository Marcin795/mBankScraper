package mbank.payload.executionOrder;

public class StageFive {

    public final String scaAuthorizationId;

    public StageFive(StageFour stageFour) {
        scaAuthorizationId = stageFour.scaAuthorizationId;
    }
}
