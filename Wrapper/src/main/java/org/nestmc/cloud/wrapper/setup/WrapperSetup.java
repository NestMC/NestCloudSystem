package org.nestmc.cloud.wrapper.setup;

import jline.console.ConsoleReader;
import org.nestmc.cloud.core.logging.Logger;
import org.nestmc.cloud.core.setup.Setup;
import org.nestmc.cloud.core.setup.requests.StringRequest;
import org.nestmc.cloud.wrapper.Wrapper;
import org.nestmc.cloud.wrapper.network.packets.WrapperKeyOutPacket;

import java.io.IOException;
import java.net.InetAddress;

public class WrapperSetup implements Setup {

    public void setup(final Logger logger, final ConsoleReader reader) throws IOException {
        if(Wrapper.getWrapper().getConfiguration().getMasterHost().isEmpty()){
            new StringRequest().request(logger, "Type the Master IP", reader, masterHost -> Wrapper.getWrapper().getConfiguration().setMasterHost(masterHost));
        }
        if (Wrapper.getWrapper().getConfiguration().getKey().isEmpty()) {
            new StringRequest().request(logger, "Type in the key, you received from the Master", reader, key -> Wrapper.getWrapper().getConfiguration().setKey(key));
            Wrapper.getWrapper().getNetworkHandler().sendPacketToMaster(new WrapperKeyOutPacket(Wrapper.getWrapper().getConfiguration().getKey()));
        }
    }

}
