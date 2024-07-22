package org.nestmc.cloud.wrapper.config.configuration;

import lombok.Data;

@Data
public class Configuration {

    private String masterHost = "";

    private String key = "";

    private int masterPort = 1337;

}
