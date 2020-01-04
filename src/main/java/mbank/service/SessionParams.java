package mbank.service;

class SessionParams {

    private String xTabId;
    private String xRequestVerificationToken;
    private String scaAuthorizationId;
    private String tranId;

    public SessionParams() {
    }

    public String getXTabId() {
        return this.xTabId;
    }

    public String getXRequestVerificationToken() {
        return this.xRequestVerificationToken;
    }

    public String getScaAuthorizationId() {
        return this.scaAuthorizationId;
    }

    public String getTranId() {
        return this.tranId;
    }

    public void setXTabId(String xTabId) {
        this.xTabId = xTabId;
    }

    public void setXRequestVerificationToken(String xRequestVerificationToken) {
        this.xRequestVerificationToken = xRequestVerificationToken;
    }

    public void setScaAuthorizationId(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

}
