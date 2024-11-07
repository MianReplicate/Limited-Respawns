package mc.mian.limitedrespawns.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyReturnValue(method = "isHardcore", at = @At("RETURN"))
    private boolean forceOff(boolean original){
        // if you have this mod, then there's no reason in having hardcore
        return false;
    }
}
