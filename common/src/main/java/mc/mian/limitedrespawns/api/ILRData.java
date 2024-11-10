package mc.mian.limitedrespawns.api;

import net.minecraft.world.entity.LivingEntity;

public interface ILRData extends IDataHolder {
    LivingEntity getLivingEntity();
    void setRespawns(int amount);
    int getRespawns();
    void onRespawn();
}
