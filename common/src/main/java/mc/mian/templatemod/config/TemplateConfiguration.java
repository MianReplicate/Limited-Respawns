package mc.mian.templatemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;

public class TemplateConfiguration {
    public final ForgeConfigSpec.BooleanValue meow;
    public TemplateConfiguration(final ForgeConfigSpec.Builder builder) {
        builder.comment("This category holds general values that most people will want to change.");
        builder.push("General Settings");
        this.meow = buildBoolean(builder, "meow", true, "meow!");
    }

    private static ForgeConfigSpec.IntValue buildInt(final ForgeConfigSpec.Builder builder, String translationPath, int defaultValue, int min, int max, @Nullable String comment) {
        return comment == null ? builder.translation(translationPath).defineInRange(translationPath, defaultValue, min, max) : builder.comment(comment).translation(translationPath).defineInRange(translationPath, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(final ForgeConfigSpec.Builder builder, String translationPath, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(translationPath).defineInRange(translationPath, defaultValue, min, max);
    }

    private static ForgeConfigSpec.ConfigValue buildString(final ForgeConfigSpec.Builder builder, String translationPath, String defaultValue, String comment) {
        return builder.comment(comment).translation(translationPath).define(translationPath, defaultValue);
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(final ForgeConfigSpec.Builder builder, String translationPath, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(translationPath).define(translationPath, defaultValue);
    }

    private static ForgeConfigSpec.EnumValue buildEnum(final ForgeConfigSpec.Builder builder, String translationPath, Enum defaultValue, String comment) {
        return builder.comment(comment).translation(translationPath).defineEnum(translationPath, defaultValue);
    }
}
