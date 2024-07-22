package org.nestmc.cloud.master.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.nestmc.cloud.master.Master;

import org.nestmc.cloud.master.network.packets.WrapperKeyInPacket;

import org.nestmc.cloud.network.packet.Packet;
import org.nestmc.cloud.network.wrapper.Wrapper;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    public void channelActive(final ChannelHandlerContext ctx) {
        final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(Master.getMaster().getMasterNetworkHandler().getHostFromChannel(ctx.channel()));
        if (wrapper != null) {
            while (!wrapper.getQueue().isEmpty()) {
                wrapper.sendPacket(wrapper.getQueue().poll());
            }
        } else {
            Master.getMaster().getLogger().warn("Unknown wrapper!");
        }
    }

    protected void channelRead0(final ChannelHandlerContext ctx, final Packet packet) {
        final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(Master.getMaster().getMasterNetworkHandler().getHostFromChannel(ctx.channel()));
        if (wrapper != null) {
            if (wrapper.isVerified() || packet instanceof WrapperKeyInPacket) {
                final Packet response = packet.handle(ctx.channel());
                if (response != null) ctx.channel().writeAndFlush(response);
            }

        }
    }

    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        final Wrapper wrapper = Master.getMaster().getMasterNetworkHandler().getWrapperByHost(Master.getMaster().getMasterNetworkHandler().getHostFromChannel(ctx.channel()));
        wrapper.disconnect();
        Master.getMaster().getLogger().info("Wrapper from host " + wrapper.getWrapperMeta().getHost() + " is disconnected!");
        super.channelInactive(ctx);
    }

}
