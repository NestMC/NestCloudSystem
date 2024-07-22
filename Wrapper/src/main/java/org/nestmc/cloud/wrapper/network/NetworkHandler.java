package org.nestmc.cloud.wrapper.network;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import org.nestmc.cloud.network.packet.Packet;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkHandler {

    @Setter
    private Channel masterChannel;

    @Getter
    private final ConcurrentLinkedQueue<Packet> queue = new ConcurrentLinkedQueue<>();

    public void sendPacketToMaster(final Packet packet) {
        if (this.isConnected()) {
            this.masterChannel.writeAndFlush(packet);
        } else {
            this.queue.offer(packet);
        }
    }

    private boolean isConnected() {
        return this.masterChannel != null && this.masterChannel.isActive() && this.masterChannel.isOpen();
    }

    public final String getHostFromChannel(final Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

}
