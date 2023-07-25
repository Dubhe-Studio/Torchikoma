package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {
    @SubscribeEvent
    public static void onRegister(net.minecraftforge.registries.RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCKS, helper -> MyBlocks.BLOCK_MAP.forEach(helper::register));
        event.register(ForgeRegistries.Keys.ITEMS, helper -> MyItems.ITEM_MAP.forEach(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> helper.register(Torchikoma.of("torchikoma"), MyItems.TAB));
        event.register(ForgeRegistries.Keys.MENU_TYPES, helper -> MyMenuTypes.MENU_TYPE_MAP.forEach(helper::register));
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> MyEntities.ENTITY_MAP.forEach(helper::register));
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> MyBlockEntities.BLOCK_ENTITY_MAP.forEach(helper::register));
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(MyEntities.TORCHIKOMA, Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D).build());
    }
}
