package mc.mian.templatemod.datagen;

import mc.mian.templatemod.common.block.TemplateBlocks;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TemplateStateAndModelProvider extends BlockStateProvider {
    public TemplateStateAndModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TemplateConstants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile crystal_block_model = cubeAll(TemplateBlocks.TEMPLATE_BLOCK.get());
        simpleBlockWithItem(TemplateBlocks.TEMPLATE_BLOCK.get(), crystal_block_model);
    }
}
