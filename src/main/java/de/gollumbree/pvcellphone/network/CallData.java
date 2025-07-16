package de.gollumbree.pvcellphone.network;

import de.gollumbree.pvcellphone.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CallData(String playerName, String groupName) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CallData> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "call_data"));

    public static final StreamCodec<FriendlyByteBuf, CallData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CallData::playerName,
            ByteBufCodecs.STRING_UTF8,
            CallData::groupName,
            CallData::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
