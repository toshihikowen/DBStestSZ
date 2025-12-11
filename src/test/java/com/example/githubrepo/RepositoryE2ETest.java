package com.example.githubrepo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RepositoryE2ETest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    static WireMockServer wireMockServer;

    /**
     * Starts WireMock server before all tests
     */
    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }


    /**
     * Stops WireMock server after all tests
     */
    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    /**
     * Sets up stub for GitHub API
     */
    @BeforeEach
    void setupStub() {
        wireMockServer.resetAll();
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"full_name\": \"octocat/Hello-World\",\n" +
                                "  \"description\": \"This your first repo!\",\n" +
                                "  \"clone_url\": \"https://github.com/octocat/Hello-World.git\",\n" +
                                "  \"stargazers_count\": 42,\n" +
                                "  \"created_at\": \"2011-01-26T19:01:12Z\"\n" +
                                "}")));
    }

    /**
     * Tests that the repository is retrieved and cached
     */
    @Test
    void getsRepositoryAndCaches() {
        String url = "http://localhost:" + port + "/repositories/octocat/Hello-World";
        ResponseEntity<Map> first = restTemplate.getForEntity(url, Map.class);
        assertEquals(200, first.getStatusCode().value());
        assertEquals("octocat/Hello-World", first.getBody().get("fullName"));
        assertEquals("This your first repo!", first.getBody().get("description"));
        assertEquals("https://github.com/octocat/Hello-World.git", first.getBody().get("cloneUrl"));
        assertEquals(42, first.getBody().get("stars"));
        assertEquals("2011-01-26T19:01:12Z", first.getBody().get("createdAt"));

        wireMockServer.resetRequests();

        ResponseEntity<Map> second = restTemplate.getForEntity(url, Map.class);
        assertEquals(200, second.getStatusCode().value());
        assertEquals("octocat/Hello-World", second.getBody().get("fullName"));
    }

    /**
     * Tests that a 404 is returned for a missing repository
     */
    @Test
    void returns404ForMissingRepo() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/unknown/missing"))
                .willReturn(WireMock.aResponse().withStatus(404)));
        String url = "http://localhost:" + port + "/repositories/unknown/missing";
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
        assertEquals(404, resp.getStatusCode().value());
    }
}
