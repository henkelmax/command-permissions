package de.maxhenkel.commandpermissions.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.maxhenkel.commandpermissions.CommandPermissions;
import net.minecraft.world.entity.player.Player;

import java.io.*;
import java.util.*;

public class PermissionConfig {

    private final File file;
    private final Gson gson;
    private Permissions permissions;
    private Map<UUID, Permissions.User> userCache;

    public PermissionConfig(File file) {
        this.file = file;
        this.gson = new GsonBuilder().create();
        this.permissions = new Permissions();
        this.userCache = new HashMap<>();
        load();
    }

    public void load() {
        if (!file.exists()) {
            return;
        }
        try (Reader reader = new FileReader(file)) {
            permissions = gson.fromJson(reader, Permissions.class);
            userCache = new HashMap<>();
        } catch (Exception e) {
            CommandPermissions.LOGGER.error("Failed to load permissions: {}", e.getMessage());
        }
    }

    public void save() {
        file.getParentFile().mkdirs();
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(permissions, writer);
        } catch (Exception e) {
            CommandPermissions.LOGGER.error("Failed to save permissions: {}", e.getMessage());
        }
    }

    public boolean hasPermission(Player player, String permission) {
        return hasPermission(getUser(player), permission);
    }

    private Permissions.User getUser(Player player) {
        Permissions.User user = userCache.get(player.getUUID());
        if (user != null) {
            return user;
        }
        for (Permissions.User u : permissions.getUsers()) {
            if (u.getUuid() != null) {
                if (u.getUuid().equals(player.getUUID())) {
                    String username = u.getUsername();
                    if (username == null || !username.equalsIgnoreCase(player.getGameProfile().getName())) {
                        u.setUsername(player.getGameProfile().getName());
                        save();
                    }
                    userCache.put(u.getUuid(), u);
                    return u;
                }
            } else {
                if (u.getUsername() == null) {
                    continue;
                }
                String userName = player.getGameProfile().getName();
                if (userName.equalsIgnoreCase(u.getUsername())) {
                    u.setUuid(player.getUUID());
                    save();
                    userCache.put(u.getUuid(), u);
                    return u;
                }
            }
        }
        user = new Permissions.User(player.getGameProfile().getName(), player.getUUID());
        permissions.addUser(user);
        save();
        userCache.put(player.getUUID(), user);
        return user;
    }

    public boolean addPermission(Player player, String permission) {
        Permissions.User user = getUser(player);
        for (String p : user.getPermissions()) {
            //TODO handle already having higher permissions
            if (p.equals(permission)) {
                return false;
            }
        }
        user.getPermissions().add(permission);
        save();
        return true;
    }

    public boolean removePermission(Player player, String permission) {
        Permissions.User user = getUser(player);
        boolean removed = user.getPermissions().removeIf(p -> p.equals(permission));
        if (removed) {
            save();
            return true;
        }
        return false;
    }

    public List<String> getPermissions(Player player) {
        Permissions.User user = getUser(player);
        return Collections.unmodifiableList(user.getPermissions());
    }

    private boolean hasPermission(Permissions.User user, String permission) {
        for (String perm : user.getPermissions()) {
            //TODO compare nodes
            if (perm.equals(permission)) {
                return true;
            }
        }
        return false;
    }

}
