package mc.mian.limitedrespawns.util;

import com.mojang.authlib.GameProfile;
import mc.mian.limitedrespawns.LimitedRespawns;
import mc.mian.limitedrespawns.api.IDataHolder;
import mc.mian.limitedrespawns.config.LRConfiguration;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.data.LRDataHolder;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class LRUtil {
    public static ResourceLocation modLoc(String name) {
        return new ResourceLocation(LRConstants.MOD_ID, name);
    }

    public static List<GameProfile> getGameProfiles(MinecraftServer server, boolean includedSaved){
        ArrayList<GameProfile> gameProfiles = new ArrayList<>();
        server.getPlayerList().getPlayers().forEach(player -> gameProfiles.add(player.getGameProfile()));
        if(includedSaved){
            LevelStorageSource.LevelStorageAccess levelStorageAccess = server.storageSource;
            File playerDir = levelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
            Arrays.stream(Objects.requireNonNull(playerDir.listFiles())).toList().forEach(file -> {
                String[] splitString = file.getName().split(".dat");
                GameProfile gameProfile = server.getProfileCache().get(UUID.fromString(splitString[0])).get();
                if(!gameProfiles.contains(gameProfile)){
                    gameProfiles.add(gameProfile);
                }
            });
        }
        return gameProfiles;
    }

    public static boolean saveLRDataToProfile(MinecraftServer server, GameProfile gameProfile, Consumer<IDataHolder> function){
        try {
            ServerPlayer serverPlayer = server.getPlayerList().getPlayer(gameProfile.getId());
            if(serverPlayer != null){
                LRData.get(serverPlayer).ifPresent(function);
            } else {
                CompoundTag playerTag = getPlayerDataFromProfile(server, gameProfile);
                if(playerTag == null)
                    return false;

                IDataHolder dataHolder = getDataHolderFromProfile(server, gameProfile).get();
                function.accept(dataHolder);
                CompoundTag compoundTag = dataHolder.serializeNBT();
                playerTag.put(LRConstants.LIMITED_RESPAWNS_DATA.getPath(), compoundTag);

                LevelStorageSource.LevelStorageAccess levelStorageAccess = server.storageSource;
                File playerDir = levelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
                String uuidString = gameProfile.getId().toString();
                Path path = playerDir.toPath();
                Path path2 = Files.createTempFile(path, uuidString + "-", ".dat");
                NbtIo.writeCompressed(playerTag, path2.toFile());
                Path path3 = path.resolve(uuidString + ".dat");
                Path path4 = path.resolve(uuidString + ".dat_old");
                Util.safeReplaceFile(path3, path2, path4);
            }
            return true;
        } catch(Exception var4) {
            LRConstants.LOGGER.warn("Failed to save "+ gameProfile.getName() + "'s data");
            LRConstants.LOGGER.warn("Error: "+var4);
        }
        return false;
    }

    public static Optional<IDataHolder> getDataHolderFromProfile(MinecraftServer server, GameProfile gameProfile){
        try{
            CompoundTag playerTag = getPlayerDataFromProfile(server, gameProfile);
            if(playerTag != null){
                return Optional.of(LRDataHolder.from(playerTag.getCompound(LRConstants.LIMITED_RESPAWNS_DATA.getPath())));
            } else {
                ServerPlayer serverPlayer = server.getPlayerList().getPlayer(gameProfile.getId());
                if(serverPlayer != null){
                    return Optional.of(LRData.get(serverPlayer).get());
                }
            }
        } catch (Exception var4){
            LRConstants.LOGGER.warn("Failed to retrieve " + gameProfile.getName()+"'s data");
            LRConstants.LOGGER.warn("Error: "+var4);
        }
        return null;
    }

    @Nullable
    public static CompoundTag getPlayerDataFromProfile(MinecraftServer server, GameProfile gameProfile){
        try{
            LevelStorageSource.LevelStorageAccess levelStorageAccess = server.storageSource;
            File playerDir = levelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
            String uuidString = gameProfile.getId().toString();
            File file = new File(playerDir, uuidString + ".dat");
            if (file.exists() && file.isFile())
            {
                CompoundTag tag = NbtIo.readCompressed(file);
                return tag;
            } else {
                return null;
            }
        } catch (Exception var4){
            LRConstants.LOGGER.warn("Failed to retrieve " + gameProfile.getName()+"'s data");
            LRConstants.LOGGER.warn("Error: "+var4);
        }
        return null;
    }

    public static MutableComponent getTimeToRespawnComponent(long respawnTick, int currentRespawns){
        int requiredRespawns = LimitedRespawns.config.loseRespawnCount.get() - currentRespawns;
        long requiredTicks = ((long) LimitedRespawns.config.getBasedOnDead(true, LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS)) - respawnTick;
        int givenRespawns = (int) LimitedRespawns.config.getBasedOnDead(true, LRConfiguration.TimedEnums.GIVE_AMOUNT_OF_RESPAWNS);

        double seconds = requiredTicks / 20.0;
        double minutes = seconds / 60;
        double roundedMinutes = Math.round(minutes * 10) / 10.0;

        return Component.translatable("chat.message.limitedrespawns.time_to_get_respawns",
                requiredRespawns, givenRespawns, roundedMinutes);
    }

    public static MutableComponent getDeadComponent(LRData lrData){
        long respawnTick = lrData.getValue(LRConstants.GAIN_RESPAWN_TICK);
        int currentRespawns = lrData.getRespawns();
        String diedMessage = Component.translatable("purgatory.limitedrespawns.lost_respawns").getString();
        List<String> messages = (List<String>) LimitedRespawns.config.customDeathMessages.get();
        if(!messages.isEmpty()) {
            Random random = new Random();
            String message = messages.get(random.nextInt(0, messages.size()));
            diedMessage = "\"" + message.replaceAll("%s", lrData.getLivingEntity().getName().getString()).replaceAll("%d", String.valueOf(currentRespawns)) + "\"";
        }

        String timeToGetRespawns = LRUtil.getTimeToRespawnComponent(respawnTick, currentRespawns).getString();

        return (long) LimitedRespawns.config.getBasedOnDead(true, LRConfiguration.TimedEnums.TICKS_UNTIL_GAIN_RESPAWNS) > -1 ?
                Component.literal(diedMessage).withStyle(Style.EMPTY.withColor(getRandomColor(getGFColors()))).append("\n").append(Component.literal(timeToGetRespawns).withStyle(Style.EMPTY.withColor(getRandomColor(getNBColors())))) :
                Component.literal(diedMessage).withStyle(Style.EMPTY.withColor(getRandomColor(getGFColors())));
    }

    public static List<String> getBothColors(){
        List<String> colors = new ArrayList<>(getGFColors());
        colors.addAll(getNBColors());
        return colors;
    }

    // no black colors cause it might ruin gameplay ngl
    public static List<String> getGFColors(){
        return List.of(
                "FF76A4",
                "FFFFFF",
                "C011D7",
                "2F3CBE",
                "635F5F"
        );
    }

    public static List<String> getNBColors(){
        return List.of(
                "FCF434",
                "FFFFFF",
                "9C59D1",
                "635F5F"
        );
    }

    public static int getRandomColor(List<String> colors){
        Random random = new Random();
        return LimitedRespawns.config.enableWokeColors.get() ? TextColor.parseColor("#"+colors.get(random.nextInt(0, colors.size()))).getValue() : 16777215;
    }
}
