package mc.mian.templatemod.forge.event;

import mc.mian.templatemod.client.TemplateKeybinds;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TemplateModClientEvents {
    @net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = TemplateConstants.MOD_ID, bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class Common{
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent.Pre tickEvent){
            TemplateKeybinds.tickKeybinds();
        }
    }

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = TemplateConstants.MOD_ID, bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Mod{
        @SubscribeEvent
        public static void registerKeybinds(RegisterKeyMappingsEvent event){
            TemplateKeybinds.getKeys().forEach(event::register);
        }
    }
}
