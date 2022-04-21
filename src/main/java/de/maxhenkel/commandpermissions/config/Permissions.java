package de.maxhenkel.commandpermissions.config;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Permissions {

    private int version;
    private List<User> users;

    public Permissions(int version, List<User> users) {
        this.version = version;
        this.users = users;
    }

    public Permissions(List<User> users) {
        this(1, users);
    }

    public Permissions() {
        this(1, new ArrayList<>());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public static class User {

        @Nullable
        private String username;
        @Nullable
        private UUID uuid;
        private List<String> permissions;

        public User(String username, UUID uuid, List<String> permissions) {
            this.username = username;
            this.uuid = uuid;
            this.permissions = permissions;
        }

        public User(String username, UUID uuid) {
            this(username, uuid, new ArrayList<>());
        }

        public User() {
            permissions = new ArrayList<>();
        }

        @Nullable
        public String getUsername() {
            return username;
        }

        public void setUsername(@Nullable String username) {
            this.username = username;
        }

        @Nullable
        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(@Nullable UUID uuid) {
            this.uuid = uuid;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }

}
