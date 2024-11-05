package mc.mian.limitedrespawns.api;

import mc.mian.limitedrespawns.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface ILRData extends Serializable<CompoundTag> {
    LivingEntity getLivingEntity();
    <T> T getValue(ResourceLocation key);
    <T> void setValue(ResourceLocation key, T value);
    void onChange();
    void reduceRespawn(int amount);
}
