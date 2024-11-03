package mc.mian.templatemod.datagen;

import mc.mian.templatemod.datagen.bootstrap.TemplateJukeboxSongsProvider;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class TemplateBootstrapProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.JUKEBOX_SONG, TemplateJukeboxSongsProvider::bootstrap);

    public TemplateBootstrapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(TemplateConstants.MOD_ID));
    }
}
