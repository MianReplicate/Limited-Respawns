package mc.mian.limitedrespawns.datagen;

import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class LRLangProvider extends LanguageProvider {
    public static final String MOD_ID = LRConstants.MOD_ID;
    
    public LRLangProvider(PackOutput output) {
        super(output, MOD_ID, "en_us");
    }

    public void addPurgatoryMessage(String title, String translation){
        add("purgatory."+MOD_ID+"."+title, translation);
    }

    public void addChatMessage(String title, String translation){
        add("chat.message."+ MOD_ID+"."+title, translation);
    }

    @Override
    protected void addTranslations() {
        addChatMessage("respawns_changed", "You've %s some respawns: %s -> %s");
        addChatMessage("set_respawns", "Set %s's respawns to %s");
        addChatMessage("get_respawns", "%s has %s respawns");

        addChatMessage("time_to_get_respawns", "%s more respawn(s) required to respawn: %s respawn(s) will be given in %s minute(s)");
        addPurgatoryMessage("lost_respawns", "You have lost all your respawns, you are now permanently dead.");
    }
}
