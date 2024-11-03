package mc.mian.templatemod;

import mc.mian.templatemod.common.block.TemplateBlocks;
import mc.mian.templatemod.common.network.TemplateNetwork;
import mc.mian.templatemod.common.tab.TemplateCreativeModeTabs;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.common.sound.TemplateSoundEvents;
import mc.mian.templatemod.config.TemplateConfiguration;
import mc.mian.templatemod.util.TemplateConstants;

public class TemplateMod {
    public static TemplateConfiguration config;

    public static void init() {
        TemplateConstants.LOGGER.info("Meow? MEOW!!");
        TemplateCreativeModeTabs.TABS.register();
        TemplateBlocks.BLOCKS.register();
        TemplateItems.ITEMS.register();
        TemplateSoundEvents.SOUND_EVENTS.register();
        TemplateNetwork.register();
    }
}