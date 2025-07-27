package de.gollumbree.pvcellphone.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.gollumbree.pvcellphone.items.CellphoneItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import de.gollumbree.smartphone.compat.ItemFinder;

public class smartphoneCompat {
    public static @Nullable CellphoneItem callSmartphone(@Nonnull Inventory inv) {
        @Nullable
        ItemStack cellphoneStack = ItemFinder.findItem(inv, CellphoneItem.class);
        return cellphoneStack != null && cellphoneStack.getItem() instanceof CellphoneItem cellphone ? cellphone : null;
    }
}
