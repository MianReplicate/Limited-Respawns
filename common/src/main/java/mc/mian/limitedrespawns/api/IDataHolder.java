package mc.mian.limitedrespawns.api;

import mc.mian.limitedrespawns.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface IDataHolder extends Serializable<CompoundTag> {
    <T> T getValue(ResourceLocation key);
    <T> void setValue(ResourceLocation key, T value);
}
