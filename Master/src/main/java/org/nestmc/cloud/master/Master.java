package org.nestmc.cloud.master;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import lombok.Getter;
import lombok.Setter;
import org.nestmc.cloud.command.CommandHandler;
import org.nestmc.cloud.core.CloudApplication;
import org.nestmc.cloud.core.document.DocumentHandler;
import org.nestmc.cloud.core.logging.LogLevel;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.master.network.handler.PacketHandler;
import org.nestmc.cloud.master.network.packets.HelloTestPacket;
import org.nestmc.cloud.master.network.packets.WrapperKeyInPacket;
import org.nestmc.cloud.master.network.packets.WrapperKeyValidationOutPacket;

import org.nestmc.cloud.master.setup.LoginSetup;
import org.nestmc.cloud.master.setup.MasterSetup;
import org.nestmc.cloud.network.NettyServer;
import org.nestmc.cloud.network.handler.PacketDecoder;
import org.nestmc.cloud.network.handler.PacketEncoder;
import org.nestmc.cloud.network.packet.impl.ErrorPacket;
import org.nestmc.cloud.network.packet.impl.SuccesPacket;
import org.nestmc.cloud.network.registry.PacketRegistry;
import org.nestmc.cloud.network.wrapper.MasterNetworkHandler;
import org.nestmc.cloud.network.wrapper.Wrapper;
import org.nestmc.cloud.security.user.CloudUserHandler;
import org.nestmc.cloud.servergroup.ServerGroupHandler;

import java.io.IOException;

public class Master implements CloudApplication {
    // déclaration de variables


    @Getter
    private static Master master;

    @Getter
    private Logger logger;

    @Getter
    private MasterNetworkHandler masterNetworkHandler;
    @Getter
    private ServerGroupHandler serverGroupHandler;

    @Getter
    private CloudUserHandler cloudUserHandler;

    private DocumentHandler documentHandler;

    private NettyServer nettyServer;

    @Setter
    @Getter
    private boolean running = false;

    // demarrage du Master
    public void bootstrap(final OptionSet optionSet) throws IOException{
        master = this;

        this.setRunning(true);

        // instanciation du Logger
        this.logger = new Logger("", "NestCloudSystem", optionSet.has("debug") ? LogLevel.DEBUG : LogLevel.INFO);
        // header du Terminal
        this.printHeader("NestCloudSystem", this.logger);
        // gestionnaire des wrappers
        this.masterNetworkHandler = new MasterNetworkHandler();
        // gestionnaire des groups
        this.serverGroupHandler = new ServerGroupHandler();
        // gestionnaire des utilisateurs
        this.cloudUserHandler = new CloudUserHandler();
        // gestionnaire des fichiers
        this.documentHandler = new DocumentHandler("org.nestmc.cloud.master.config");
        // lecteur du Terminal
        final ConsoleReader reader = new ConsoleReader();
        reader.setHistoryEnabled(false);
        // system de login
        new LoginSetup().setup(this.logger, reader);
        // connexion du master
        new MasterSetup().setup(this.logger, reader);

        this.setupServer(() -> this.logger.info("Server was successfully bound to port 1337"));
        // gestionnaire des commandes
        final CommandHandler commandHandler = new CommandHandler("org.nestmc.cloud.master.commands", this.logger);
        // tant que le master est en route on exexute les commandes lu sur le terminal
        while (this.running) {
            try {
                commandHandler.executeCommand(reader.readLine(), this.logger);
            } catch (IOException e) {
                this.logger.error("Error while reading command!", e);
            }
        }
        // fermeture du lecteur du terminal
        reader.close();
        // fermeture du master
        this.shutdown();
    }
    public DocumentHandler getDocumentHandler(){
        return this.documentHandler;
    }

    private void setupServer(final Runnable ready) {
        this.registerPackets();

        this.nettyServer = new NettyServer(1337).withSSL().bind(ready, channel -> {
            final String host = this.masterNetworkHandler.getHostFromChannel(channel);

            this.logger.info("HostFromChannel "+host);

            if (!this.masterNetworkHandler.isWhitelisted(host)) {
                channel.close().syncUninterruptibly();
                this.logger.warn("A not whitelisted Wrapper would like to connect to this master!");
                return;
            }

            channel.pipeline().addLast(new PacketEncoder()).addLast(new PacketDecoder()).addLast(new PacketHandler());
            final Wrapper wrapper = this.masterNetworkHandler.getWrapperByHost(host);
            if (wrapper != null) {
                wrapper.setChannel(channel);
                this.logger.info("Wrapper from " + wrapper.getWrapperMeta().getHost() + " connected!");
            }
        });
    }

    public void shutdown() {
        this.logger.info("NestCloudSystem is stopping!");
        // sauvegarde des fichiers
        this.documentHandler.saveFiles();

        this.masterNetworkHandler.getWrappers().stream().filter(Wrapper::isConnected).forEach(wrapper -> wrapper.getChannel().close().syncUninterruptibly());

        this.nettyServer.close(() -> logger.info("Netty server was closed!"));

        System.exit(0);
    }

    private void registerPackets() {
        PacketRegistry.PacketDirection.IN.addPacket(0, SuccesPacket.class);
        PacketRegistry.PacketDirection.IN.addPacket(1, ErrorPacket.class);

        PacketRegistry.PacketDirection.IN.addPacket(200, WrapperKeyInPacket.class);

        PacketRegistry.PacketDirection.OUT.addPacket(0, SuccesPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(1, ErrorPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(202, HelloTestPacket.class);
        PacketRegistry.PacketDirection.OUT.addPacket(201, WrapperKeyValidationOutPacket.class);
    }

}
