package org.nestmc.cloud.master.network.packets;

import io.netty.buffer.ByteBufOutputStream;
import org.nestmc.cloud.network.packet.Packet;

import java.io.IOException;

public class WrapperKeyValidationOutPacket implements Packet {

    private boolean valid;

    public WrapperKeyValidationOutPacket(final boolean valid) {
        this.valid = valid;
    }

    public void write(final ByteBufOutputStream byteBuf) throws IOException {
        byteBuf.writeBoolean(this.valid);
    }
}