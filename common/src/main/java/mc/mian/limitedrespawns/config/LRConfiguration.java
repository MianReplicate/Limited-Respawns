package mc.mian.limitedrespawns.config;

import mc.mian.limitedrespawns.data.LRDataHolder;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LRConfiguration {
    // how many do new players start with?
    public final ForgeConfigSpec.IntValue startingRespawns;
    //how many do they lose?
    public final ForgeConfigSpec.IntValue loseRespawnCount;
    // do dead players get banned upon death?
    public final ForgeConfigSpec.BooleanValue bannedUponDeath;
    // what messages should be used every time a player is revived?
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> customRevivalMessages;
    // what messages should be used every time a player respawns?
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> customRespawnMessages;
    // what messages should be used every time a player joins in?
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> customWelcomeMessages;
    // what messages should be used when a player dies and has no respawns left?
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> customDeathMessages;
    public final ForgeConfigSpec.BooleanValue enableWokeColors;

    public final HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> deadConfig;
    public final HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> aliveConfig;
    public final HashMap<LRDataHolder.CODEnums, ForgeConfigSpec.BooleanValue> CODToConfig;

    public enum TimedEnums {
        // How long till they get a respawn? (0 disables this feature entirely)
        TICKS_UNTIL_GAIN_RESPAWNS,
        // How many do they get?
        GIVE_AMOUNT_OF_RESPAWNS,
        // Is there a max respawn count?
        MAXIMUM_RESPAWNS,
        // Is the timer player based or global based?
        IS_GLOBAL,
        // Should players earn respawns while offline (bannedUponDeath forces this on if true and if this is for dead players)
        RUN_WHILE_OFFLINE
    }

    public Object getBasedOnDead(boolean died, TimedEnums timedEnum){
        if(died){
            return deadConfig.get(timedEnum).get();
        } else {
            return aliveConfig.get(timedEnum).get();
        }
    }

    public boolean loseRespawnForCause(LRDataHolder.CODEnums codEnum){
        if(codEnum == LRDataHolder.CODEnums.NONE) return false;
        return CODToConfig.get(codEnum).get();
    }

    public LRConfiguration(final ForgeConfigSpec.Builder builder) {
        this.deadConfig = new HashMap<>();
        this.aliveConfig = new HashMap<>();
        this.CODToConfig = new HashMap<>();

        builder.comment("This category holds general values that most people will want to change.");
        builder.push("Starting Configurations");
        this.startingRespawns = buildInt(builder, "Starting Respawns:", 3, 1, Integer.MAX_VALUE, "How many respawns should a new player start with?");
        this.loseRespawnCount = buildInt(builder, "Amount of Respawns Lost Per Respawn Use:", 1, 1, Integer.MAX_VALUE, "How many respawns do you lose each time you respawn?");
        this.bannedUponDeath = buildBoolean(builder, "Banned Upon Dying:", false, "This value determines whether you get banned or go into spectator mode when you lose all your respawns.");

        builder.pop();

        builder.comment("This category messes around with how the mod sends you messages.");
        builder.push("Custom Messages");
        this.customRespawnMessages = buildStringList(builder, "Respawn Messages:", new ArrayList<>(List.of("Don't worry about losing. Worry about winning. %d more respawn(s).","You'll come back from this. Don't let them take you down. %d more respawn(s).","That was unfortunate. %d more respawn(s).", "Let's not see that again. %d more respawn(s).", "Gonna need to try harder than that if you wanna survive. %d more respawn(s)", "%d more respawn(s).", "You only get %d more bod(ies). Make this count, %s.", "Try not to get yourself killed again, %s. You've got %d more respawn(s).")), "Create custom messages here for users to see when they respawn. %s is a placeholder for the user's name. %d is a placeholder for the amount of respawns they have. Empty this list to disable the feature");
        this.customRevivalMessages = buildStringList(builder, "Revive Messages:", new ArrayList<>(List.of("You are who you are. No one can tell you who you are but you. Whoever you know yourself to be, you are right.","No matter who you are or what you do in the world, you make a difference every single day.","Tell your stories. Often and loudly. We need to hear you, and you need to be heard. Tell your stories. Someone is listening. I promise.","Instead of 'I’ve always been this way,' I think: 'I have always been becoming who I am right now.'", "Those who have survived the unthinkable are also those who know how to create a better world – because it’s ended for us before.", "Discover who you are, and live this new life authentically", "New body, new you.", "A swap in identity never hurt anyone.")), "Create custom messages here for users to see when they are revived. %s is a placeholder for the user's name. %d is a placeholder for the amount of respawns they have. Empty this list to disable the feature");
        this.customWelcomeMessages = buildStringList(builder, "Welcome Messages:", new ArrayList<>(List.of("The outside is watching us. You've got %d respawn(s).","Careful, dangers await this land. You have %d respawn(s).","Howdy, you've got %d respawn(s).","\"How'd they not realize, THEY DON'T KNOW ANYTHING!\" Ahem sorry, welcome. You have %d respawn(s).","Did you know? Binary is a number system- sorry it's not funny. Welcome, you have %d respawn(s).","Welcome %s. You have %d respawn(s).")), "Create custom messages here for users to see when they join in. %s is a placeholder for the user's name. %d is a placeholder for the amount of respawns they have. Empty this list to disable the feature");
        this.customDeathMessages = buildStringList(builder, "Death Messages:", new ArrayList<>(List.of("You've lost all your respawns :<","Don't fret, next time will be better.", "Well that was something.")), "Create custom messages here for users to see when they run out of respawns and die. %s is a placeholder for the user's name. %d is a placeholder for the amount of respawns they have. Empty this list to use the default message");
        this.enableWokeColors = buildBoolean(builder, "Enable NB/GF Colors:", true, "Disable randomization of NB/GF colors");

        builder.pop();

        builder.comment("This category determines how a player may lose a respawn");
        builder.push("Ways of Losing Respawns");
        this.CODToConfig.put(LRDataHolder.CODEnums.ENVIRONMENT, buildBoolean(builder, "Lose Respawns Via Environment:", true, "Lose respawns from environmental deaths"));
        this.CODToConfig.put(LRDataHolder.CODEnums.PVE, buildBoolean(builder, "Lose Respawns Via PVE:", true, "Lose a respawn when killed by non-player entities"));
        this.CODToConfig.put(LRDataHolder.CODEnums.PVP, buildBoolean(builder, "Lose Respawns Via PVP:", true, "Lose a respawn when you are killed by another player"));

        builder.pop();

        builder.comment("This category allows you to customize how players gain respawns based on time. There are two subcategories to differentiate between alive & dead players");
        builder.comment("Alive: Still has respawns");
        builder.comment("Dead: Lost all their respawns and is dead");
        builder.push("Timer-Based Respawns");
        for(int i = 0; i < 2; i++){
            HashMap<TimedEnums, ForgeConfigSpec.ConfigValue<?>> mapToUse = i == 0 ? aliveConfig : deadConfig;
            String display = i == 0 ? "Alive" : "Dead";
            builder.push(display+" Players");
            mapToUse.put(TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS, buildLong(
                    builder, "How Many Ticks Until Gain Respawn:", -1, -1, Integer.MAX_VALUE, "How many in-game ticks until a player gains a respawn? Setting the value to -1 disables this entire category"
            ));
            mapToUse.put(TimedEnums.MAXIMUM_RESPAWNS, buildInt(
                    builder, "Maximum Amount of Respawns:", 5, -1, Integer.MAX_VALUE, "How many respawns can the player have before they stop gaining respawns? Setting the value to -1 disables this feature"
            ));
            mapToUse.put(TimedEnums.GIVE_AMOUNT_OF_RESPAWNS, buildInt(
                    builder, "How Many Respawns to Give?", 1, 1, Integer.MAX_VALUE, "How many respawns should a player get?"
            ));
            mapToUse.put(TimedEnums.IS_GLOBAL, buildBoolean(
                    builder, "Global Timer", false, "Should the timer run globally across the server for all players or be player-specific?"
            ));


            if(i == 0){
                mapToUse.put(TimedEnums.RUN_WHILE_OFFLINE, buildBoolean(
                        builder, "Runs Offline", false, "Should an offline player be accounted for? This option only works while the server is running. Server will not account for any players while it is offline."
                ));
            }

            builder.pop();
        }
        builder.pop();
    }

    private static ForgeConfigSpec.IntValue buildInt(final ForgeConfigSpec.Builder builder, String translationPath, int defaultValue, int min, int max, @Nullable String comment) {
        return comment == null ? builder.translation(translationPath).defineInRange(translationPath, defaultValue, min, max) : builder.comment(comment).translation(translationPath).defineInRange(translationPath, defaultValue, min, max);
    }

    private static ForgeConfigSpec.LongValue buildLong(final ForgeConfigSpec.Builder builder, String translationPath, long defaultValue, long min, long max, @Nullable String comment) {
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

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> buildStringList(final ForgeConfigSpec.Builder builder, String translationPath, List<? extends String> defaultList, String comment){
        return builder.comment(comment).translation(translationPath).defineList(translationPath, defaultList, String.class::isInstance);
    }
}
