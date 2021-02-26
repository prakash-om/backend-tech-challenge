package net.gini.challenge.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.http.HttpHeader.httpHeader;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import wiremock.org.apache.commons.lang3.RandomStringUtils;

/**
 * Mock of client's very expensive API. No need to modify anything here
 */
@Component
@Profile("mock")
public class ExpensiveClientAPIMock {

    private static final Logger LOG = LoggerFactory.getLogger(ExpensiveClientAPIMock.class);
    private static final String PROCESSING_STATUS_INCOMPLETE = "INCOMPLETE";
    private static final String PROCESSING_STATUS_OK = "OK";

    private final WireMockServer wireMockServer;
    private static final Random RANDOM = new SecureRandom();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ExpensiveClientAPIMock(@Value("${client.api.port}") int port) {
        this.wireMockServer = new WireMockServer(wireMockConfig().port(port).extensions(new ClientApiResponseTransformer()));
    }

    @PostConstruct
    public void init() {
        wireMockServer.start();
        wireMockServer.stubFor(any(anyUrl()).willReturn(aResponse()));
    }

    @PreDestroy
    public void finish() {
        wireMockServer.stop();
    }

    private class ClientApiResponseTransformer extends ResponseTransformer {

        @Override
        public String getName() {
            return "ClientApiResponseTransformer";
        }

        @Override
        public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
            String serialNumber = RandomStringUtils.randomAlphanumeric(16);
            String processingStatus = getHeavyProcessingStatus();
            LOG.debug("Returning a document with serial number {} and processing status {}", serialNumber, processingStatus);
            return Response.Builder.like(response)
                                   .but().status(200)
                                   .body(String.format("{ \"PROCESSING_STATUS\": \"%s\", \"INVOICE_ID\": \"%s\" }", processingStatus, serialNumber))
                                   .headers(response.getHeaders().plus(httpHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)))
                                   .build();
        }

        private String getHeavyProcessingStatus() {
            try {
                // Client has only 2 threads to handle the load
                return executorService.submit(this::doHeavyProcessing).get(4, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                return PROCESSING_STATUS_INCOMPLETE;
            }
        }

        private String doHeavyProcessing() {
            try {
                // Client does some bad logic here
                Thread.sleep(RANDOM.nextInt(6000));
                return RANDOM.nextBoolean() ? PROCESSING_STATUS_OK : PROCESSING_STATUS_INCOMPLETE;
            } catch (InterruptedException e) {
                return PROCESSING_STATUS_INCOMPLETE;
            }
        }
    }

}
