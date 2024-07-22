package org.nestmc.cloud.master.setup;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.core.setup.Setup;
import org.nestmc.cloud.master.Master;

import java.io.IOException;

public class MasterSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Master.getMaster().getMasterNetworkHandler().getWrapperMetas().isEmpty()) {
            logger.info("To create a wrapper use the following command: \"wrapper create <host>\"!");
        }
    }

}
