package org.nestmc.cloud.security.user;

import lombok.Data;

import java.util.UUID;

@Data
public class CloudUser {

    private final String name;

    private final UUID uuid;

    private final String hash;

}
