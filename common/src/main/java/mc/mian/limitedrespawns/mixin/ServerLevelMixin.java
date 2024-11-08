package mc.mian.limitedrespawns.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Unique
    private int tickTimer = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo ci){
        
    }
}
