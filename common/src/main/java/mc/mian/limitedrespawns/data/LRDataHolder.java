package mc.mian.limitedrespawns.data;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.IDataHolder;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;

public class LRDataHolder implements IDataHolder {
    protected final HashMap<ResourceLocation, Object> dataMap = new HashMap<>();

    protected LRDataHolder(){
        this.dataMap.putIfAbsent(LRConstants.RESPAWNS, LimitedRespawns.config.startingRespawns.get());
        this.dataMap.putIfAbsent(LRConstants.GAIN_RESPAWN_TICK, 0L);
        this.dataMap.putIfAbsent(LRConstants.DIED, false);
        this.dataMap.putIfAbsent(LRConstants.CAUSE_OF_DEATH, CODEnums.NONE);
    }

    public static LRDataHolder from(CompoundTag tag){
        LRDataHolder dataHolder = new LRDataHolder();
        dataHolder.deserializeNBT(tag);
        return dataHolder;
    }

    @Override
    public <T> T getValue(ResourceLocation key) {
        return (T) this.dataMap.get(key);
    }

    @Override
    public <T> void setValue(ResourceLocation key, T value) {
        this.dataMap.put(key, value);
    }

    public void writeToNbt(CompoundTag compoundTag) {
        CompoundTag nbt = this.serializeNBT();
        compoundTag.put(LRConstants.LIMITED_RESPAWNS_DATA.getPath(), nbt);
    }

    @Override
    public Collection<ResourceLocation> getKeys(){
        return this.dataMap.keySet();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(LRConstants.RESPAWNS.getPath(), getValue(LRConstants.RESPAWNS));
        tag.putLong(LRConstants.GAIN_RESPAWN_TICK.getPath(), getValue(LRConstants.GAIN_RESPAWN_TICK));
        tag.putBoolean(LRConstants.DIED.getPath(), getValue(LRConstants.DIED));
        tag.putInt(LRConstants.CAUSE_OF_DEATH.getPath(), ((CODEnums) getValue(LRConstants.CAUSE_OF_DEATH)).getNum());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains(LRConstants.RESPAWNS.getPath()))
            setValue(LRConstants.RESPAWNS, tag.getInt(LRConstants.RESPAWNS.getPath()));
        if(tag.contains(LRConstants.CAUSE_OF_DEATH.getPath()))
            setValue(LRConstants.CAUSE_OF_DEATH, CODEnums.fromNum(tag.getInt(LRConstants.CAUSE_OF_DEATH.getPath())));

        setValue(LRConstants.GAIN_RESPAWN_TICK, tag.getLong(LRConstants.GAIN_RESPAWN_TICK.getPath()));
        setValue(LRConstants.DIED, tag.getBoolean(LRConstants.DIED.getPath()));
    }

    public enum CODEnums{
        NONE(-1), ENVIRONMENT(0), PVP(1), PVE(2);
        private final int num;

        CODEnums(int num){
            this.num = num;
        }

        public static CODEnums fromNum(int num){
            if(num == -1) {
                return NONE;
            } else if(num == 0) {
                return ENVIRONMENT;
            } else if(num == 1) {
                return PVP;
            } else if (num == 2) {
                return PVE;
            }
            return null;
        }

        public int getNum(){
            return num;
        }
    }
}
