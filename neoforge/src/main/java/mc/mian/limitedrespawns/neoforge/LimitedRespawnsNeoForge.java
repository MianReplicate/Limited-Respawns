package mc.mian.limitedrespawns.neoforge;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.command.LRCommand;
import mc.mian.limitedrespawns.config.ConfigHolder;
import mc.mian.limitedrespawns.datagen.LRDataGenerators;
import mc.mian.limitedrespawns.util.LRConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(LRConstants.MOD_ID)
public class LimitedRespawnsNeoForge {
    public LimitedRespawnsNeoForge(IEventBus modEventBusParam) {
        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        LimitedRespawns.config = ConfigHolder.SERVER;

        modEventBusParam.register(LRDataGenerators.class);
    }

    @EventBusSubscriber(modid = LRConstants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class Common {
        @SubscribeEvent
        public static void OnCommandsRegister(final RegisterCommandsEvent event) {
            LRCommand.register(event.getDispatcher());
        }
    }
}