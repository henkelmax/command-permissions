package de.maxhenkel.commandpermissions.permission;

import de.maxhenkel.commandpermissions.CommandPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

public class FabricPermissionManager {

    public static boolean hasPermission(ServerPlayer player, String node, boolean defaultValue) {
        if (isFabricPermissionsAPILoaded()) {
            return Permissions.check(player, node, defaultValue);
        }
        return defaultValue;
    }

    private static Boolean loaded;

    private static boolean isFabricPermissionsAPILoaded() {
        if (loaded == null) {
            loaded = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");
            if (loaded) {
                CommandPermissions.LOGGER.info("Using Fabric Permissions API");
            }else{
                CommandPermissions.LOGGER.warn("Fabric Permissions API not present");
            }
        }
        return loaded;
    }

}
