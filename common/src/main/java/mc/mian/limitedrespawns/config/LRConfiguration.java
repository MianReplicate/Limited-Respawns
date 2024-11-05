package mc.mian.limitedrespawns.config;

import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.HashMap;

public class LRConfiguration {

    // how many do new players start with?
    public final ForgeConfigSpec.IntValue startingRespawns;
    //how many do they lose?
    public final ForgeConfigSpec.IntValue loseRespawnCount;
    // do dead players get banned upon death?
    public final ForgeConfigSpec.BooleanValue bannedUponDeath;

    public final HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> deadConfig;
    public final HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> aliveConfig;

    public enum TimedEnums {
        // How long till they get a respawn? (0 disables this feature entirely)
        TICKS_UNTIL_GAIN_RESPAWNS,
        // How many do they get?
        GIVE_AMOUNT_OF_RESPAWNS,
        // Is the timer player based or global based?
        IS_GLOBAL,
        // If timer is player based, should it run while players are offline? (bannedUponDeath forces this on if true and if this is for dead players)
        RUN_WHILE_OFFLINE
    }

    public LRConfiguration(final ForgeConfigSpec.Builder builder) {
        this.deadConfig = new HashMap<>();
        this.aliveConfig = new HashMap<>();

        builder.comment("This category holds general values that most people will want to change.");
        builder.push("Starting Configurations");
        this.startingRespawns = buildInt(builder, "Starting Respawns:", 10, 1, Integer.MAX_VALUE, "How many respawns should a new player start with?");
        this.loseRespawnCount = buildInt(builder, "Amount of Respawns Lost Per Death:", 1, 1, Integer.MAX_VALUE, "How many respawns do you lose each death?");
        this.bannedUponDeath = buildBoolean(builder, "Banned Upon Dying:", false, "This value determines whether you get banned or go into spectator mode when you lose all your respawns.");

        builder.pop();
        builder.comment("This category allows you to customize how players gain lives based on time. There are two subcategories to differentiate between alive & dead players");
        builder.comment("Alive: Still has lives");
        builder.comment("Dead: Lost all their lives");
        builder.push("Timer-Based Lives");
        for(int i = 0; i < 2; i++){
            HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> mapToUse = i == 0 ? aliveConfig : deadConfig;
            String display = i == 0 ? "Alive" : "Dead";
            builder.push(display+" Players");
            mapToUse.put(TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS, buildInt(
                    builder, "How Many Ticks Until Gain Respawn:", -1, -1, Integer.MAX_VALUE, "How many in-game ticks until a player gains a respawn? Setting the value to -1 disables this entire category"
            ));
            mapToUse.put(TimedEnums.GIVE_AMOUNT_OF_RESPAWNS, buildInt(
                    builder, "How Many Respawns to Give?", 1, 1, Integer.MAX_VALUE, "How many respawns should a player get?"
            ));
            mapToUse.put(TimedEnums.IS_GLOBAL, buildBoolean(
                    builder, "Global Timer", false, "Should the timer run globally across the server for all players or be player-specific?"
            ));
            if(i == 0){
                mapToUse.put(TimedEnums.RUN_WHILE_OFFLINE, buildBoolean(
                        builder, "Runs Offline", false, "Should an offline player be accounted for?"
                ));
            }

            builder.pop();
        }
        builder.pop();
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
