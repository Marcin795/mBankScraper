package unit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import model.Credentials;
import okhttp3.Headers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import mbank.model.request.LoginRequestBody;
import mbank.model.response.LoginResponseBody;
import util.Http;

import java.io.UncheckedIOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class HttpTest {

    private static final WireMockServer wireMockServer = new WireMockServer(8090);
    private static final Http http = new Http();
    private static final String TEST_ADDRESS = "http://127.0.0.1:8090";
    private static final Headers TEST_HEADERS = new Headers.Builder()
            .add("testKey", "testValue")
            .build();
    private static final LoginResponseBody EXPECTED_RESPONSE_BODY = new LoginResponseBody(false, "is just a test");
    private static final String EXPECTED_RESPONSE_BODY_JSON = "{\"successful\":false,\"errorMessageTitle\":\"is just a test\"}";
    private static final LoginRequestBody EXPECTED_REQUEST_BODY = new LoginRequestBody(new Credentials("adam", "pass"));
    private static final String EXPECTED_REQUEST_BODY_JSON = "{\"UserName\":\"adam\",\"Password\":\"pass\"}";

    @BeforeAll
    static void start() {
        WireMock.configureFor(8090);
        wireMockServer.start();
    }

    @AfterAll
    static void stop() {
        wireMockServer.stop();
    }

    @Test
    void sendsProperlyAndReturnStatus() {
        var expected = 200;
        wireMockServer.stubFor(get(urlEqualTo("/responseWithoutBody"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withStatus(expected)
                        .withBody("{\"key\": \"value\"}")));
        var actual = http.get(TEST_ADDRESS + "/responseWithoutBody");
        verify(getRequestedFor(urlEqualTo("/responseWithoutBody")));
        assertEquals(expected, actual);
    }

    @Test
    void requestSendsHeadersAndBody() {
        wireMockServer.stubFor(post(urlEqualTo("/void/post"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)));
        http.post(TEST_ADDRESS + "/void/post", EXPECTED_REQUEST_BODY, TEST_HEADERS);
        verify(postRequestedFor(urlEqualTo("/void/post"))
                .withHeader("testKey", equalTo("testValue"))
                .withRequestBody(equalToJson(EXPECTED_REQUEST_BODY_JSON)));
    }

    @Test
    void sendsProperHeadersAndReturnsResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/response/with/body"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withBody(EXPECTED_RESPONSE_BODY_JSON)));
        var actual = http.get(TEST_ADDRESS + "/response/with/body", LoginResponseBody.class, TEST_HEADERS);
        verify(getRequestedFor(urlEqualTo("/response/with/body"))
                .withHeader("testKey", equalTo("testValue")));
        assertResponseEquals(EXPECTED_RESPONSE_BODY, actual);
    }

    @Test
    void sendsProperHeadersAndBodyAndReturnsResponse() {
        wireMockServer.stubFor(post(urlEqualTo("/response/with/body"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withBody(EXPECTED_RESPONSE_BODY_JSON)));
        var actual = http.post(TEST_ADDRESS + "/response/with/body", LoginResponseBody.class, EXPECTED_REQUEST_BODY, TEST_HEADERS);
        verify(postRequestedFor(urlEqualTo("/response/with/body"))
                .withHeader("testKey", equalTo("testValue"))
                .withRequestBody(equalToJson(EXPECTED_REQUEST_BODY_JSON)));
        assertResponseEquals(actual, EXPECTED_RESPONSE_BODY);
    }

    static void assertResponseEquals(LoginResponseBody expected, LoginResponseBody actual) {
        assertEquals(expected.successful, actual.successful);
        assertEquals(expected.errorMessageTitle, actual.errorMessageTitle);
    }

    @Test
    void faultyResponseThrowsUncheckedIOException() {
        wireMockServer.stubFor(get(urlEqualTo("/fault"))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        assertThrows(UncheckedIOException.class, () -> http.get(TEST_ADDRESS + "/fault"));
    }
}
