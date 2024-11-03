package mc.mian.templatemod.client;

import commonnetwork.api.Network;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.mian.templatemod.common.network.custom.TemplatePacket;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TemplateKeybinds {
    private static final ArrayList<KeyMapping> keys = new ArrayList<>();
    public static final KeyMapping TEMPLATE_KEY = registerKeybind(new KeyMapping("templatemod.keybinds.ping", GLFW.GLFW_KEY_G, TemplateConstants.MOD_DISPLAY_NAME));

    public static void tickKeybinds(){
        if(Minecraft.getInstance().level != null) {
            if(TEMPLATE_KEY.consumeClick()){
                Minecraft.getInstance().player.displayClientMessage(Component.literal("Client: ").withStyle(ChatFormatting.YELLOW).append(Component.literal("Ping!")), false);
                Network.getNetworkHandler().sendToServer(new TemplatePacket());
            }
        }
    }

    private static KeyMapping registerKeybind(KeyMapping keyMapping){
        keys.add(keyMapping);
        return keyMapping;
    }

    public static List<KeyMapping> getKeys(){
        return keys;
    }
}
