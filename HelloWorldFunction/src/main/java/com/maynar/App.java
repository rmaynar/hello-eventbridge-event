package com.maynar;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maynar.model.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResultEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<BaseMessage, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    @Override
    public String handleRequest(BaseMessage mesage, Context context) {

        final String EVENT_DETAILS = "{\"message\":\"Hello world !\"}";
        LOGGER.info("Inside lambda handle request.....");

        EventBridgeClient eventBridgeClient =
                EventBridgeClient.builder()
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .build();

        ObjectMapper mapper = new ObjectMapper();
        String messageJson = "";
        try {
            messageJson = mapper.writeValueAsString(mesage);
        } catch (JsonProcessingException e) {
            messageJson = EVENT_DETAILS;
        }

        PutEventsRequestEntry requestEntry = PutEventsRequestEntry.builder()
                .resources("resource1", "resource2")
                .source("com.maynar.myapp")
                .detailType("sampleSubmitted")
                .detail(messageJson)
                .build();

        List<PutEventsRequestEntry> requestEntries = new ArrayList<>();
        requestEntries.add(requestEntry);

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
                .entries(requestEntries)
                .build();

        PutEventsResponse result = eventBridgeClient.putEvents(eventsRequest);

        LOGGER.info("Printing event Id result.....");
        for (PutEventsResultEntry resultEntry: result.entries()) {
            if (resultEntry.eventId() != null) {
                LOGGER.info("****** Event Id: " + resultEntry.eventId());
            } else {
                LOGGER.error("PutEvents failed with Error Code: " + resultEntry.errorCode());
            }
        }

        return "Lambda invoked sucessfully! -> input:" + mesage.toString();
    }
}
