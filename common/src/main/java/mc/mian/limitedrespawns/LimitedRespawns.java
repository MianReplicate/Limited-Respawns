package mc.mian.limitedrespawns;

import mc.mian.limitedrespawns.common.block.TemplateBlocks;
import mc.mian.limitedrespawns.common.network.TemplateNetwork;
import mc.mian.limitedrespawns.common.tab.TemplateCreativeModeTabs;
import mc.mian.limitedrespawns.common.item.TemplateItems;
import mc.mian.limitedrespawns.common.sound.TemplateSoundEvents;
import mc.mian.limitedrespawns.config.LRConfiguration;
import mc.mian.limitedrespawns.util.LRConstants;

public class LimitedRespawns {
    public static LRConfiguration config;

    public static void init() {
        LRConstants.LOGGER.info("Meow? MEOW!!");
        TemplateCreativeModeTabs.TABS.register();
        TemplateBlocks.BLOCKS.register();
        TemplateItems.ITEMS.register();
        TemplateSoundEvents.SOUND_EVENTS.register();
        TemplateNetwork.register();
    }
}