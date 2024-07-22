package org.nestmc.cloud.wrapper.network.packets;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import org.nestmc.cloud.network.packet.Packet;
import org.nestmc.cloud.network.packet.impl.SuccesPacket;
import org.nestmc.cloud.wrapper.Wrapper;

import java.io.IOException;


public class CreateGroupReceiver implements Packet{
    private String string;
    public final Packet handle(final Channel channel){
        if(this.string!=null || !(this.string.equalsIgnoreCase(""))){
            Wrapper.getWrapper().getLogger().info(this.string);
            return new SuccesPacket();
        }else{
            return null;
        }
    }
    public void read(final ByteBufInputStream byteBuf) throws IOException{
        this.string = byteBuf.readUTF();
    }
}
