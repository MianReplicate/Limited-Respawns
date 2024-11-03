package mc.mian.templatemod.fabric;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import mc.mian.templatemod.util.TemplateConstants;
import mc.mian.templatemod.util.fabric.TemplatePlatformImpl;
import net.fabricmc.api.ModInitializer;
import mc.mian.templatemod.TemplateMod;
import mc.mian.templatemod.config.ConfigHolder;
import net.neoforged.fml.config.ModConfig;

public class TemplateModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(TemplateConstants.MOD_ID, ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        TemplateMod.config = ConfigHolder.SERVER;
        TemplatePlatformImpl.init();
        TemplateMod.init();
    }
}