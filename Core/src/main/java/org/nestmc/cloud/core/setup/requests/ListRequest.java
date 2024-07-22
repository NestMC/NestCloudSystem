package org.nestmc.cloud.core.setup.requests;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class ListRequest {

    public void request(final Logger logger, final String request, final String[] responses, final ConsoleReader reader, final Consumer<String> accept) throws IOException {
        logger.info(request + " " + this.getResponseString(responses));
        final String line = reader.readLine();
        if (this.contains(responses, line)) {
            accept.accept(line.toUpperCase());
        } else {
            this.request(logger, request, responses, reader, accept);
        }
    }

    private String getResponseString(final String[] responses) {
        final StringBuilder responseBuilder = new StringBuilder();
        for (final String response : responses) {
            responseBuilder.append((responseBuilder.length() == 0) ? response : ", " + response);
        }
        return responseBuilder.toString();
    }

    private boolean contains(final String[] responses, final String response) {
        return Arrays.stream(responses).anyMatch(s -> s.equalsIgnoreCase(response));
    }

}
