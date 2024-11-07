package mc.mian.limitedrespawns.fabric;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import mc.mian.limitedrespawns.util.LRConstants;
import net.fabricmc.api.ModInitializer;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.config.ConfigHolder;
import net.neoforged.fml.config.ModConfig;

public class LimitedRespawnsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(LRConstants.MOD_ID, ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        LimitedRespawns.config = ConfigHolder.SERVER;
        LimitedRespawns.init();
    }
}