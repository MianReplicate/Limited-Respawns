package mc.mian.templatemod.datagen;

import mc.mian.templatemod.common.block.TemplateBlocks;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TemplateLangProvider extends LanguageProvider {
    public static final String MOD_ID = TemplateConstants.MOD_ID;
    
    public TemplateLangProvider(PackOutput output) {
        super(output, MOD_ID, "en_us");
    }

    public void addAdvancement(ResourceLocation advancementLocation, String title, String desc){
        add("advancement."+MOD_ID+":"+advancementLocation.getPath(), title);
        add("advancement."+MOD_ID+":"+advancementLocation.getPath()+".desc", desc);
    }

    public void addGuiMessage(String title, String translation){
        add("gui."+ MOD_ID+"."+title, translation);
    }

    public void addChatMessage(String title, String translation){
        add("chat.message."+ MOD_ID+"."+title, translation);
    }

    public void addDisconnectionReason(String title, String translation){
        add("disconnect."+ MOD_ID+"."+title, translation);
    }

    public void addConfigOption(String title, String tileTranslation, String tipTranslation){
     add(title, tileTranslation);
     add(title+".tooltip", tipTranslation);
    }
    
    public void addConfigSection(String title, String titleTranslation, String commentTranslation){
        add(MOD_ID + ".configuration."+title, titleTranslation);
        add(MOD_ID + ".configuration."+title+".button", "Edit");
        add(MOD_ID + ".configuration."+title+".tooltip", commentTranslation);
    }

    public void addJukeboxSong(ResourceKey key, String translation){
        add(Util.makeDescriptionId("jukebox_song", key.location()), translation);
    }

    public void addItemGroup(String title, String translation){
        add("itemGroup."+ TemplateConstants.MOD_ID+"."+title, translation);
    }

    public void addSound(String title, String translation){
        add(TemplateConstants.MOD_ID+".sounds."+title, translation);
    }

    public void addKeybind(String title, String translation){
        add(TemplateConstants.MOD_ID+".keybinds."+title, translation);
    }

    @Override
    protected void addTranslations() {
        addConfigOption("meow", "meow", "meow?");
        addConfigSection("General Settings", "General Settings", "This category holds general values that most people will want to change.");
        addBlock(TemplateBlocks.TEMPLATE_BLOCK, "Meowing Block");
        addItem(TemplateItems.TEMPLATE_ITEM, "Meowing Disc");
        addJukeboxSong(TemplateConstants.TEMPLATE_JUKEBOX_SONG, "Tanger - Scratch!");
        addKeybind("ping", "Ping");

        addSound("fail", "no meow :<");
        addSound("meow", "MEOW!!");

        addItemGroup("template_mod", "Template Mod");
    }
}
