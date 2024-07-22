package org.nestmc.cloud.core;

import joptsimple.OptionSet;
import org.nestmc.cloud.core.logging.Logger;

import java.io.IOException;

public interface CloudApplication {

    void bootstrap(final OptionSet optionSet) throws IOException;

    void shutdown();

    default void printHeader(final String module, final Logger logger) {
        logger.info("   _____ _                 _       ");
        logger.info("  / ____| |               | |     ");
        logger.info(" | |    | | ___  _   _  __| |      ");
        logger.info(" | |    | |/ _ \\| | | |/ _` |  ");
        logger.info(" | |____| | (_) | |_| | (_| |      ");
        logger.info("  \\_____|_|\\___/ \\__,_|\\__,_|");

        logger.info("                                ");

        this.sleep(200);

        logger.info("");

        this.sleep(200);

        logger.info("Starting " + module + "!");
    }

    default void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
