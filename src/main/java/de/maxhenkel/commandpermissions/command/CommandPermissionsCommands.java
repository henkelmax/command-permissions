package de.maxhenkel.commandpermissions.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.maxhenkel.commandpermissions.CommandPermissions;
import de.maxhenkel.commandpermissions.config.PermissionConfig;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class CommandPermissionsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {
        LiteralArgumentBuilder<CommandSourceStack> literalBuilder = Commands.literal("cp");

        literalBuilder.then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("node", StringArgumentType.string()).executes((ctx) -> {
            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
            String node = StringArgumentType.getString(ctx, "node");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(Component.literal("Internal error"));
            }

            boolean added = permissions.addPermission(player, node);

            if (added) {
                ctx.getSource().sendSuccess(Component.literal("Successfully added permission"), false);
            } else {
                ctx.getSource().sendFailure(Component.literal("This player already has this permission"));
            }

            return 1;
        }))));

        literalBuilder.then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("node", StringArgumentType.string()).executes((ctx) -> {
            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
            String node = StringArgumentType.getString(ctx, "node");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(Component.literal("Internal error"));
            }

            boolean removed = permissions.removePermission(player, node);

            if (removed) {
                ctx.getSource().sendSuccess(Component.literal("Successfully removed permission"), false);
            } else {
                ctx.getSource().sendFailure(Component.literal("This player does not have this permission"));
            }

            return 1;
        }))));

        literalBuilder.then(Commands.literal("list").then(Commands.argument("player", EntityArgument.player()).executes((ctx) -> {
            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(Component.literal("Internal error"));
            }

            List<String> perms = permissions.getPermissions(player);

            if (perms.isEmpty()) {
                ctx.getSource().sendSuccess(Component.literal("This player does not have any permissions"), false);
                return 0;
            }

            ctx.getSource().sendSuccess(Component.literal("Permissions: \n  %s".formatted(String.join("\n  ", perms))), false);

            return perms.size();
        })));

        dispatcher.register(literalBuilder);
    }

}
