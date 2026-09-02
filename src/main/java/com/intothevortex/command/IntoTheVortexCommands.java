package com.intothevortex.command;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.item.ModItems;
import com.intothevortex.item.TardisLinking;
import com.intothevortex.tardis.TardisData;
import com.intothevortex.tardis.TardisManager;
import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorRegistry;
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
import com.intothevortex.network.RuntimeDimensionPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import com.intothevortex.exterior.TardisAnimationManager;
import com.intothevortex.tardis.TardisTravelManager;
import com.intothevortex.tardis.TardisTeleportCooldowns;

public final class IntoTheVortexCommands {
    private IntoTheVortexCommands() {}

    public static void initialize() { CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher)); }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        SuggestionProvider<CommandSourceStack> tardisSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(TardisManager.ids(context.getSource().getServer()).stream().map(UUID::toString), builder);
        SuggestionProvider<CommandSourceStack> exteriorSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(ExteriorRegistry.values().stream().map(value -> value.id().toString()), builder);
        SuggestionProvider<CommandSourceStack> interiorSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(InteriorRegistry.registered().stream().map(Object::toString), builder);
        SuggestionProvider<CommandSourceStack> doorAnimationSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(TardisAnimationManager.doorIds().stream().map(Object::toString).toList(), builder);
        SuggestionProvider<CommandSourceStack> phaseAnimationSuggestions = (context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(TardisAnimationManager.phaseValues().stream().map(value -> value.id().toString()).toList(), builder);
        var root = Commands.literal("intothevortex").requires(source -> source.permissions().hasPermission(net.minecraft.server.permissions.Permissions.COMMANDS_MODERATOR));
        root.then(Commands.literal("summon").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UUID id;
            try {
                id = UUID.fromString(StringArgumentType.getString(context, "id"));
            } catch (IllegalArgumentException exception) {
                context.getSource().sendFailure(Component.literal("Invalid TARDIS UUID."));
                return 0;
            }
            if (com.intothevortex.dimension.TardisDimensionManager.id(player.level().dimension()) != null) {
                context.getSource().sendFailure(Component.literal("You cannot summon a TARDIS from inside a TARDIS."));
                return 0;
            }
            if (!TardisTravelManager.summon(player, id)) {
                context.getSource().sendFailure(Component.literal("That TARDIS cannot travel to a safe location right now."));
                return 0;
            }
            context.getSource().sendSuccess(() -> Component.literal("TARDIS " + id.toString().substring(0, 7) + " is on the way."), false);
            return 1;
        })));
        root.then(Commands.literal("link").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
            if (TardisManager.get(context.getSource().getServer(), id) == null) return 0;
            if (!(player.getMainHandItem().getItem() instanceof com.intothevortex.item.LinkableItem linkable)) return 0;
            linkable.link(player.getMainHandItem(), id);
            context.getSource().sendSuccess(() -> Component.literal("TARDIS key linked to " + id), false);
            return 1;
        })));
        var teleportCommand = Commands.literal("teleport").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).then(Commands.argument("target", StringArgumentType.word()).suggests((context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(java.util.List.of("interior", "exterior"), builder)).executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
            TardisData data = TardisManager.get(context.getSource().getServer(), id);
            if (data == null) { context.getSource().sendFailure(Component.literal("Unknown TARDIS: " + id)); return 0; }
            var level = TardisDimensionManager.ensureLoaded(context.getSource().getServer(), id);
            String target = StringArgumentType.getString(context, "target");
            if (target.equals("exterior")) {
                level = context.getSource().getServer().getLevel(TardisDimensionManager.parseDimension(data.dimension()));
                if (level == null) { context.getSource().sendFailure(Component.literal("Exterior dimension is not loaded.")); return 0; }
                double yaw = Math.toRadians(data.yaw());
                var destination = new net.minecraft.world.phys.Vec3(data.position().getX() + 0.5D - Math.sin(yaw) * 1.8D, data.position().getY(), data.position().getZ() + 0.5D + Math.cos(yaw) * 1.8D);
                var targetLevel = level;
                context.getSource().getServer().execute(() -> {
                    if (player.connection != null) {
                        TardisTeleportCooldowns.clear(player.getUUID());
                        player.teleportTo(targetLevel, destination.x, destination.y, destination.z, java.util.Set.of(), net.minecraft.util.Mth.wrapDegrees(data.yaw() + 180.0F), 0.0F, false);
                    }
                });
            } else {
                context.getSource().getServer().execute(() -> TardisDimensionManager.whenInteriorReady(context.getSource().getServer(), id, targetLevel -> {
                    var door = TardisDimensionManager.interiorDoor(targetLevel);
                    if (door == null) {
                        context.getSource().sendFailure(Component.literal("TARDIS interior doorway is not available."));
                    } else if (player.connection != null) {
                        var arrival = TardisDimensionManager.interiorArrival(targetLevel, door);
                        var facing = targetLevel.getBlockState(door).getValue(com.intothevortex.interior.InteriorDoorBlock.FACING);
                        player.teleportTo(targetLevel, arrival.x, arrival.y, arrival.z, java.util.Set.of(), facing.toYRot(), 0.0F, false);
                    }
                }));
            }
            return 1;
        })));
        root.then(teleportCommand);
        root.then(Commands.literal("change").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).then(Commands.argument("target", StringArgumentType.word()).suggests((context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(java.util.List.of("interior", "exterior"), builder)).then(Commands.argument("value", StringArgumentType.greedyString()).suggests((context, builder) -> {
            String target = StringArgumentType.getString(context, "target");
            return (target.equals("interior") ? interiorSuggestions : exteriorSuggestions).getSuggestions(context, builder);
        }).executes(context -> {
            UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
            TardisData data = TardisManager.get(context.getSource().getServer(), id);
            if (data == null) return 0;
            String target = StringArgumentType.getString(context, "target").toLowerCase(java.util.Locale.ROOT);
            String value = StringArgumentType.getString(context, "value").trim();
            if (target.equals("interior")) {
                if (!InteriorRegistry.registered().contains(net.minecraft.resources.Identifier.parse(value))) return 0;
                TardisManager.switchInterior(context.getSource().getServer(), id, value);
                TardisDimensionManager.replaceInterior(context.getSource().getServer(), id);
            } else {
                net.minecraft.resources.Identifier exteriorId;
                try {
                    exteriorId = net.minecraft.resources.Identifier.parse(value);
                } catch (IllegalArgumentException exception) {
                    exteriorId = ExteriorRegistry.DEFAULT_ID;
                }
                if (ExteriorRegistry.get(exteriorId).id().equals(ExteriorRegistry.DEFAULT_ID) && !exteriorId.equals(ExteriorRegistry.DEFAULT_ID)) exteriorId = ExteriorRegistry.DEFAULT_ID;
                TardisManager.save(context.getSource().getServer(), data.withExteriorType(exteriorId.toString()));
                var interiorLevel = TardisDimensionManager.ensureLoaded(context.getSource().getServer(), id);
                if (interiorLevel != null) com.intothevortex.interior.InteriorDoorBlock.syncExterior(interiorLevel, id);
                value = exteriorId.toString();
            }
            String changedValue = value;
            context.getSource().sendSuccess(() -> Component.literal("TARDIS " + id + " changed " + target + " to " + changedValue), false);
            return 1;
        })))));
        root.then(Commands.literal("change").then(Commands.argument("id", StringArgumentType.word()).suggests(tardisSuggestions).then(Commands.literal("animation").then(Commands.literal("door").then(Commands.argument("value", StringArgumentType.word()).suggests(doorAnimationSuggestions).executes(context -> changeAnimation(context, "door")))).then(Commands.literal("demat").then(Commands.argument("value", StringArgumentType.word()).suggests(phaseAnimationSuggestions).executes(context -> changeAnimation(context, "demat")))).then(Commands.literal("mat").then(Commands.argument("value", StringArgumentType.word()).suggests(phaseAnimationSuggestions).executes(context -> changeAnimation(context, "mat")))))));
        dispatcher.register(root);
    }

    private static int changeAnimation(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context, String channel) {
        UUID id = UUID.fromString(StringArgumentType.getString(context, "id"));
        TardisData data = TardisManager.get(context.getSource().getServer(), id);
        Identifier value = Identifier.parse(StringArgumentType.getString(context, "value"));
        if (data == null || (channel.equals("door") ? !TardisAnimationManager.hasDoor(value) : !TardisAnimationManager.hasPhase(value))) return 0;
        String door = channel.equals("door") ? value.toString() : data.doorAnimation();
        String demat = channel.equals("demat") ? value.toString() : data.dematAnimation();
        String mat = channel.equals("mat") ? value.toString() : data.matAnimation();
        TardisManager.save(context.getSource().getServer(), data.withAnimations(door, demat, mat));
        context.getSource().sendSuccess(() -> Component.literal("TARDIS " + id + " " + channel + " animation set to " + value), false);
        return 1;
    }
}
