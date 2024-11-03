package mc.mian.templatemod.common.block;

import mc.mian.templatemod.common.block.custom.MeowingBlock;
import mc.mian.templatemod.common.item.TemplateItems;
import mc.mian.templatemod.registry.DeferredRegistry;
import mc.mian.templatemod.registry.RegistrySupplier;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class TemplateBlocks {
    public static final DeferredRegistry<Block> BLOCKS = DeferredRegistry.create(TemplateConstants.MOD_ID, Registries.BLOCK);

    public static final RegistrySupplier<Block> TEMPLATE_BLOCK = registerBlock("template_block", MeowingBlock::new, true);

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, Block> blockFunction, boolean registerItem) {
        RegistrySupplier<T> toReturn = (RegistrySupplier<T>) BLOCKS.register(name, () -> blockFunction.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(TemplateConstants.MOD_ID, name)))));
        if (registerItem) {
            registerBlockItem(name, toReturn);
        }
        return toReturn;
    }

    private static <T extends Block> RegistrySupplier<Item> registerBlockItem(String name, RegistrySupplier<T> block) {
        return TemplateItems.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }
}
