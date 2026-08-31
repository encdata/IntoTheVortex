package com.intothevortex.command;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.item.ModItems;
import com.intothevortex.item.TardisLinking;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.TeleportTransition;
import java.util.UUID;

public final class IntoTheVortexCommands {
    private IntoTheVortexCommands() {}

    public static void initialize() { CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher)); }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        SuggestionProvider<CommandSourceStack> tardisSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(TardisManager.ids(context.getSource().getServer()).stream().map(UUID::toString), builder);
        var root = Commands.literal("intothevortex");
        root.then(Commands.literal("key").executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.getInventory().placeItemBackInInventory(new ItemStack(ModItems.TARDIS_KEY));
            context.getSource().sendSuccess(() -> Component.literal("TARDIS key given."), false);
            return 1;
        }));
        root.then(Commands.literal("link").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
            if (TardisManager.get(context.getSource().getServer(), id) == null) return 0;
            TardisLinking.link(player.getMainHandItem(), id);
            context.getSource().sendSuccess(() -> Component.literal("TARDIS key linked to " + id), false);
            return 1;
        })));
        root.then(Commands.literal("tp").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).then(Commands.argument("target", StringArgumentType.word()).suggests((context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(java.util.List.of("interior", "exterior"), builder)).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
            TardisData data = TardisManager.get(context.getSource().getServer(), id);
            if (data == null) return 0;
            var level = TardisDimensionManager.ensureLoaded(context.getSource().getServer(), id);
            String target = StringArgumentType.getString(context, "target");
            if (target.equals("exterior")) {
                level = context.getSource().getServer().getLevel(TardisDimensionManager.parseDimension(data.dimension()));
                if (level == null) return 0;
                var pos = data.position().getCenter();
                var destination = new net.minecraft.world.phys.Vec3(pos.x + 0.5D, pos.y, pos.z + 1.8D);
                var targetLevel = level;
                context.getSource().getServer().execute(() -> player.teleport(new TeleportTransition(targetLevel, destination, net.minecraft.world.phys.Vec3.ZERO, data.yaw(), 0.0F, TeleportTransition.DO_NOTHING)));
            } else {
                var targetLevel = level;
                context.getSource().getServer().execute(() -> player.teleport(new TeleportTransition(targetLevel, new net.minecraft.world.phys.Vec3(0.5D, 65D, 0.5D), net.minecraft.world.phys.Vec3.ZERO, data.yaw(), 0.0F, TeleportTransition.DO_NOTHING)));
            }
            return 1;
        }))));
        dispatcher.register(root);
    }
}
