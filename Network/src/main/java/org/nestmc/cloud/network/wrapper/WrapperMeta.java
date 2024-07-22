package org.nestmc.cloud.network.wrapper;

import lombok.Data;

import java.util.UUID;

@Data
public class WrapperMeta {

    private final UUID uuid;

    private final String host;

    private final String key;
}
