package mc.mian.templatemod.common.tab;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.registry.DeferredRegistry;
import mc.mian.templatemod.registry.RegistrySupplier;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class TemplateCreativeModeTabs {
    public static final DeferredRegistry<CreativeModeTab> TABS = DeferredRegistry.create(TemplateConstants.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> TEMPLATE_MOD = TABS.register("template_mod", () -> createTab("template_mod"));
    public static ItemStack makeIcon() {
        return new ItemStack(TemplateItems.TEMPLATE_ITEM.get());
    }
    @ExpectPlatform
    public static CreativeModeTab createTab(String title){
        throw new RuntimeException("fuck off");
    }
}
