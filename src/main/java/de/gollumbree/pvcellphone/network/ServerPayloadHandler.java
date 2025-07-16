package de.gollumbree.pvcellphone.network;

import java.util.UUID;

// import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
// import net.neoforged.api.distmarker.OnlyIn;
// import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleDataOnMain(final CallData data, final IPayloadContext context) {
        Player sender = context.player();
        MinecraftServer server = sender.getServer();
        if (server == null) {
            context.disconnect(Component.literal("Server not reachable!")); // Server is not available, cannot send
                                                                            // packet
            return;
        }
        ServerPlayer targetPlayer = server.getPlayerList().getPlayerByName(data.playerName());
        if (targetPlayer == null) {
            context.player().displayClientMessage(Component.literal("Target Player not Online!"), false);
            // Target player is not online, cannot send packet
            return;
        }

        String groupName = data.groupName();
        if (groupName.isEmpty()) {
            // If no group name is provided, create a new group with a random UUID
            groupName = UUID.randomUUID().toString().replaceAll("-", "");
            // CommandSourceStack senderCommandSource = sender.createCommandSourceStack();
            // TODO: create the group in a way that gives the id
            // server.getCommands().performPrefixedCommand(senderCommandSource,
            // "groups create name:" + groupName);
        }

        PacketDistributor.sendToPlayer(targetPlayer, new CallData(sender.getName().getString(), groupName));
    }
}
