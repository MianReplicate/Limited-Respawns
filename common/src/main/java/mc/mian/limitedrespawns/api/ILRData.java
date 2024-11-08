package mc.mian.limitedrespawns.api;

import mc.mian.limitedrespawns.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface ILRData extends IDataHolder {
    LivingEntity getLivingEntity();
    void setRespawns(int amount);
    int getRespawns();
    void onRespawn();
}
