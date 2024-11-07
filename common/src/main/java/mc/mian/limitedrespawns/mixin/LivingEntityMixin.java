package mc.mian.limitedrespawns.mixin;

import mc.mian.limitedrespawns.api.ILRRetrieve;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILRRetrieve {
    @Unique
    private LRData limitedRespawns$lrData;

    @Inject(at = @At("TAIL"), method = "<init>")
    public <T extends Entity> void init(EntityType<T> entityType, Level level, CallbackInfo ci){
        this.limitedRespawns$lrData = new LRData((LivingEntity)(Object)this);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    public void addLRData(CompoundTag compound, CallbackInfo ci){
        this.limitedRespawns$lrData.writeToNbt(compound);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    public void readLRData(CompoundTag compound, CallbackInfo ci){
        this.limitedRespawns$lrData.deserializeNBT(compound.getCompound(LRConstants.LIMITED_RESPAWNS_DATA.getPath()));
    }

    @Override
    public LRData limitedRespawns$getData() {
        return limitedRespawns$lrData;
    }
}
