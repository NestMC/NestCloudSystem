package org.nestmc.cloud.master.setup;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.core.setup.Setup;
import org.nestmc.cloud.core.setup.requests.StringRequest;
import org.nestmc.cloud.master.Master;
import org.nestmc.cloud.security.Hashing;
import org.nestmc.cloud.security.user.CloudUser;

import java.io.IOException;
import java.util.UUID;

public class LoginSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if (Master.getMaster().getCloudUserHandler().getCloudUsers().isEmpty()) {
            logger.info("There is currently no user created. Creating the first user -> ");
            new StringRequest().request(logger, "Please enter a name for the setup user:", reader, name -> {
                if (name.equalsIgnoreCase("exit")) {
                    logger.info("\"exit\" is an invalid username. Exiting...");
                    Master.getMaster().shutdown();
                } else {
                    try {
                        new StringRequest().request(logger, "Please enter the password for the setup user:", reader, input -> Master.getMaster().getCloudUserHandler().getCloudUsers().add(new CloudUser(name, UUID.randomUUID(), Hashing.hash(input))));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            this.login(logger, reader);
        }
    }

    private void login(final Logger logger, final ConsoleReader reader) throws IOException {
        logger.info("Welcome to the login!");
        new StringRequest().request(logger, "Please enter your username:", reader, name -> {
            if (name.equalsIgnoreCase("exit")) {
                Master.getMaster().shutdown();
            } else {
                final CloudUser cloudUser = Master.getMaster().getCloudUserHandler().findCloudUserByName(name);
                if (cloudUser == null) {
                    try {
                        this.login(logger, reader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        new StringRequest().request(logger,"Password:", reader, password -> {
                            if (Hashing.verify(password, cloudUser.getHash())) {
                                logger.info("You successfully logged in!");
                            } else {
                                try {
                                    this.login(logger, reader);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}