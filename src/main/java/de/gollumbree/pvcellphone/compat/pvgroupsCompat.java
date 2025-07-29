package de.gollumbree.pvcellphone.compat;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import su.plo.voice.groups.GroupsAddon;
import su.plo.voice.server.ModVoiceServer;

@OnlyIn(Dist.DEDICATED_SERVER)
public class pvgroupsCompat {
    private static GroupsAddon groupsAddon = null;
    private static ModVoiceServer modVoiceServer = null;

    public static ModVoiceServer pVoiceServer() {
        if (modVoiceServer == null) {
            modVoiceServer = ModVoiceServer.INSTANCE;
        }
        return modVoiceServer;
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static GroupsAddon pvgroupsAddon() {
        if (groupsAddon == null) {
            groupsAddon = (GroupsAddon) pVoiceServer().getAddonManager()
                    .getAddon("pv-addon-groups")
                    .orElseThrow().getInstance().orElseThrow();
        }
        return groupsAddon;
    }
}
