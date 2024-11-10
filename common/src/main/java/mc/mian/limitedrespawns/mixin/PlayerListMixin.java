package mc.mian.limitedrespawns.mixin;

import mc.mian.limitedrespawns.data.LRData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "respawn", at = @At(value = "RETURN"))
    public void onRespawn(ServerPlayer player, boolean keepInventory, Entity.RemovalReason reason, CallbackInfoReturnable<ServerPlayer> cir){
        if(reason == Entity.RemovalReason.KILLED) {
            LRData.get(cir.getReturnValue()).ifPresent(LRData::onRespawn);
        }
    }
}
