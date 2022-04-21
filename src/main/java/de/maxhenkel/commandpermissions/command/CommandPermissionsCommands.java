package de.maxhenkel.commandpermissions.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.maxhenkel.commandpermissions.CommandPermissions;
import de.maxhenkel.commandpermissions.config.PermissionConfig;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class CommandPermissionsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        LiteralArgumentBuilder<CommandSourceStack> literalBuilder = Commands.literal("cp");

        literalBuilder.then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("node", StringArgumentType.string()).executes((context) -> {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            String node = StringArgumentType.getString(context, "node");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(new TextComponent("Internal error"));
            }

            boolean added = permissions.addPermission(player, node);

            if (added) {
                context.getSource().sendSuccess(new TextComponent("Successfully added permission"), false);
            } else {
                context.getSource().sendFailure(new TextComponent("This player already has this permission"));
            }

            return 1;
        }))));

        literalBuilder.then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("node", StringArgumentType.string()).executes((context) -> {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            String node = StringArgumentType.getString(context, "node");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(new TextComponent("Internal error"));
            }

            boolean removed = permissions.removePermission(player, node);

            if (removed) {
                context.getSource().sendSuccess(new TextComponent("Successfully removed permission"), false);
            } else {
                context.getSource().sendFailure(new TextComponent("This player does not have this permission"));
            }

            return 1;
        }))));

        literalBuilder.then(Commands.literal("list").then(Commands.argument("player", EntityArgument.player()).executes((context) -> {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");

            PermissionConfig permissions = CommandPermissions.PERMISSIONS;

            if (permissions == null) {
                throw new CommandRuntimeException(new TextComponent("Internal error"));
            }

            List<String> perms = permissions.getPermissions(player);

            if (perms.isEmpty()) {
                context.getSource().sendSuccess(new TextComponent("This player does not have any permissions"), false);
                return 0;
            }

            context.getSource().sendSuccess(new TextComponent("Permissions: \n  %s".formatted(String.join("\n  ", perms))), false);

            return perms.size();
        })));

        dispatcher.register(literalBuilder);
    }

}
