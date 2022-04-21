# Command Permissions

This is a server-side Fabric mod, adding permissions for all commands.

> This mod requires [Fabric Loader](https://fabricmc.net/use/installer/) 0.14.0 or later!

## Permission Nodes

This mod adds permission nodes for every command.
The pattern for command permission nodes is `command.<command-name>`.
For example `command.kill` for the `/kill` command.

## Permission Systems

You change the permission system, this mod uses in the config (`/config/commandpermissions/commandpermissions.properties`).
By default `permission_system` is set to `FABRIC_PERMISSIONS_API`.

Possible values are:

- `VANILLA` To have the vanilla command permission behavior
- `BUILTIN` To use the mod built-in permission system (Not recommended)
- `FABRIC_PERMISSIONS_API` To use the [fabric-permissions-api](https://github.com/lucko/fabric-permissions-api)


### Using the Fabric Permissions API

The Fabric Permissions API is used by almost any permission system.

The recommended permission system to use with this mod is [LuckPerms](https://www.curseforge.com/minecraft/mc-mods/luckperms).

For more information on how to use LuckPerms, read [their wiki page](https://luckperms.net/wiki/Home).

### Using the Builtin Permission System

When having the built-in permission system active, you have access to the following commands:

- `/cp add <playername> <permission-node>` Adds the permission node to the player
- `/cp remove <playername> <permission-node>` Removes the permission node from the player
- `/cp list <playername> <permission-node>` Lists all permissions, the player has

By default, only OPs have permissions to use any command.
You need to manually give permissions for every command to each player.

This system is not recommended, since it has a **very** limited set of features:

- There are no permission groups
- OPs always have all permissions
- Players can't have any permissions by default
- No support for wildcards like `command.*`
- No permission hierarchy
- Permissions are stored in a `.json` file
- It might contain severe bugs, since this is only a quick and dirty solution

It is strongly recommended to use the `FABRIC_PERMISSIONS_API` setting!
