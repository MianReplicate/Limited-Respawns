package mc.mian.templatemod.common.tab.forge;

import mc.mian.templatemod.common.tab.TemplateCreativeModeTabs;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class TemplateCreativeModeTabsImpl {
    public static CreativeModeTab createTab(String title){
        return CreativeModeTab.builder()
                .icon(TemplateCreativeModeTabs::makeIcon)
                .title(Component.translatable("itemGroup."+ TemplateConstants.MOD_ID+"."+title))
                .displayItems((itemDisplayParameters, output) -> TemplateItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
                .build();
    }
}
