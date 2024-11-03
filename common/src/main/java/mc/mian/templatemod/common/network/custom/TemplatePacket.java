package mc.mian.templatemod.common.network.custom;

import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import mc.mian.templatemod.util.TemplateUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class TemplatePacket {
    public static final ResourceLocation CHANNEL = TemplateUtil.modLoc("template_packet");
    public static final StreamCodec<FriendlyByteBuf, TemplatePacket> STREAM_CODEC =
            StreamCodec.ofMember(TemplatePacket::encode, TemplatePacket::new);

    public static CustomPacketPayload.Type<CustomPacketPayload> type(){
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public TemplatePacket(){}

    public TemplatePacket(FriendlyByteBuf buf){

    }

    public void encode(FriendlyByteBuf buf){

    }

    public static void handle(PacketContext<TemplatePacket> ctx)
    {
        if (Side.CLIENT.equals(ctx.side()))
        {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Client: ").withStyle(ChatFormatting.YELLOW).append(Component.literal("Pogo!")), false);
        }
        else
        {
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Server: ").withStyle(ChatFormatting.BLUE).append(Component.literal("Pogo!")), false);
        }
    }
}
