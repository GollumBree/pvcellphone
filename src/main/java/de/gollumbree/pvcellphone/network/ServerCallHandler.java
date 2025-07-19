package de.gollumbree.pvcellphone.network;

import java.util.ArrayList;
import java.util.HashSet;
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
import su.plo.voice.api.server.player.VoicePlayer;
import su.plo.voice.groups.GroupsManager;
import su.plo.voice.groups.group.Group;

public class ServerCallHandler {
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
            context.player().displayClientMessage(Component.literal("Target Player not Found!"), false);
            // Target player is not online, cannot send packet
            return;
        }
        GroupsManager groupManager = Main.pvgroupsAddon().getGroupManager();
        Group group = groupManager.getGroupByPlayer().get(sender.getUUID());
        if (group == null) {
            // If no group name is provided, create a new group with a random UUID
            UUID groupUuid = UUID.randomUUID();
            String groupName = groupUuid.toString().replaceAll("-", "");

            ServerPlayerSet serverPlayerSet = groupManager.getSourceLine().getPlayerSetManager().createBroadcastSet();
            group = new Group(serverPlayerSet, groupUuid, groupName, (String) null,
                    false,
                    new HashSet<UUID>(Set.of(sender.getUUID())),
                    new ArrayList<McGameProfile>(List.of()),
                    (McGameProfile) null);
            groupManager.getGroups().put(groupUuid, group);

            VoicePlayer voicePlayer = Main.pVoiceServer().getPlayerManager().getPlayerById(sender.getUUID())
                    .orElse(null);
            if (voicePlayer == null) {
                context.disconnect(Component.literal("Player not found in Voice Server!"));
                return;
            }

            group.setOwner(voicePlayer.getInstance().getGameProfile());
            groupManager.join(voicePlayer, group);
        }
        PacketDistributor.sendToPlayer(targetPlayer,
                new CallData(sender.getName().getString(), group.getId().toString()));
    }
}
