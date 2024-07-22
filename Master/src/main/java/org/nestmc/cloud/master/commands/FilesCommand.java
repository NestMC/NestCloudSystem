package org.nestmc.cloud.master.commands;

import org.nestmc.cloud.command.Command;
import org.nestmc.cloud.master.Master;

@Command.CommandInfo(name="files", aliases={"f"})
public class FilesCommand implements Command{

    @Override
    public boolean execute(String[] args) {
        if(args[0].equalsIgnoreCase("list")){
            Master.getMaster().getLogger().info(Master.getMaster().getDocumentHandler().getFiles());
        }
        return true;
    }
}
