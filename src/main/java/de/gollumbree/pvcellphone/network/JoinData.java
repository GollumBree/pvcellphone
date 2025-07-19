package de.gollumbree.pvcellphone.network;

import de.gollumbree.pvcellphone.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record JoinData(
        String playerId, String groupId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<JoinData> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "join_data"));

    public static final StreamCodec<FriendlyByteBuf, JoinData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            JoinData::playerId,
            ByteBufCodecs.STRING_UTF8,
            JoinData::groupId,
            JoinData::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
