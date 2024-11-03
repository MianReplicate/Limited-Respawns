package mc.mian.templatemod.neoforge;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import mc.mian.templatemod.TemplateMod;
import mc.mian.templatemod.config.ConfigHolder;
import mc.mian.templatemod.datagen.TemplateDataGenerators;
import mc.mian.templatemod.util.TemplateConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TemplateConstants.MOD_ID)
public class TemplateModNeoForge {
    public static IEventBus modEventBus;
    public static final IEventBus commonEventBus = NeoForge.EVENT_BUS;
    public TemplateModNeoForge(IEventBus modEventBusParam) {
        modEventBus = modEventBusParam;

        ForgeConfigRegistry.INSTANCE.register(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
//        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        TemplateMod.config = ConfigHolder.SERVER;
        TemplateMod.init();

        modEventBus.register(TemplateDataGenerators.class);
    }
}