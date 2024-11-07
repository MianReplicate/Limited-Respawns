package mc.mian.limitedrespawns.data;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.ILRData;
import mc.mian.limitedrespawns.api.ILRRetrieve;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;

import java.util.HashMap;
import java.util.Optional;

public class LRData implements ILRData {
    private final LivingEntity livingEntity;
    private final HashMap<ResourceLocation, Object> dataMap;

    public LRData(LivingEntity livingEntity){
        this.dataMap = new HashMap<>();
        this.livingEntity = livingEntity;

        this.dataMap.putIfAbsent(LRConstants.RESPAWNS, LimitedRespawns.config.startingRespawns.get());
        this.dataMap.putIfAbsent(LRConstants.NEEDED_RESPAWNS, 0);
        this.dataMap.putIfAbsent(LRConstants.TIME_OF_DEATH, 0L);
    }

    public static Optional<LRData> get(LivingEntity livingEntity){
        return Optional.ofNullable(((ILRRetrieve) livingEntity).limitedRespawns$getData());
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public <T> T getValue(ResourceLocation key) {
        return (T) this.dataMap.get(key);
    }

    @Override
    public <T> void setValue(ResourceLocation key, T value) {
        this.dataMap.put(key, value);
    }

    @Override
    public void onRespawn(){
        if(!this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }
        int respawns = this.getValue(LRConstants.RESPAWNS);
        int amountNeeded = LimitedRespawns.config.loseRespawnCount.get();
        ServerPlayer serverPlayer = (ServerPlayer) livingEntity;
        if(respawns - amountNeeded < 0){
            serverPlayer.setGameMode(GameType.SPECTATOR);
            serverPlayer.displayClientMessage(Component.translatable("chat.message.limitedrespawns.lost_respawns_no_time"), false);
        } else {
            this.setValue(LRConstants.RESPAWNS, respawns - LimitedRespawns.config.loseRespawnCount.get());
            serverPlayer.displayClientMessage(Component.translatable("chat.message.limitedrespawns.lost_respawns_no_time", this.getValue(LRConstants.RESPAWNS)), false);
        }
    }

    @Override
    public void onChange() {
        if(!this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }
        int respawns = this.getValue(LRConstants.RESPAWNS);
        if(respawns < 0){
            ServerPlayer serverPlayer = (ServerPlayer) livingEntity;
//            if(LimitedRespawns.config.bannedUponDeath.get()){
//
//            } else {
//
//            }
            serverPlayer.setGameMode(GameType.SPECTATOR);
            serverPlayer.displayClientMessage(Component.translatable("chat.message.limitedrespawns.lost_respawns_no_time"), false);
        }
    }

    @Override
    public void setRespawns(int amount, boolean triggerChange) {
        if(this.livingEntity.level().isClientSide()){
           throw new RuntimeException("who tf is running this on the client");
        }

        this.setValue(LRConstants.RESPAWNS, amount);
        if(triggerChange)
            this.onChange();
    }

    public void setRespawns(int amount){
        this.setRespawns(amount, true);
    }

    public void writeToNbt(CompoundTag compoundTag) {
        CompoundTag nbt = this.serializeNBT();
        compoundTag.put(LRConstants.LIMITED_RESPAWNS_DATA.getPath(), nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(LRConstants.RESPAWNS.getPath(), getValue(LRConstants.RESPAWNS));
        tag.putInt(LRConstants.NEEDED_RESPAWNS.getPath(), getValue(LRConstants.NEEDED_RESPAWNS));
        tag.putLong(LRConstants.TIME_OF_DEATH.getPath(), getValue(LRConstants.TIME_OF_DEATH));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        setValue(LRConstants.RESPAWNS, tag.getInt(LRConstants.RESPAWNS.getPath()));
        setValue(LRConstants.NEEDED_RESPAWNS, tag.getInt(LRConstants.NEEDED_RESPAWNS.getPath()));
        setValue(LRConstants.TIME_OF_DEATH, tag.getLong(LRConstants.TIME_OF_DEATH.getPath()));
    }
}
