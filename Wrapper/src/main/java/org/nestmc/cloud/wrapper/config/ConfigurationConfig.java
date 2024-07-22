package org.nestmc.cloud.wrapper.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.nestmc.cloud.core.document.DocumentFile;
import org.nestmc.cloud.wrapper.Wrapper;
import org.nestmc.cloud.wrapper.config.configuration.Configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigurationConfig extends DocumentFile {

    public ConfigurationConfig() {
        super(new File("Wrapper//config//configuration.json"));
    }

    protected void load() throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(this.file.toPath())){
            Wrapper.getWrapper().setConfiguration(new Gson().fromJson(reader, Configuration.class));
        }
    }

    protected void save() throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(this.file.toPath())){
            writer.write(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(Wrapper.getWrapper().getConfiguration()));
        }
    }
}