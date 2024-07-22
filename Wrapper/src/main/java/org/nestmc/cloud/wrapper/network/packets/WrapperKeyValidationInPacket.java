package org.nestmc.cloud.wrapper.network.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import org.nestmc.cloud.network.packet.Packet;
import org.nestmc.cloud.network.packet.impl.SuccesPacket;
import org.nestmc.cloud.wrapper.Wrapper;

import java.io.IOException;

public class WrapperKeyValidationInPacket implements Packet {

    private boolean valid;

    public final Packet handle(final Channel channel) {
        if (this.valid) {
            Wrapper.getWrapper().getLogger().info("Wrapper key is valid!");
            return new SuccesPacket();
        } else {
            Wrapper.getWrapper().getLogger().info("Wrapper key is not valid!");
            Wrapper.getWrapper().shutdown();
            return null;
        }
    }

    public void read(final ByteBufInputStream byteBuf) throws IOException {
        this.valid = byteBuf.readBoolean();
    }
}
