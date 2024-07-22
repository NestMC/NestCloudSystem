package org.nestmc.cloud.master.commands;

import org.nestmc.cloud.command.Command;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.master.Master;
import org.nestmc.cloud.master.network.packets.HelloTestPacket;
import org.nestmc.cloud.network.wrapper.Wrapper;

// creategroup <grouptype : proxy/server> <groupname> <wrapper> <onlineAmount -> server group> <ram> <static>
@Command.CommandInfo(name = "creategroup", aliases = "cg")
public class CreateGroupCommand implements Command {
    @Override
    public boolean execute(String[] args) {
        String type = args[0];
        String name = args[1];
        Logger logger = Master.getMaster().getLogger();


        if(type.equalsIgnoreCase("server")){
           // Master.getMaster().getLogger().info(Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[2]));
            if(Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[2])!= null){

               Master.getMaster().getMasterNetworkHandler().getWrapperByHost(args[2]).sendPacket(new HelloTestPacket("A little hello by the master !"));
            }else{
                logger.warn("Can't found wrapper !");
            }
        }
        if(type.equalsIgnoreCase("proxy")){
            int i = 1;
        }


        return false;
    }

    @Override
    public void printHelp() {

    }
}
