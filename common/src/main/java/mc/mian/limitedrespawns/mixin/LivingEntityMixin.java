package mc.mian.limitedrespawns.mixin;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.ILRRetrieve;
import mc.mian.limitedrespawns.config.LRConfiguration;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILRRetrieve {
    @Shadow public abstract boolean isDeadOrDying();

    @Unique
    private LRData limitedRespawns$lrData;

    // Initialize our data class
    @Inject(at = @At("TAIL"), method = "<init>")
    public <T extends Entity> void init(EntityType<T> entityType, Level level, CallbackInfo ci){
        this.limitedRespawns$lrData = new LRData((LivingEntity)(Object)this);
    }

    // Add our data
    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addLRData(CompoundTag compound, CallbackInfo ci){
        this.limitedRespawns$lrData.writeToNbt(compound);
    }

    // Read our data
    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readLRData(CompoundTag compound, CallbackInfo ci){
        this.limitedRespawns$lrData.deserializeNBT(compound.getCompound(LRConstants.LIMITED_RESPAWNS_DATA.getPath()));
    }

    // Tick for player timer to give respawns
    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci){
        LRData lrData = this.limitedRespawns$lrData;
        if(!lrData.getLivingEntity().level().isClientSide() && lrData.getLivingEntity() instanceof ServerPlayer serverPlayer){
            boolean dead = lrData.getValue(LRConstants.DEAD);
            if(dead && lrData.hasEnoughRespawns()){
                TeleportTransition teleportTransition = serverPlayer.findRespawnPositionAndUseSpawnBlock(true, TeleportTransition.DO_NOTHING);
                serverPlayer.teleportTo(
                        teleportTransition.newLevel(),
                        teleportTransition.position().x,
                        teleportTransition.position().y,
                        teleportTransition.position().z,
                        Relative.ROTATION,
                        teleportTransition.yRot(),
                        teleportTransition.xRot(),
                        true);
                lrData.onRespawn();
            }

            if(this.isDeadOrDying() || (long) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS) < 0){
                lrData.setValue(LRConstants.GAIN_RESPAWN_TICK, 0L);
                return;
            }

            lrData.setValue(LRConstants.GAIN_RESPAWN_TICK, (long) lrData.getValue(LRConstants.GAIN_RESPAWN_TICK) + 1);

            if((long) lrData.getValue(LRConstants.GAIN_RESPAWN_TICK) >
                    (long) LimitedRespawns.config.getBasedOnDead(dead,
                            LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS)){
                lrData.setValue(LRConstants.GAIN_RESPAWN_TICK, 0L);
                lrData.setRespawns(this.limitedRespawns$getData().getRespawns() + (int) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.GIVE_AMOUNT_OF_RESPAWNS));
            }
        }
    }

    // Retrieve data when needed
    @Override
    public LRData limitedRespawns$getData() {
        return limitedRespawns$lrData;
    }
}
