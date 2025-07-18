package de.gollumbree.pvcellphone.network;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.gollumbree.pvcellphone.Main;
// import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
// import net.neoforged.api.distmarker.OnlyIn;
// import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import su.plo.slib.api.entity.player.McGameProfile;
import su.plo.voice.api.server.audio.line.ServerPlayerSet;
import su.plo.voice.groups.GroupsManager;
import su.plo.voice.groups.group.Group;

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

        String groupId = data.groupId();
        UUID groupUuid = null;
        if (groupId.isEmpty()) {
            // If no group name is provided, create a new group with a random UUID
            groupUuid = UUID.randomUUID();
            String groupName = groupUuid.toString().replaceAll("-", "");
            GroupsManager groupManager = Main.groupsAddon().groupManager;
            ServerPlayerSet serverPlayerSet = groupManager.getSourceLine().getPlayerSetManager().createBroadcastSet();
            Group group = new Group(serverPlayerSet, groupUuid, groupName, (String) null,
                    false,
                    Set.<UUID>of(sender.getUUID()),
                    List.<McGameProfile>of(),
                    (McGameProfile) null);
            groupManager.getGroups().put(groupUuid, group);
            // TODO: join using the Addon
            // server.getCommands().performPrefixedCommand(sender.createCommandSourceStack(),
            // "groups join " + groupUuid.toString());

            // groupManager.join(serverPlayerSet.getPlayers().stream()
            // .filter(player ->
            // player.getInstance().getUuid().equals(sender.getUUID())).findAny().orElseThrow(),
            // group);
        } else {
            groupUuid = UUID.fromString(groupId);
        }

        PacketDistributor.sendToPlayer(targetPlayer, new CallData(sender.getName().getString(), groupUuid.toString()));
    }
}
