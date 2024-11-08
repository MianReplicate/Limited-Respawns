package mc.mian.limitedrespawns.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.mian.limitedrespawns.data.LRData;
import mc.mian.limitedrespawns.util.LRConstants;
import mc.mian.limitedrespawns.util.LRUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LRCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("lr")
                        .then(Commands.literal("getrespawns")
                                .executes(commandContext -> getRespawns(commandContext.getSource()))
                                .then(Commands.argument("players", GameProfileArgument.gameProfile())
                                        .suggests(((context, builder) -> {
                                            ArrayList<String> suggestList = new ArrayList<>();
                                            List<GameProfile> gameProfiles = LRUtil.getGameProfiles(context.getSource().getServer(), true);
                                            gameProfiles.forEach(gameProfile -> suggestList.add(gameProfile.getName()));
                                            return SharedSuggestionProvider.suggest(suggestList, builder);
                                        }))
                                        .executes(commandContext -> getRespawns(commandContext.getSource(), GameProfileArgument.getGameProfiles(commandContext, "players")))
        )));
    }

    private static int getRespawns(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        ServerPlayer serverPlayer = commandSourceStack.getPlayerOrException();
        LRData.get(serverPlayer).ifPresent(lrData -> serverPlayer
                .displayClientMessage(Component.translatable("chat.message.limitedrespawns.get_respawns",
                        serverPlayer.getName(), lrData.getRespawns()), false));

        return Command.SINGLE_SUCCESS;
    }

    private static int getRespawns(CommandSourceStack commandSourceStack, Collection<GameProfile> gameProfiles) {
        gameProfiles.forEach(gameProfile ->
             LRUtil.getDataHolderFromProfile(commandSourceStack.getServer(), gameProfile).ifPresent(dataHolder ->
                     commandSourceStack.sendSystemMessage(
                             Component.translatable("chat.message.limitedrespawns.get_respawns",
                                     gameProfile.getName(), dataHolder.getValue(LRConstants.RESPAWNS)))));

        return Command.SINGLE_SUCCESS;
    }
}