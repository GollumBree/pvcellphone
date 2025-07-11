package de.gollumbree.pvcellphone.items;

import javax.annotation.Nonnull;

import de.gollumbree.pvcellphone.Ringtones;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CellphoneItem extends Item {
    private boolean incomingCall = false;
    private Player player; // Placeholder for the player who is calling
    float ringtoneVolume = 1.0f; // Placeholder for ringtone volume
    float ringtonePitch = 1.0f; // Placeholder for ringtone pitch

    public CellphoneItem(Properties props) {
        super(props); // Register the data component
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        if (level.isClientSide) {
            Minecraft.getInstance()
                    .setScreen(new CellphoneScreen(Component.translatable("screen.pvcellphone.cellphone.title"),
                            incomingCall, this));
            // call(player);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public void call(@Nonnull Player player, String callerPlayerName) {
        incomingCall = true;
        this.player = player;
        player.displayClientMessage(Component.literal("Call by: " + callerPlayerName), false);
        player.displayClientMessage(Component.translatable("pvcellphone.cellphone.called.chat"), false);
        player.displayClientMessage(Component.translatable("pvcellphone.cellphone.called.actionbar"), true);
        System.out.println("Calling player: " + player + " with Sound: " + Ringtones.RINGTONE0.get()
                + " Volume: " + ringtoneVolume + " Pitch: " + ringtonePitch);
        player.playSound(Ringtones.RINGTONE0.get(), ringtoneVolume, ringtonePitch);
    }

    public void stopCall() {
        if (!incomingCall || player == null) {
            System.out.println("No incoming call to stop: " + incomingCall + ", Player: " + player);
            return; // No call to stop
        }
        incomingCall = false; // Reset incoming call state
        System.out.println("Call stopped!");
        Minecraft.getInstance().getSoundManager().stop(Ringtones.RINGTONE0.getId(), SoundSource.PLAYERS);
        // Additional logic to handle call termination can be added here
    }
}
