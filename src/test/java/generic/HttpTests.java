package generic;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.google.gson.Gson;
import exceptions.CommunicationFailed;
import mbank.model.payload.request.LoginRequestBody;
import mbank.model.payload.response.LoginResponseBody;
import util.Http;
import model.Credentials;
import okhttp3.Headers;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class HttpTests {

    private static final Gson GSON = new Gson();
    private static final WireMockServer wireMockServer = new WireMockServer(8090);
    private static final Http http = new Http("http://127.0.0.1:8090");
    private static final Headers TEST_HEADERS = new Headers.Builder()
            .add("testKey", "testValue")
            .build();
    private static final LoginResponseBody LOGIN_RESPONSE_BODY = new LoginResponseBody(false, "is just a test");
    private static final LoginRequestBody LOGIN_REQUEST_BODY = new LoginRequestBody(new Credentials("adam", "małysz"));

    @BeforeAll
    static void start() {
        WireMock.configureFor(8090);
        wireMockServer.start();
        setupStub();
    }

    @AfterAll
    static void stop() {
        wireMockServer.stop();
    }

    static void setupStub() {
        wireMockServer.stubFor(get(urlEqualTo("/responseWithoutBody"))
                .willReturn(aResponse().withHeader("Content-Type", "text/plain")
                        .withStatus(200)
                        .withBody("{\"dupa\": \"gowno\"}")));

        wireMockServer.stubFor(post(urlEqualTo("/void/post"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withStatus(200)));

        wireMockServer.stubFor(get(urlEqualTo("/response/with/body"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withBody(GSON.toJson(LOGIN_RESPONSE_BODY))));

        wireMockServer.stubFor(post(urlEqualTo("/response/with/body"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/json")
                        .withBody(GSON.toJson(LOGIN_RESPONSE_BODY))));

        wireMockServer.stubFor(get(urlEqualTo("/fault"))
                .willReturn(aResponse().withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
    }

    @Test
    void getWithoutBodyTest() {
        var response = http.get("/responseWithoutBody");
        verify(getRequestedFor(urlEqualTo("/responseWithoutBody")));
        Assertions.assertEquals(200, response.status);
    }

    @Test
    void voidPostTest() {
        var bodyJson = GSON.toJson(LOGIN_REQUEST_BODY);
        http.post("/void/post", LOGIN_REQUEST_BODY, TEST_HEADERS);
        verify(postRequestedFor(urlEqualTo("/void/post"))
                .withHeader("testKey", equalTo("testValue"))
                .withRequestBody(equalToJson(bodyJson)));
    }

    @Test
    void getWithResponseTest() {
        var respones = http.get("/response/with/body", LoginResponseBody.class, TEST_HEADERS);
        verify(getRequestedFor(urlEqualTo("/response/with/body"))
                .withHeader("testKey", equalTo("testValue")));
        Assertions.assertTrue(responseEquals(respones.body));
    }

    @Test
    void postWithResponseTest() {
        var response = http.post("/response/with/body", LoginResponseBody.class, LOGIN_REQUEST_BODY, TEST_HEADERS);
        verify(postRequestedFor(urlEqualTo("/response/with/body"))
                .withHeader("testKey", equalTo("testValue"))
                .withRequestBody(equalToJson(GSON.toJson(LOGIN_REQUEST_BODY))));
        Assertions.assertTrue(responseEquals(response.body));
    }

    static boolean responseEquals(LoginResponseBody a) {
        return a.successful == LOGIN_RESPONSE_BODY.successful
                && a.errorMessageTitle.equals(LOGIN_RESPONSE_BODY.errorMessageTitle);
    }

    @Test
    void communicationFailedTest() {
        Assertions.assertThrows(CommunicationFailed.class, () -> http.get("/fault"));
    }

}
