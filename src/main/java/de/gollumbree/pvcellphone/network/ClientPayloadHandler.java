package de.gollumbree.pvcellphone.network;

import javax.annotation.Nullable;

import de.gollumbree.pvcellphone.compat.smartphoneCompat;
import de.gollumbree.pvcellphone.items.CellphoneItem;
// import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
// import net.neoforged.api.distmarker.OnlyIn;
// import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleDataOnMain(final CallData data, final IPayloadContext context) {
        Player reciever = context.player();

        Inventory inv = reciever.getInventory();
        assert inv != null;

        @Nullable
        CellphoneItem cellphone = inv.items.stream().map(ItemStack::getItem)
                .filter(item -> item instanceof CellphoneItem).map(item -> (CellphoneItem) item)
                .findFirst().orElse(null);
        if (cellphone != null) {
            cellphone.call(reciever, data.playerName(), data.groupName());
            return;
        }

        if (ModList.get().isLoaded("smartphone")) {
            cellphone = smartphoneCompat.callSmartphone(inv);
            if (cellphone != null) {
                cellphone.call(reciever, data.playerName(), data.groupName());
                return;
            }
        }
        // context.player().displayClientMessage((Component.literal("Player has no
        // Cellphone!")), false);

        // No compatible smartphone found, cannot send packet
    }
}
