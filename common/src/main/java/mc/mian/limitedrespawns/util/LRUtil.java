package mc.mian.limitedrespawns.util;

import com.mojang.authlib.GameProfile;
import mc.mian.limitedrespawns.api.IDataHolder;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.data.LRDataHolder;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class LRUtil {
    public static ResourceLocation modLoc(String name) {
        return ResourceLocation.fromNamespaceAndPath(LRConstants.MOD_ID, name);
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
                LRData.get(serverPlayer).ifPresent(function::accept);
            } else {
                CompoundTag playerTag = getPlayerDataFromProfile(server, gameProfile);
                IDataHolder dataHolder = getDataHolderFromProfile(server, gameProfile).get();

                function.accept(dataHolder);
                CompoundTag compoundTag = dataHolder.serializeNBT();
                playerTag.put(LRConstants.LIMITED_RESPAWNS_DATA.getPath(), compoundTag);

                LevelStorageSource.LevelStorageAccess levelStorageAccess = server.storageSource;
                File playerDir = levelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
                String uuidString = gameProfile.getId().toString();
                Path path = playerDir.toPath();
                Path path2 = Files.createTempFile(path, uuidString + "-", ".dat");
                NbtIo.writeCompressed(playerTag, path2);
                Path path3 = path.resolve(uuidString + ".dat");
                Path path4 = path.resolve(uuidString + ".dat_old");
                Util.safeReplaceFile(path3, path2, path4);
            }
            return true;
        } catch(Exception var4) {
            LRConstants.LOGGER.warn("Failed to save "+ gameProfile.getName() + "'s data");
        }
        return false;
    }

    public static Optional<IDataHolder> getDataHolderFromProfile(MinecraftServer server, GameProfile gameProfile){
        try{
            return Optional.of(LRDataHolder.from(getPlayerDataFromProfile(server, gameProfile).getCompound(LRConstants.LIMITED_RESPAWNS_DATA.getPath())));
        } catch (Exception var4){
            LRConstants.LOGGER.warn("Failed to retrieve " + gameProfile.getName()+"'s data");
        }
        return null;
    }

    public static CompoundTag getPlayerDataFromProfile(MinecraftServer server, GameProfile gameProfile){
        try{
            LevelStorageSource.LevelStorageAccess levelStorageAccess = server.storageSource;
            File playerDir = levelStorageAccess.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
            String uuidString = gameProfile.getId().toString();
            File file = new File(playerDir, uuidString + ".dat");
            if (file.exists() && file.isFile())
            {
                CompoundTag tag = NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
                return tag;
            } else {
                throw new Exception("Soooo we couldn't get their data whomp whomp");
            }
        } catch (Exception var4){
            LRConstants.LOGGER.warn("Failed to retrieve " + gameProfile.getName()+"'s data");
        }
        return null;
    }
}
