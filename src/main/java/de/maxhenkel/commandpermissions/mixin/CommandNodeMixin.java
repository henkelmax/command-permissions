package de.maxhenkel.commandpermissions.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.maxhenkel.commandpermissions.permission.UniversalPermissionManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CommandNode.class, remap = false)
public abstract class CommandNodeMixin<S> implements Comparable<CommandNode<S>> {

    @Inject(method = "canUse", at = @At(value = "RETURN"), cancellable = true)
    private void canUse(S source, CallbackInfoReturnable<Boolean> cir) {
        if (!(source instanceof CommandSourceStack stack)) {
            return;
        }
        if (stack.getServer() == null) {
            return;
        }
        CommandDispatcher<CommandSourceStack> dispatcher = stack.getServer().getCommands().getDispatcher();
        CommandNode<S> thisNode = (CommandNode<S>) ((Object) this);
        for (CommandNode<?> node : dispatcher.getRoot().getChildren()) {
            if (!thisNode.equals(node)) {
                continue;
            }

            if (!(node instanceof LiteralCommandNode<?> literalCommandNode)) {
                throw new IllegalStateException("Root command node can only have LiteralCommandNodes as children");
            }

            if (stack.hasPermission(stack.getServer().getOperatorUserPermissionLevel())) {
                // Let OPs use all commands
                continue;
            }

            if (stack.getEntity() instanceof ServerPlayer player) {
                cir.setReturnValue(UniversalPermissionManager.hasPermission(player, "command.%s".formatted(literalCommandNode.getLiteral()), cir.getReturnValue()));
            }

            // Letting the command pass, since it's not from a player
        }

    }

}
