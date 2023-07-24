package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
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
    }

    // FIXME
    @SubscribeEvent
    public static void onRegisterMenu(net.minecraftforge.event.RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().register(MyMenuTypes.TORCH_TOOL);
        event.getRegistry().register(MyMenuTypes.CLUSTERED_TORCH_TOOL);
        event.getRegistry().register(MyMenuTypes.TORCHIKOMA_ENTITY);
        event.getRegistry().register(MyMenuTypes.TORCHIKOMA_BLOCK);
    }

    // FIXME
    @SubscribeEvent
    public static void onRegisterEntity(net.minecraftforge.event.RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(MyEntities.TORCH);
        event.getRegistry().register(MyEntities.CLUSTERED_TORCH);
        event.getRegistry().register(MyEntities.TORCHIKOMA);
    }

    // FIXME
    @SubscribeEvent
    public static void onRegisterBlockEntity(net.minecraftforge.event.RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(MyBlockEntities.BLOCKLIGHT_DETECTOR);
        event.getRegistry().register(MyBlockEntities.TORCHIKOMA);
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(MyEntities.TORCHIKOMA, Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D).build());
    }
}
