package mc.mian.limitedrespawns.mixin;

import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.util.LRConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onRestoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        LRData.get(oldPlayer).ifPresent(oldData -> LRData.get((ServerPlayer)(Object)this).ifPresent(
                newData -> {
                    Collection<ResourceLocation> keys = newData.getKeys();
                    keys.forEach(key -> newData.setValue(key, oldData.getValue(key)));
                }
        ));
    }
}
