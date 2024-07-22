package org.nestmc.cloud.master.bootstrap;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.nestmc.cloud.core.exceptions.JavaVersionRequiredException;
import org.nestmc.cloud.master.Master;

import java.io.IOException;

public class MasterBootstrap {

    public static void main(final String[] args) throws JavaVersionRequiredException {

        // Verification de la version de Java
        if (Double.parseDouble(System.getProperty("java.class.version")) < 52) {
            throw new JavaVersionRequiredException();
        } else {
            new MasterBootstrap(args);
        }
    }
    // Mise en place des options du Terminal
    private MasterBootstrap(final String[] args) {
        final OptionParser optionParser = new OptionParser();

        optionParser.accepts("debug");
        optionParser.accepts("help");
        optionParser.accepts("version");

        final OptionSet optionSet = optionParser.parse(args);

        System.setProperty("jline.WindowsTerminal.directConsole", "false");

        try {
            // demarrage du Master
            new Master().bootstrap(optionSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
