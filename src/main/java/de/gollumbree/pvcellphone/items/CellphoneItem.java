package de.gollumbree.pvcellphone.items;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.gollumbree.pvcellphone.Ringtones;
import de.gollumbree.pvcellphone.network.JoinData;
import net.minecraft.client.Minecraft;
// import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
// import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class CellphoneItem extends Item {
    // maybe after a Timeout
    private boolean incomingCall = false;
    Player player; // Placeholder for the player who is calling
    float ringtoneVolume = 1.0f; // Placeholder for ringtone volume
    float ringtonePitch = 1.0f; // Placeholder for ringtone pitch
    UUID groupUuid = null; // Placeholder for group ID

    public CellphoneItem(Properties props) {
        super(props); // Register the data component
        NeoForge.EVENT_BUS.register(this); // Register this item for events
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        if (level.isClientSide) {
            this.player = player; // Set the player who is using the cellphone
            new CellphoneScreen(Component.translatable("screen.pvcellphone.cellphone.title"),
                    incomingCall, this).open();
            // call(player);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public void call(@Nonnull Player player, String callerPlayerName, String groupId) {
        if (incomingCall) {
            return; // Already in a call, do not initiate another
        }
        incomingCall = true;
        this.groupUuid = UUID.fromString(groupId); // Set the group ID for the call
        this.player = player;
        player.displayClientMessage(
                Component.translatable("pvcellphone.cellphone.called.chat", Component.literal(callerPlayerName)),
                false);
        player.displayClientMessage(
                Component.translatable("pvcellphone.cellphone.called.actionbar", Component.literal(callerPlayerName)),
                true);
        // System.out.println("Calling player: " + player + " with Sound: " +
        // Ringtones.RINGTONE0.get()
        // + " Volume: " + ringtoneVolume + " Pitch: " + ringtonePitch);
        player.playSound(Ringtones.RINGTONE0.get(), ringtoneVolume, ringtonePitch);
    }

    public void stopCall() {
        if (!incomingCall || player == null) {
            // System.out.println("No incoming call to stop: " + incomingCall + ", Player: "
            // + player);
            return; // No call to stop
        }
        incomingCall = false; // Reset incoming call state
        // System.out.println("Call stopped!");
        Minecraft.getInstance().getSoundManager().stop(Ringtones.RINGTONE0.getId(), SoundSource.PLAYERS);
        // Additional logic to handle call termination can be added here
    }

    public void joinGroup() {
        PacketDistributor.sendToServer(
                new JoinData(player.getUUID().toString(), groupUuid.toString()));
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        incomingCall = false; // Reset incoming call state when player joins
        groupUuid = null; // Reset group ID
    }
}
