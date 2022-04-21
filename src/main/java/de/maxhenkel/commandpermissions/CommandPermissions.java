package de.maxhenkel.commandpermissions;

import de.maxhenkel.commandpermissions.command.CommandPermissionsCommands;
import de.maxhenkel.commandpermissions.config.PermissionConfig;
import de.maxhenkel.commandpermissions.config.ServerConfig;
import de.maxhenkel.configbuilder.ConfigBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.nio.file.Paths;

public class CommandPermissions implements ModInitializer {

    public static final String MODID = "commandpermissions";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ServerConfig SERVER_CONFIG;
    @Nullable
    public static PermissionConfig PERMISSIONS;

    @Override
    public void onInitialize() {
        SERVER_CONFIG = ConfigBuilder.build(Paths.get(".", "config", MODID, "%s.properties".formatted(MODID)), true, ServerConfig::new);

        if (SERVER_CONFIG.permissionSystem.get().equals(ServerConfig.PermissionSystem.BUILTIN)) {
            PERMISSIONS = new PermissionConfig(Paths.get(".", "config", MODID, "permissions.json").toFile());
            CommandRegistrationCallback.EVENT.register(CommandPermissionsCommands::register);
        }

    }
}
