package mc.mian.limitedrespawns.mixin;

import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.config.LRConfiguration;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.util.LRConstants;
import mc.mian.limitedrespawns.util.LRUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow @NotNull public abstract MinecraftServer getServer();

    @Unique
    private long limitedRespawns$deadTickTimer = 0;

    @Unique
    private long limitedRespawns$aliveTickTimer = 0;

    @Unique
    private long limitedRespawns$tickTimer = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        ServerLevel serverLevel = (ServerLevel) (Object) this;
        if(serverLevel == this.getServer().overworld() && !serverLevel.isClientSide){
            limitedRespawns$aliveTickTimer++;
            limitedRespawns$deadTickTimer++;

            limitedRespawns$tickTimer++;
            if(limitedRespawns$tickTimer % 20 == 0){
                boolean deadMeetTimer = limitedRespawns$deadTickTimer > (long) LimitedRespawns.config.getBasedOnDead(true,
                        LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS);
                boolean aliveMeetTimer = limitedRespawns$aliveTickTimer > (long) LimitedRespawns.config.getBasedOnDead(false,
                        LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS);

                if(deadMeetTimer)
                    limitedRespawns$deadTickTimer = 0L;
                if(aliveMeetTimer)
                    limitedRespawns$aliveTickTimer = 0L;

                LRUtil.getGameProfiles(this.getServer(), true).forEach(gameProfile -> LRUtil.saveLRDataToProfile(this.getServer(), gameProfile, (holder) -> {
                    boolean dead = holder.getValue(LRConstants.DIED);
                    if((long) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS) <= -1){
                        return;
                    }

                    boolean playerOnline = this.getServer().getPlayerList().getPlayer(gameProfile.getId()) != null;
                    boolean global = (boolean)LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.IS_GLOBAL);
                    // runOffline is not a config available for dead users so we specify false
                    boolean runOffline = (boolean)LimitedRespawns.config.getBasedOnDead(false, LRConfiguration.TimedEnums.RUN_WHILE_OFFLINE);
                    CompoundTag playerData = LRUtil.getPlayerDataFromProfile(this.getServer(), gameProfile);
                    if(!playerOnline && ((playerData != null && playerData.getFloat("Health") <= 0.0F) || (long) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS) < 0)){
                        holder.setValue(LRConstants.GAIN_RESPAWN_TICK, 0L);
                        return;
                    }

                    if(!global){
                        // This code runs only if player is offline to handle certain things
                        // IF they are dead then the mandatory code runs below
                        // If they are an alive player who is offline, then code below can run ONLY if runOffline is true
                        if((runOffline || dead) && !playerOnline){
                            // don't account for someone currently in the dead menu, they might be abusing this
                            holder.setValue(LRConstants.GAIN_RESPAWN_TICK, (long) holder.getValue(LRConstants.GAIN_RESPAWN_TICK) + 1);

                            if((long) holder.getValue(LRConstants.GAIN_RESPAWN_TICK) >
                                    (long) LimitedRespawns.config.getBasedOnDead(dead,
                                            LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS)){
                                holder.setValue(LRConstants.GAIN_RESPAWN_TICK, 0L);

                                int newAmount = (int) holder.getValue(LRConstants.RESPAWNS) + (int) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.GIVE_AMOUNT_OF_RESPAWNS);
                                holder.setValue(LRConstants.RESPAWNS, newAmount);
                            }
                        }
                    } else {
                        // if we are running on global timer, let's do things a bit differently..
                        // below code runs only if player is online or (player is offline & runOffline is true) or if they are dead
                        // we do not need to worry about this conflicting with LivingEntityMixin because it will autodisable itself if global is true
                        if(runOffline || dead || playerOnline){
                            boolean meetTimer = dead ? deadMeetTimer : aliveMeetTimer;
                            long tickTimer = dead ? limitedRespawns$deadTickTimer : limitedRespawns$aliveTickTimer;

                            holder.setValue(LRConstants.GAIN_RESPAWN_TICK, tickTimer);
                            if(meetTimer){
                                int newAmount = (int) holder.getValue(LRConstants.RESPAWNS) + (int) LimitedRespawns.config.getBasedOnDead(dead, LRConfiguration.TimedEnums.GIVE_AMOUNT_OF_RESPAWNS);
                                if(holder instanceof LRData lrData){
                                    boolean announce = !dead || !lrData.hasEnoughRespawns(newAmount);
                                    lrData.setRespawns(newAmount, announce);
                                } else {
                                    holder.setValue(LRConstants.RESPAWNS, newAmount);
                                }
                            }
                        }
                    }
                }));
            }
        }
    }
}
