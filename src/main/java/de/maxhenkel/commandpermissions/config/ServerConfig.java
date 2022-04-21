package de.maxhenkel.commandpermissions.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.ConfigEntry;

public class ServerConfig {

    public final ConfigEntry<PermissionSystem> permissionSystem;

    public ServerConfig(ConfigBuilder builder) {
        permissionSystem = builder.enumEntry("permission_system", PermissionSystem.FABRIC_PERMISSIONS_API);
    }

    public static enum PermissionSystem {
        VANILLA, BUILTIN, FABRIC_PERMISSIONS_API
    }

}
