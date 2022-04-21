package de.maxhenkel.commandpermissions.permission;

import de.maxhenkel.commandpermissions.CommandPermissions;
import net.minecraft.server.level.ServerPlayer;

public class UniversalPermissionManager {

    public static boolean hasPermission(ServerPlayer player, String permission, boolean defaultValue) {
        return switch (CommandPermissions.SERVER_CONFIG.permissionSystem.get()) {
            case BUILTIN -> CommandPermissions.PERMISSIONS == null ? defaultValue : CommandPermissions.PERMISSIONS.hasPermission(player, permission);
            case FABRIC_PERMISSIONS_API -> FabricPermissionManager.hasPermission(player, permission, defaultValue);
            default -> defaultValue;
        };
    }

}
