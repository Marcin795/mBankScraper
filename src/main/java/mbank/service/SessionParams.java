package mbank.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class SessionParams {

    String xTabId;
    String xRequestVerificationToken;
    String scaAuthorizationId;
    String tranId;

}
