package mc.mian.limitedrespawns.forge;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.command.LRCommand;
import mc.mian.limitedrespawns.config.ConfigHolder;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(LRConstants.MOD_ID)
public class LimitedRespawnsForge {
    public LimitedRespawnsForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);

        LimitedRespawns.config = ConfigHolder.SERVER;
    }

    @Mod.EventBusSubscriber(modid = LRConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Common {
        @SubscribeEvent
        public static void OnCommandsRegister(final RegisterCommandsEvent event) {
            LRCommand.register(event.getDispatcher());
        }
    }
}