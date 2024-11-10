package mc.mian.limitedrespawns.data;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.ILRData;
import mc.mian.limitedrespawns.api.ILRRetrieve;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;

import java.util.Optional;

public class LRData extends LRDataHolder implements ILRData {
    private final LivingEntity livingEntity;

    public LRData(LivingEntity livingEntity){
        this.livingEntity = livingEntity;
    }

    public static Optional<LRData> get(LivingEntity livingEntity){
        return Optional.ofNullable(((ILRRetrieve) livingEntity).limitedRespawns$getData());
    }

    public boolean hasEnoughRespawns(){
        return this.getRespawns() - LimitedRespawns.config.loseRespawnCount.get() >= 0;
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public <T> T getValue(ResourceLocation key) {
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }

        return super.getValue(key);
    }

    @Override
    public <T> void setValue(ResourceLocation key, T value) {
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }

        super.setValue(key, value);
    }

    @Override
    public void onRespawn(){
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }
        int respawns = this.getValue(LRConstants.RESPAWNS);
        int amountToLose = LimitedRespawns.config.loseRespawnCount.get();
        ServerPlayer serverPlayer = (ServerPlayer) livingEntity;
        if(!this.hasEnoughRespawns()){
            setValue(LRConstants.DEAD, true);
            serverPlayer.setGameMode(GameType.SPECTATOR);
            serverPlayer.displayClientMessage(Component.translatable("purgatory.limitedrespawns.lost_respawns_no_time"), false);
        } else {
            setValue(LRConstants.DEAD, false);
            if(serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR){
                serverPlayer.setGameMode(GameType.SURVIVAL);
            }
            this.setRespawns(respawns - amountToLose);
        }
    }

    @Override
    public void setRespawns(int amount) {
        if(this.livingEntity.level().isClientSide()){
           throw new RuntimeException("who tf is running this on the client");
        }
        amount = Math.max(amount, 0);
        int old = this.getValue(LRConstants.RESPAWNS);
        if(old == amount) return;

        String lostOrGained = old < amount ? "gained":"lost";
        this.setValue(LRConstants.RESPAWNS, amount);
        if(livingEntity instanceof ServerPlayer serverPlayer){
            serverPlayer.displayClientMessage(Component.translatable("chat.message.limitedrespawns.respawns_changed", lostOrGained, old, amount), false);
        }
    }

    @Override
    public int getRespawns() {
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }

        return this.getValue(LRConstants.RESPAWNS);
    }
}
