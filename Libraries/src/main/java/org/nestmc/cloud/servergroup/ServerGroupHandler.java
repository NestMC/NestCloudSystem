package org.nestmc.cloud.servergroup;


import lombok.Getter;

import java.util.ArrayList;

public class ServerGroupHandler {
    @Getter
    private final ArrayList<ServerGroup> serverGroups = new ArrayList<>();
    public final ServerGroup getServerGroup(final String name){

        return this.serverGroups.stream().filter(serverGroup -> serverGroup.getServerGroupMeta().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    void addServerGroup(final ServerGroup serverGroup){ this.serverGroups.add(serverGroup);}
    void removeServerGroup(final ServerGroup serverGroup){ this.serverGroups.remove(serverGroup);}
    public final void createServerGroups(final ArrayList<ServerGroupMeta> serverGroupMetas){
        serverGroupMetas.forEach(serverGroupMeta -> this.addServerGroup(new ServerGroup(serverGroupMeta)));
    }
    public final ArrayList<ServerGroup> getServerGroups(){return serverGroups;}
    public final ArrayList<ServerGroupMeta> getServerGroupMetas(){
        final ArrayList<ServerGroupMeta> metas = new ArrayList<>();
        this.serverGroups.forEach(serverGroup -> metas.add(serverGroup.getServerGroupMeta()));
        return metas;
    }


}
