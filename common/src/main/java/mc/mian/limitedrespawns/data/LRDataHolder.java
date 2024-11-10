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
        this.dataMap.putIfAbsent(LRConstants.TIME_OF_DEATH, 0L);
        this.dataMap.putIfAbsent(LRConstants.GAIN_RESPAWN_TICK, 0L);
        this.dataMap.putIfAbsent(LRConstants.DEAD, false);
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
        tag.putLong(LRConstants.TIME_OF_DEATH.getPath(), getValue(LRConstants.TIME_OF_DEATH));
        tag.putLong(LRConstants.GAIN_RESPAWN_TICK.getPath(), getValue(LRConstants.GAIN_RESPAWN_TICK));
        tag.putBoolean(LRConstants.DEAD.getPath(), getValue(LRConstants.DEAD));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains(LRConstants.RESPAWNS.getPath()))
            setValue(LRConstants.RESPAWNS, tag.getInt(LRConstants.RESPAWNS.getPath()));

        setValue(LRConstants.TIME_OF_DEATH, tag.getLong(LRConstants.TIME_OF_DEATH.getPath()));
        setValue(LRConstants.GAIN_RESPAWN_TICK, tag.getLong(LRConstants.GAIN_RESPAWN_TICK.getPath()));
        setValue(LRConstants.DEAD, tag.getBoolean(LRConstants.DEAD.getPath()));
    }
}
