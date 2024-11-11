package mc.mian.limitedrespawns.api;

import net.minecraft.world.entity.LivingEntity;

public interface ILRData extends IDataHolder {
    LivingEntity getLivingEntity();
    void setRespawns(int amount, boolean announce);
    int getRespawns();
    void onRespawn();
    boolean hasEnoughRespawns();
    boolean hasEnoughRespawns(int amount);
    void init();
}
