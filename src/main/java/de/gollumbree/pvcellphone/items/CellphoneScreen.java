package de.gollumbree.pvcellphone.items;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.Window;

import de.gollumbree.pvcellphone.Main;
import de.gollumbree.pvcellphone.network.NameCallData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

public class CellphoneScreen extends Screen {
    private final CellphoneItem cellphoneItem;
    private boolean incomingCall;
    @SuppressWarnings("unused")
    private static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(Main.MODID,
            "textures/gui/screen/cellphone/cellphone_screen.png");
    private static final ResourceLocation BUTTONACCEPT_LOCATION_FOCUSED = ResourceLocation.fromNamespaceAndPath(
            Main.MODID,
            "screen/cellphone/cellphone_button_accept_focused");
    private static final ResourceLocation BUTTONACCEPT_LOCATION_UNFOCUSED = ResourceLocation.fromNamespaceAndPath(
            Main.MODID,
            "screen/cellphone/cellphone_button_accept_unfocused");
    private static final ResourceLocation BUTTONDECLINE_LOCATION_FOCUSED = ResourceLocation.fromNamespaceAndPath(
            Main.MODID,
            "screen/cellphone/cellphone_button_decline_focused");
    private static final ResourceLocation BUTTONDECLINE_LOCATION_UNFOCUSED = ResourceLocation.fromNamespaceAndPath(
            Main.MODID,
            "screen/cellphone/cellphone_button_decline_unfocused");
    private final float SCREEN_RATIO = 9f / 18f; // width / height

    private int centerX;
    private int centerY;

    public CellphoneScreen(Component title, boolean incomingCall, CellphoneItem cellphone) {
        super(title);
        this.incomingCall = incomingCall;
        this.cellphoneItem = cellphone;
        // init();
    }

    @Override
    protected void init() {
        minecraft = getMinecraft();

        assert minecraft != null;
        Window window = minecraft.getWindow();
        int windowWidth = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();

        centerX = windowWidth / 2;
        centerY = windowHeight / 2;

        if (windowWidth > windowHeight * SCREEN_RATIO) {
            width = (int) (windowHeight * SCREEN_RATIO);
            height = windowHeight;
        } else {
            width = windowWidth;
            height = (int) (windowWidth / SCREEN_RATIO);
        }

        System.out.println(
                "Window Width: " + windowWidth + ", Window Height: " + windowHeight + ", Screen Width: "
                        + width + ", Screen Height: " + height);

        final int ButtonSize = width / 4;
        final int buttonOffsetX = width / 5;
        final int buttonOffsetY = height / 5;

        if (incomingCall) {
            ImageButton acceptButton = new ImageButton(centerX - buttonOffsetX -
                    ButtonSize
                            / 2,
                    centerY + buttonOffsetY, ButtonSize, ButtonSize,
                    new WidgetSprites(BUTTONACCEPT_LOCATION_UNFOCUSED,
                            BUTTONACCEPT_LOCATION_FOCUSED),
                    btn -> Accept(),
                    Component
                            .empty());
            acceptButton.setTooltip(
                    Tooltip.create(Component.translatable("screen.pvcellphone.cellphone.accept")));

            ImageButton declineButton = new ImageButton(centerX + buttonOffsetX -
                    ButtonSize
                            / 2,
                    centerY + buttonOffsetY, ButtonSize, ButtonSize,
                    new WidgetSprites(BUTTONDECLINE_LOCATION_UNFOCUSED,
                            BUTTONDECLINE_LOCATION_FOCUSED),
                    btn -> Decline(), Component
                            .empty());
            declineButton.setTooltip(
                    Tooltip.create(Component.translatable("screen.pvcellphone.cellphone.decline")));

            addRenderableWidget(acceptButton);
            addRenderableWidget(declineButton);

        } else {
            EditBox textField = new EditBox(
                    font,
                    centerX, // x
                    centerY - 50, // y
                    150, // width
                    20, // height
                    Component.literal("Type Playername"));
            textField.setMaxLength(50);
            textField.setSuggestion("Enter player name");
            textField.setResponder(text -> {
                // textField.setSuggestion();
            });
            addRenderableWidget(textField);
            ImageButton callButton = new ImageButton(centerX - buttonOffsetX -
                    ButtonSize
                            / 2,
                    centerY + buttonOffsetY, ButtonSize, ButtonSize,
                    new WidgetSprites(BUTTONACCEPT_LOCATION_UNFOCUSED,
                            BUTTONACCEPT_LOCATION_FOCUSED),
                    btn -> Call(textField
                            .getValue()),
                    Component
                            .empty());
            addRenderableWidget(callButton);
        }
    }

    private void Call(String playerName) {
        // Call the player with the name in the text field
        System.out.println("Calling player: " + playerName);
        // send packet to server or handle the call logic here
        PacketDistributor
                .sendToServer(new NameCallData(playerName));
        assert minecraft != null;
        minecraft.setScreen(null);
    }

    private void Accept() {
        cellphoneItem.stopCall();
        System.out.println("Call accepted!");
        assert minecraft != null;
        minecraft.setScreen(null);
        // Put players in Group
    }

    private void Decline() {
        cellphoneItem.stopCall();
        System.out.println("Call declined!");
        assert minecraft != null;
        minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // graphics.blit(BACKGROUND_LOCATION, centerX - width / 2, centerY - height / 2,
        // 0, 0, width, height);
        graphics.fill(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2,
                0xFF999999);
    }
}
