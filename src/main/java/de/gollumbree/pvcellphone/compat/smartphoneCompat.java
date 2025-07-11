package de.gollumbree.pvcellphone.compat;

import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.gollumbree.pvcellphone.items.CellphoneItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import de.gollumbree.smartphone.items.SmartphoneItem;

public class smartphoneCompat {
    public static @Nullable CellphoneItem callSmartphone(@Nonnull Inventory inv) {
        Stream<ItemStack> smartphoneContents = inv.items.stream() // Stream all items in the inventory
                .filter(itemStack -> itemStack.getItem() instanceof SmartphoneItem) // Filter Smartphones
                .map(item -> item.get(DataComponents.CONTAINER)) // get their contents
                .filter(contents -> contents != null).flatMap(ItemContainerContents::stream); // Discard null contents
                                                                                              // and flatten the stream

        return smartphoneContents
                .map(ItemStack::getItem)
                .filter(item -> item instanceof CellphoneItem) // Filter CellphoneItems from the smartphone contents
                .map(item -> (CellphoneItem) item) // Cast to CellphoneItem
                .findFirst() // Get the first CellphoneItem found
                .orElse(null); // Return null if no CellphoneItem is found
    }
}
