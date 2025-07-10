package de.gollumbree.pvcellphone;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Ringtones {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
            .create(BuiltInRegistries.SOUND_EVENT, Main.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE0 = SOUND_EVENTS
            .register("ringtone0",
                    // 1 is the default range of sounds. Be aware that due to OpenAL limitations,
                    // values above 1 have no effect and will be capped to 1.
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone0"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE1 = SOUND_EVENTS
            .register("ringtone1",
                    // 1 is the default range of sounds. Be aware that due to OpenAL limitations,
                    // values above 1 have no effect and will be capped to 1.
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone1"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE2 = SOUND_EVENTS
            .register("ringtone2",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone2"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE3 = SOUND_EVENTS
            .register("ringtone3",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone3"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE4 = SOUND_EVENTS
            .register("ringtone4",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone4"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE5 = SOUND_EVENTS
            .register("ringtone5",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone5"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE6 = SOUND_EVENTS
            .register("ringtone6",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone6"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE7 = SOUND_EVENTS
            .register("ringtone7",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone7"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE8 = SOUND_EVENTS
            .register("ringtone8",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone8"),
                                    1));
    public static final DeferredHolder<SoundEvent, SoundEvent> RINGTONE9 = SOUND_EVENTS
            .register("ringtone9",
                    () -> SoundEvent
                            .createFixedRangeEvent(
                                    ResourceLocation.fromNamespaceAndPath(
                                            Main.MODID, "ringtone9"),
                                    1));
}
