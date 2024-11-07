package mc.mian.limitedrespawns.neoforge;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.config.ConfigHolder;
import mc.mian.limitedrespawns.datagen.LRDataGenerators;
import mc.mian.limitedrespawns.util.LRConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(LRConstants.MOD_ID)
public class LimitedRespawnsNeoForge {
    public static IEventBus modEventBus;
    public static final IEventBus commonEventBus = NeoForge.EVENT_BUS;
    public LimitedRespawnsNeoForge(IEventBus modEventBusParam) {
        modEventBus = modEventBusParam;

        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
//        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        LimitedRespawns.config = ConfigHolder.SERVER;
        LimitedRespawns.init();

        modEventBus.register(LRDataGenerators.class);
    }
}