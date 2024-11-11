package mc.mian.limitedrespawns.data;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.ILRData;
import mc.mian.limitedrespawns.api.ILRRetrieve;
import mc.mian.limitedrespawns.config.LRConfiguration;
import mc.mian.limitedrespawns.util.LRConstants;
import mc.mian.limitedrespawns.util.LRUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LRData extends LRDataHolder implements ILRData {
    private final LivingEntity livingEntity;
    public boolean init;

    public LRData(LivingEntity livingEntity){
        this.livingEntity = livingEntity;
    }

    public static Optional<LRData> get(LivingEntity livingEntity){
        return Optional.ofNullable(((ILRRetrieve) livingEntity).limitedRespawns$getData());
    }

    @Override
    public boolean hasEnoughRespawns(){
        return this.getRespawns() - LimitedRespawns.config.loseRespawnCount.get() >= 0;
    }

    @Override
    public boolean hasEnoughRespawns(int respawnCount){
        return respawnCount - LimitedRespawns.config.loseRespawnCount.get() >= 0;
    }

    @Override
    public void init(){
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }

        if(this.livingEntity instanceof ServerPlayer serverPlayer && !init){
            init = true;
            if(this.getValue(LRConstants.DIED)) {
                MutableComponent deadComponent = LRUtil.getTimeToRespawnComponent(this.getValue(LRConstants.GAIN_RESPAWN_TICK), this.getRespawns()).withColor(LRUtil.getRandomColor(LRUtil.getNBColors()));

                if (LimitedRespawns.config.bannedUponDeath.get()) {
                    serverPlayer.connection.disconnect(deadComponent);
                } else {
                    serverPlayer.setGameMode(GameType.SPECTATOR);
                    serverPlayer.displayClientMessage(deadComponent, false);
                }
            } else {
                Random random = new Random();
                List<String> messages = (List<String>) LimitedRespawns.config.customWelcomeMessages.get();
                if(!messages.isEmpty()){
                    String message = messages.get(random.nextInt(0, messages.size()));
                    serverPlayer.displayClientMessage(Component.literal(message.replaceAll("%s", serverPlayer.getName().getString()).replaceAll("%d", String.valueOf(this.getRespawns()))).withColor(LRUtil.getRandomColor(LRUtil.getBothColors())), false);
                }
            }
        }
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public void onRespawn(){
        if(this.livingEntity.level().isClientSide()){
            throw new RuntimeException("who tf is running this on the client");
        }
        this.init = true; // just set this to true
        if(this.getValue(LRConstants.CAUSE_OF_DEATH) instanceof CODEnums && LimitedRespawns.config.loseRespawnForCause(this.getValue(LRConstants.CAUSE_OF_DEATH))){
            int respawns = this.getValue(LRConstants.RESPAWNS);
            int amountToLose = LimitedRespawns.config.loseRespawnCount.get();
            ServerPlayer serverPlayer = (ServerPlayer) livingEntity;

            if(!this.hasEnoughRespawns()){
                setValue(LRConstants.DIED, true);
                MutableComponent deadComponent = LRUtil.getDeadComponent(this);

                if(!LimitedRespawns.config.bannedUponDeath.get()){
                    serverPlayer.setGameMode(GameType.SPECTATOR);
                    serverPlayer.displayClientMessage(deadComponent, false);
                } else {
                    serverPlayer.connection.disconnect(deadComponent);
                }
            } else {
                boolean wasDead = getValue(LRConstants.DIED);
                setValue(LRConstants.DIED, false);
                this.setValue(LRConstants.CAUSE_OF_DEATH, CODEnums.NONE);
                if(serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR){
                    serverPlayer.setGameMode(GameType.SURVIVAL);
                }
                this.setRespawns(respawns - amountToLose, false);
                List<String> messages = wasDead ? (List<String>) LimitedRespawns.config.customRevivalMessages.get() : (List<String>) LimitedRespawns.config.customRespawnMessages.get();
                if(!messages.isEmpty()) {
                    Random random = new Random();
                    String message = messages.get(random.nextInt(0, messages.size()));
                    serverPlayer.displayClientMessage(
                            Component.literal("\"" + message.replaceAll("%s", serverPlayer.getName().getString()).replaceAll("%d", String.valueOf(this.getRespawns())) + "\"").withColor(LRUtil.getRandomColor(LRUtil.getBothColors())),
                            false);
                }
            }
        }
    }

    @Override
    public void setRespawns(int amount, boolean announce) {
        if(this.livingEntity.level().isClientSide()){
           throw new RuntimeException("who tf is running this on the client");
        }
        amount = Math.max(amount, 0);
        int old = this.getValue(LRConstants.RESPAWNS);
        if(old == amount) return;
        int maxRespawns = (int) LimitedRespawns.config.getBasedOnDead(this.getValue(LRConstants.DIED), LRConfiguration.TimedEnums.MAXIMUM_RESPAWNS);
        if((amount > maxRespawns && amount > old) && maxRespawns != -1) return;

        this.setValue(LRConstants.RESPAWNS, amount);

        if(livingEntity instanceof ServerPlayer serverPlayer && announce){
            String lostOrGained = old < amount ? "gained":"lost";
            MutableComponent component = Component.literal(Component.translatable("chat.message.limitedrespawns.respawns_changed", lostOrGained, old, amount).getString()).withColor(LRUtil.getRandomColor(LRUtil.getBothColors()));
            serverPlayer.displayClientMessage(component, false);
        }
    }

    @Override
    public int getRespawns() {
        return this.getValue(LRConstants.RESPAWNS);
    }
}
