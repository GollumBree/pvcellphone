package de.gollumbree.pvcellphone.network;

import de.gollumbree.pvcellphone.Main;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NameCallData(String playerName) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NameCallData> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "name_call_data"));

    public static final StreamCodec<FriendlyByteBuf, NameCallData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            NameCallData::playerName, NameCallData::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
