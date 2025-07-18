package de.gollumbree.pvcellphone;

import de.gollumbree.pvcellphone.items.CellphoneItem;
import de.gollumbree.pvcellphone.network.ClientPayloadHandler;
import de.gollumbree.pvcellphone.network.CallData;
import de.gollumbree.pvcellphone.network.ServerPayloadHandler;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import su.plo.voice.groups.GroupsAddon;
import su.plo.voice.server.ModVoiceServer;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Main.MODID)
public class Main {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pvcellphone";
    // Create a Deferred Register to hold Items which will all be registered under
    // the "pvcellphone" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Creates a new item with the id "pvcellphone:cellphone"
    public static final DeferredItem<CellphoneItem> CELLPHONE_ITEM = ITEMS.register("cellphone",
            () -> new CellphoneItem(new Item.Properties().stacksTo(1)));

    private static GroupsAddon groupsAddon = null;

    // The constructor for the mod class is the first code that is run when your mod
    // is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and
    // pass them in automatically.
    public Main(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        // modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Smartphone)
        // to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in
        // this class, like onServerStarting() below.
        // NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config
        // file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        // NeoForge.EVENT_BUS.register(Config.class);
        Ringtones.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener(Main::registerPayloads);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CELLPHONE_ITEM);
        }
    }

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                CallData.TYPE,
                CallData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleDataOnMain,
                        ServerPayloadHandler::handleDataOnMain));
    }

    public static GroupsAddon groupsAddon() {
        if (groupsAddon == null) {
            groupsAddon = (GroupsAddon) ModVoiceServer.INSTANCE.getAddonManager()
                    .getAddon("pv-addon-groups")
                    .orElseThrow().getInstance().orElseThrow();
        }
        return groupsAddon;
    }
}
