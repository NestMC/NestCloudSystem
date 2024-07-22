package org.nestmc.cloud.wrapper;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.Setter;
import org.nestmc.cloud.command.CommandHandler;
import org.nestmc.cloud.core.CloudApplication;
import org.nestmc.cloud.core.document.DocumentHandler;
import org.nestmc.cloud.core.logging.LogLevel;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.network.NettyClient;
import org.nestmc.cloud.network.handler.PacketDecoder;
import org.nestmc.cloud.network.handler.PacketEncoder;
import org.nestmc.cloud.network.packet.impl.ErrorPacket;
import org.nestmc.cloud.network.packet.impl.SuccesPacket;
import org.nestmc.cloud.network.registry.PacketRegistry;
import org.nestmc.cloud.network.utils.ConnectableAddress;
import org.nestmc.cloud.wrapper.config.configuration.Configuration;
import org.nestmc.cloud.wrapper.network.NetworkHandler;
import org.nestmc.cloud.wrapper.network.handler.PacketHandler;
import org.nestmc.cloud.wrapper.network.packets.CreateGroupReceiver;
import org.nestmc.cloud.wrapper.network.packets.WrapperKeyOutPacket;
import org.nestmc.cloud.wrapper.network.packets.WrapperKeyValidationInPacket;
import org.nestmc.cloud.wrapper.setup.WrapperSetup;


import java.io.IOException;


public class Wrapper implements CloudApplication {

    @Getter
    private static Wrapper wrapper;

    @Getter
    private Logger logger;

    @Getter
    private NetworkHandler networkHandler = new NetworkHandler();

    private DocumentHandler documentHandler;

    private NettyClient nettyClient;

    @Setter
    @Getter
    private Configuration configuration = new Configuration();

    @Setter
    @Getter
    private boolean running = false;

    public void bootstrap(final OptionSet optionSet) throws IOException {
        wrapper = this;

        this.setRunning(true);

        this.logger = new Logger("", "NestCloudSystem Wrapper", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);

        this.printHeader("NestCloudSystem Wrapper", this.logger);

        this.documentHandler = new DocumentHandler("org.nestmc.cloud.wrapper.config");

        final ConsoleReader reader = new ConsoleReader();

        new WrapperSetup().setup(this.logger, reader);

        this.setupServer();

        final CommandHandler commandHandler = new CommandHandler("org.nestmc.cloud.wrapper.commands", this.logger);

        while (this.running) {
            try {
                commandHandler.executeCommand(reader.readLine(), this.logger);
            } catch (IOException e) {
                this.logger.error("Error while reading command!", e);
            }
        }

        reader.close();

        this.shutdown();
    }

    private void setupServer() {
        this.registerPackets();

        this.nettyClient = new NettyClient(new ConnectableAddress(this.configuration.getMasterHost(), this.configuration.getMasterPort())).withSSL().connect(() -> this.logger.info("Connected to Master!"), () -> {
            this.logger.warn("Master is currently not available!");
            this.shutdown();
        }, channel -> channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler()));
    }

    public void shutdown() {
        this.logger.info("NestCloudSystem Wrapper is stopping!");

        this.setRunning(false);

        this.documentHandler.saveFiles();

        this.nettyClient.disconnect(() -> this.logger.info("Wrapper is disconnected!"));

        System.exit(0);
    }

    private void registerPackets() {
        PacketRegistry.PacketDirection.IN.addPacket(0, SuccesPacket.class);
        PacketRegistry.PacketDirection.IN.addPacket(1, ErrorPacket.class);
        PacketRegistry.PacketDirection.IN.addPacket(202, CreateGroupReceiver.class);
        PacketRegistry.PacketDirection.IN.addPacket(201, WrapperKeyValidationInPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(0, SuccesPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(200, WrapperKeyOutPacket.class);
    }

}