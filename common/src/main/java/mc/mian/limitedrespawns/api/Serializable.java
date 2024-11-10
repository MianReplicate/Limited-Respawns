package mc.mian.limitedrespawns.api;

import net.minecraft.nbt.Tag;

public interface Serializable<T extends Tag> {
    T serializeNBT();

    void deserializeNBT(T arg);
}
