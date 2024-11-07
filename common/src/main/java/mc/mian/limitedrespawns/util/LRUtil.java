package mc.mian.limitedrespawns.util;

import net.minecraft.resources.ResourceLocation;

public class LRUtil {
    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(LRConstants.MOD_ID, name);
    }
}
