package org.nestmc.cloud.wrapper.commands;

import org.nestmc.cloud.command.Command;
import org.nestmc.cloud.wrapper.Wrapper;

@Command.CommandInfo(name = "stop", aliases = {"shutdown", "terminate"})
public class StopCommand implements Command {

    public boolean execute(final String[] args) {
        Wrapper.getWrapper().setRunning(false);
        return true;
    }

}
