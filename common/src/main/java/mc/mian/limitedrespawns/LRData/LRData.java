package mc.mian.limitedrespawns.LRData;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.mian.limitedrespawns.api.ILRData;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class LRData implements ILRData {
    private final LivingEntity livingEntity;
    public LRData(LivingEntity livingEntity){
        this.livingEntity = livingEntity;
    }

    @ExpectPlatform
    public static Optional<LRData> get(LivingEntity livingEntity){
        throw new AssertionError("i love women");
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public <T> T getValue(ResourceLocation key) {
        return null;
    }

    @Override
    public <T> void setValue(ResourceLocation key, T value) {

    }

    @Override
    public void onChange() {
        if(!this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }
        int respawns = this.getValue(LRConstants.RESPAWNS);
        if(respawns <= 0){

        }
    }

    @Override
    public void reduceRespawn(int amount) {
        if(this.livingEntity.level().isClientSide()){
           throw new RuntimeException("who tf is running this on the client");
        }

        this.setValue(LRConstants.RESPAWNS, (int) getValue(LRConstants.RESPAWNS) - amount);
        this.onChange();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(LRConstants.RESPAWNS.getPath(), getValue(LRConstants.RESPAWNS));
        tag.putLong(LRConstants.TIME_OF_DEATH.getPath(), getValue(LRConstants.TIME_OF_DEATH));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        setValue(LRConstants.RESPAWNS, tag.getInt(LRConstants.RESPAWNS.getPath()));
        setValue(LRConstants.TIME_OF_DEATH, tag.getLong(LRConstants.TIME_OF_DEATH.getPath()));
    }
}
