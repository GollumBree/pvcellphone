package de.gollumbree.pvcellphone.network;

import de.gollumbree.pvcellphone.compat.pvgroupsCompat;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import su.plo.voice.api.server.player.VoicePlayer;
import su.plo.voice.groups.GroupsAddon;
import su.plo.voice.groups.group.Group;
import su.plo.voice.server.ModVoiceServer;

import java.util.UUID;

public class ServerJoinHandler {
    public static void handleDataOnMain(final JoinData data, final IPayloadContext context) {
        GroupsAddon groupsAddon = pvgroupsCompat.pvgroupsAddon();
        ModVoiceServer voiceServer = pvgroupsCompat.pVoiceServer();
        VoicePlayer player = voiceServer.getPlayerManager().getPlayerById(UUID.fromString(data.playerId()))
                .orElseThrow();
        Group group = groupsAddon.getGroupManager().getGroups().get(UUID.fromString(data.groupId()));
        pvgroupsCompat.pvgroupsAddon().getGroupManager().join(
                player,
                group);
    }
}
