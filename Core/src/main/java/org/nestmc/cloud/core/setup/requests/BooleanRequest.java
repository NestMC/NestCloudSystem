package org.nestmc.cloud.core.setup.requests;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;

import java.io.IOException;

public class BooleanRequest {

    public void request(final Logger logger, final String request, final ConsoleReader reader, final Runnable runnable) throws IOException {
        logger.info(request + " Y/N");
        if (reader.readLine().equalsIgnoreCase("y")) {
            runnable.run();
        }
    }

}
