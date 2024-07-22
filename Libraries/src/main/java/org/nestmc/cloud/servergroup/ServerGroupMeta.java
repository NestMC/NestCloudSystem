package org.nestmc.cloud.servergroup;

import lombok.Data;
import org.nestmc.cloud.network.wrapper.Wrapper;

@Data
public class ServerGroupMeta {

    private final String name;
    private final int onlineAmount;
    private final int maxRam;
    private final Wrapper wrapper;
    private final boolean isStatic;
}
