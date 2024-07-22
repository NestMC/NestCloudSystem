package org.nestmc.cloud.master.network.packets;

import io.netty.buffer.ByteBufOutputStream;
import org.nestmc.cloud.network.packet.Packet;

import java.io.IOException;

public class HelloTestPacket implements Packet {
    private String hello="test hello";
    public HelloTestPacket(final String hello){this.hello = hello;}
    public void write(final ByteBufOutputStream byteBuf) throws IOException{
        byteBuf.writeUTF(this.hello);
    }



}
