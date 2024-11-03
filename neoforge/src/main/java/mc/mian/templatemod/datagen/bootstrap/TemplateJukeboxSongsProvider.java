package mc.mian.templatemod.datagen.bootstrap;

import mc.mian.templatemod.common.sound.TemplateSoundEvents;
import mc.mian.templatemod.registry.RegistrySupplierHolder;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class TemplateJukeboxSongsProvider {
    private static void register(
            BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, RegistrySupplierHolder<SoundEvent, SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput
    ) {
        context.register(
                key, new JukeboxSong(soundEvent, Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), (float)lengthInSeconds, comparatorOutput)
        );
    }

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, TemplateConstants.TEMPLATE_JUKEBOX_SONG, TemplateSoundEvents.TEMPLATE_SOUND_EVENT, 156, 11);
    }
}
