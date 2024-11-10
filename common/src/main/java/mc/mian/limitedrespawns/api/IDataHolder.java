package mc.mian.limitedrespawns.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface IDataHolder extends Serializable<CompoundTag> {
    <T> T getValue(ResourceLocation key);
    <T> void setValue(ResourceLocation key, T value);
    Collection<ResourceLocation> getKeys();
}
