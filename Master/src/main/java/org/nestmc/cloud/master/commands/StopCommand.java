package org.nestmc.cloud.master.commands;

import org.nestmc.cloud.command.Command;
import org.nestmc.cloud.master.Master;

@Command.CommandInfo(name = "stop", aliases = {"shutdown", "terminate"})
public class StopCommand implements Command {

    public boolean execute(final String[] args) {
        Master.getMaster().setRunning(false);
        return true;
    }
}
