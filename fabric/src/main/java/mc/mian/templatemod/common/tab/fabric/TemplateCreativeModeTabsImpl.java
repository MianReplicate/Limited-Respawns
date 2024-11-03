package mc.mian.templatemod.common.tab.fabric;

import mc.mian.templatemod.common.tab.TemplateCreativeModeTabs;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.util.TemplateConstants;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class TemplateCreativeModeTabsImpl {
    public static CreativeModeTab createTab(String title){
        return FabricItemGroup.builder()
                .icon(TemplateCreativeModeTabs::makeIcon)
                .title(Component.translatable("itemGroup."+ TemplateConstants.MOD_ID+"."+title))
                .displayItems((itemDisplayParameters, output) -> TemplateItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                .build();
    }
}
