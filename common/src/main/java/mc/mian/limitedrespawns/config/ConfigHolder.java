package mc.mian.limitedrespawns.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;

public class ConfigHolder {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final LRConfiguration SERVER;

    public static HashMap<ForgeConfigSpec.ConfigValue<?>, Object> config = new HashMap<>();

    static{
        final Pair<LRConfiguration, ForgeConfigSpec> specPair = new
                ForgeConfigSpec.Builder()
                .configure(LRConfiguration::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }
}
