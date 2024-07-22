package org.nestmc.cloud.master.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.nestmc.cloud.core.document.DocumentFile;
import org.nestmc.cloud.master.Master;
import org.nestmc.cloud.security.user.CloudUser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class CloudUserConfig extends DocumentFile {

    public CloudUserConfig() {
        super(new File("Master//config//user.json"));
    }

    protected void load() throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(this.file.toPath())){
            Master.getMaster().getCloudUserHandler().setCloudUsers(new Gson().fromJson(reader, new TypeToken<ArrayList<CloudUser>>(){}.getType()));
        }
    }

    protected void save() throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(this.file.toPath())) {
            final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            writer.write(gson.toJson(Master.getMaster().getCloudUserHandler().getCloudUsers()));
        }
    }
}
