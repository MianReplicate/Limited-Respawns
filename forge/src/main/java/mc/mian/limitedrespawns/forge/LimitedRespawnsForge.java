package mc.mian.limitedrespawns.forge;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.config.ConfigHolder;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LRConstants.MOD_ID)
public class LimitedRespawnsForge {
    public static final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    public static final IEventBus commonEventBus = MinecraftForge.EVENT_BUS;
    public LimitedRespawnsForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);

        LimitedRespawns.config = ConfigHolder.SERVER;
        LimitedRespawns.init();
    }
}