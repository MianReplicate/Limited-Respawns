package mc.mian.templatemod.datagen;

import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TemplateeItemModelProvider extends ItemModelProvider {
    public TemplateeItemModelProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, TemplateConstants.MOD_ID, efh);
    }

    @Override
    protected void registerModels() {
        basicItem(TemplateItems.TEMPLATE_ITEM.get());
    }
}
