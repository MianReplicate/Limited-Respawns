package mc.mian.templatemod.common.network;

import commonnetwork.api.Network;
import mc.mian.templatemod.common.network.custom.TemplatePacket;

public class TemplateNetwork {
    public static void register(){
        Network.registerPacket(
                TemplatePacket.type(),
                TemplatePacket.class,
                TemplatePacket.STREAM_CODEC,
                TemplatePacket::handle
        );
    }
}
