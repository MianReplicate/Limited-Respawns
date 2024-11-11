package mc.mian.limitedrespawns.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import mc.mian.limitedrespawns.command.LRCommand;
import mc.mian.limitedrespawns.util.LRConstants;
import net.fabricmc.api.ModInitializer;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.config.ConfigHolder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraftforge.fml.config.ModConfig;

public class LimitedRespawnsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(LRConstants.MOD_ID, ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        LimitedRespawns.config = ConfigHolder.SERVER;

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> LRCommand.register(commandDispatcher));
    }
}