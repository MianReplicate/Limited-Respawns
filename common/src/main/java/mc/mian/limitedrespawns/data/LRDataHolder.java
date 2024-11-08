package mc.mian.limitedrespawns.data;

import mc.mian.limitedrespawns.api.IDataHolder;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class LRDataHolder implements IDataHolder {
    protected final HashMap<ResourceLocation, Object> dataMap = new HashMap<>();

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
