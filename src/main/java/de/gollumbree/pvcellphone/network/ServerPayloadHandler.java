package de.gollumbree.pvcellphone.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    public static void handleDataOnMain(final NameCallData data, final IPayloadContext context) {
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
        PacketDistributor.sendToPlayer(targetPlayer, new NameCallData(sender.getName().getString()));
    }
}
