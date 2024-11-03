package mc.mian.templatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final TemplateConfiguration SERVER;

    static{
        final Pair<TemplateConfiguration, ForgeConfigSpec> specPair = new
                ForgeConfigSpec.Builder()
                .configure(TemplateConfiguration::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }
}
