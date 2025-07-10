package de.gollumbree.pvcellphone.items;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.NoteBlockEvent.Play;

public class CellphoneItem extends Item {
    public boolean incomingCall = true;
    float ringtoneVolume = 1.0f; // Placeholder for ringtone volume
    float ringtonePitch = 1.0f; // Placeholder for ringtone pitch
    public ResourceLocation ringtone = ResourceLocation.fromNamespaceAndPath("pvcellphone", "ringtone1");

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
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    public void call(@Nonnull Player player) {
        incomingCall = true;
        SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(ringtone);
        player.displayClientMessage(Component.translatable("pvcellphone.cellphone.called"), true);
        if (sound != null) {
            player.playSound(sound, ringtoneVolume, ringtonePitch);
        }
    }
}
