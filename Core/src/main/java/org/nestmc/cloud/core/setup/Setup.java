package org.nestmc.cloud.core.setup;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;

import java.io.IOException;

public interface Setup {
    void setup(final Logger logger, final ConsoleReader reader) throws IOException;
}
