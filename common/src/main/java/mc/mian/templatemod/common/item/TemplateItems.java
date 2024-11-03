package mc.mian.templatemod.common.item;

import mc.mian.templatemod.registry.DeferredRegistry;
import mc.mian.templatemod.registry.RegistrySupplier;
import mc.mian.templatemod.util.TemplateConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;
import java.util.function.Supplier;

public class TemplateItems {
    public static final DeferredRegistry<Item> ITEMS = DeferredRegistry.create(TemplateConstants.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> TEMPLATE_ITEM = registerItem("template_item", (properties) -> new Item(properties
            .stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(TemplateConstants.TEMPLATE_JUKEBOX_SONG)));

    public static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> itemFunc){
        return ITEMS.register(name, () -> itemFunc.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(TemplateConstants.MOD_ID, name)))));
    }
}
