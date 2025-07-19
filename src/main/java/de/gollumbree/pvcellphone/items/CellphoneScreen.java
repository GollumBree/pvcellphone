package de.gollumbree.pvcellphone.items;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import de.gollumbree.pvcellphone.Main;
import de.gollumbree.pvcellphone.network.CallData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CellphoneScreen extends Screen {
    private final CellphoneItem cellphoneItem;
    private boolean incomingCall;
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
    private int leftPos;
    private int topPos;
    private EditBox textField;

    private int centerX;
    private int centerY;

    private final int imageHeight = 512;
    private final int imageWidth = 256;

    public CellphoneScreen(Component title, boolean incomingCall, CellphoneItem cellphone) {
        super(title);
        this.incomingCall = incomingCall;
        this.cellphoneItem = cellphone;
        minecraft = getMinecraft();
    }

    @Override
    protected void init() {

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        centerX = width / 2;
        centerY = height / 2;

        final int ButtonSize = 30;
        final Size2i TextFieldSize = new Size2i(250, 20);
        final int buttonOffsetX = 25;
        final int buttonOffsetY = 20;

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
                    centerX - 125, // x
                    topPos + 3, // y
                    TextFieldSize.width, // width
                    TextFieldSize.height, // height
                    Component.literal("Enter Playername"));
            textField.setMaxLength(50);
            textField.setResponder(text -> {
                textField.setSuggestion(playerAutoComplete(text));
            });
            addRenderableWidget(textField);
            this.textField = textField;
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
        // System.out.println("Calling player: " + playerName);
        // send packet to server or handle the call logic here
        PacketDistributor
                .sendToServer(new CallData(playerName, ""));
        this.onClose();
    }

    private void Accept() {
        cellphoneItem.stopCall();
        cellphoneItem.joinGroup();
        // System.out.println("Call accepted!");
        this.onClose();
        // Put players in Group
    }

    private void Decline() {
        cellphoneItem.stopCall();
        // System.out.println("Call declined!");
        // assert minecraft != null;
        this.onClose();
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
        graphics.blit(BACKGROUND_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @SuppressWarnings("null")
    private String playerAutoComplete(String text) {
        assert minecraft != null;
        assert minecraft.level != null;
        return minecraft.level.players().stream().map(player -> player.getName().getString())
                .filter(name -> !name.equals(cellphoneItem.player.getName().getString()))
                .filter(name -> name.toLowerCase().startsWith(text.toLowerCase()))
                .map(name -> name.substring(text.length())).findFirst().orElse("");
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textField != null && keyCode == GLFW.GLFW_KEY_TAB) {
            textField.setValue(textField.getValue() + playerAutoComplete(textField.getValue()));
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public void open() {
        Minecraft.getInstance().setScreen(this);
    }

    // private Optional<UUID> getCurrentGroupId() {
    // GroupsManager groupManager = Main.pvgroupsAddon().getGroupManager(); // cant
    // be called on client side
    // if (groupManager == null || minecraft == null || minecraft.player == null) {
    // return Optional.empty();
    // }
    // return
    // Optional.ofNullable(groupManager.getGroupByPlayer().get(minecraft.player.getUUID()))
    // .map(group -> group.getId());
    // }

}
