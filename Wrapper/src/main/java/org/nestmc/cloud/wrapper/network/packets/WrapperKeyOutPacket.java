package org.nestmc.cloud.wrapper.network.packets;

import io.netty.buffer.ByteBufOutputStream;
import org.nestmc.cloud.network.packet.Packet;

import java.io.IOException;

public class WrapperKeyOutPacket implements Packet {

    private String key;

    public WrapperKeyOutPacket(final String key) {
        this.key = key;
    }

    public void write(final ByteBufOutputStream byteBuf) throws IOException {
        byteBuf.writeUTF(this.key);
    }
}