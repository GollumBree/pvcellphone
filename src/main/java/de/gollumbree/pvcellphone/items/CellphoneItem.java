package de.gollumbree.pvcellphone.items;

import javax.annotation.Nonnull;

import de.gollumbree.pvcellphone.Ringtones;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CellphoneItem extends Item {
    public boolean incomingCall = false;
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
                            incomingCall));
            // call(player);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public void call(@Nonnull Player player) {
        incomingCall = true;
        player.displayClientMessage(Component.translatable("pvcellphone.cellphone.called.chat"), false);
        player.displayClientMessage(Component.translatable("pvcellphone.cellphone.called.actionbar"), true);
        System.out.println("Calling player: " + player + " with Sound: " + Ringtones.RINGTONE0.get()
                + " Volume: " + ringtoneVolume + " Pitch: " + ringtonePitch);
        player.playSound(Ringtones.RINGTONE0.get(), ringtoneVolume, ringtonePitch);
    }
}
