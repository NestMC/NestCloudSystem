package org.nestmc.cloud.master.commands;

import org.nestmc.cloud.command.Command;
import org.nestmc.cloud.core.document.DocumentHandler;
import org.nestmc.cloud.master.Master;
import org.nestmc.cloud.master.config.ServerGroupConfig;
import org.nestmc.cloud.network.wrapper.Wrapper;
import org.nestmc.cloud.network.wrapper.WrapperMeta;


import java.util.UUID;

@Command.CommandInfo(name = "wrapper", aliases = {"w"})
public class WrapperCommand implements Command {

    public boolean execute(final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Master.getMaster().getLogger().info("<-- Wrapper -->");
                Master.getMaster().getMasterNetworkHandler().getWrappers().forEach(wrapper -> Master.getMaster().getLogger().info("Wrapper on host " + wrapper.getWrapperMeta().getHost() + " with unique id " + wrapper.getWrapperMeta().getUuid().toString() + (wrapper.isConnected() ? " is connected" : " is not connected")));
                Master.getMaster().getLogger().info("");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[1]);
                if (wrapper == null) {
                    Master.getMaster().getLogger().warn("Wrapper not available!");
                } else {
                    Master.getMaster().getLogger().info("Wrapper with unique id " + wrapper.getWrapperMeta().getUuid().toString() + " is " + (wrapper.isConnected() ? "connected" : "not connected"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("create")) {
                if (Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[1]) != null) {
                    Master.getMaster().getLogger().warn("Wrapper already exists!");
                    return true;
                }

                final String key = Master.getMaster().getMasterNetworkHandler().generateWrapperKey();
                final Wrapper wrapper = new Wrapper(new WrapperMeta(UUID.randomUUID(), args[1], key));
                Master.getMaster().getMasterNetworkHandler().addWrapper(wrapper);
                Master.getMaster().getLogger().info("Added wrapper on host " + wrapper.getWrapperMeta().getHost() + " with unique id " + wrapper.getWrapperMeta().getUuid().toString());
                Master.getMaster().getLogger().info("Key for wrapper is: " + key);

                return true;
            } else if (args[0].equalsIgnoreCase("delete")) {
                final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[1]);
                if (wrapper == null) {
                    Master.getMaster().getLogger().warn("Wrapper is not created yet!");
                    return true;
                }
                Master.getMaster().getMasterNetworkHandler().removeWrapper(wrapper);
                Master.getMaster().getLogger().info("Removed wrapper with host " + wrapper.getWrapperMeta().getHost() + "!");
                return true;
            }
        }
        return false;
    }

    public void printHelp() {
        Master.getMaster().getLogger().info("wrapper list");
        Master.getMaster().getLogger().info("wrapper info <host>");
        Master.getMaster().getLogger().info("wrapper create <host>");
        Master.getMaster().getLogger().info("wrapper delete <host>");
    }

}
