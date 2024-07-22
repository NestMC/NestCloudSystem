package org.nestmc.cloud.master.network.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import org.nestmc.cloud.master.Master;

import org.nestmc.cloud.network.packet.Packet;
import org.nestmc.cloud.network.wrapper.Wrapper;

import java.io.IOException;

public class WrapperKeyInPacket implements Packet {

    private String key;

    public final Packet handle(final Channel channel) {
        final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(Master.getMaster().getMasterNetworkHandler().getHostFromChannel(channel));
        if (wrapper != null) {
            if (wrapper.getWrapperMeta().getKey().equals(this.key)) {
                //SUCCESS
                wrapper.setVerified(true);
                return null;
            }
        }
        return null;
    }

    public void read(final ByteBufInputStream byteBuf) throws IOException {
        this.key = byteBuf.readUTF();
    }
}
