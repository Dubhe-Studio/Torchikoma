package dev.dubhe.torchikoma.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvent {
    @SubscribeEvent
    public static void onRegisterBlock(net.minecraftforge.event.RegistryEvent.Register<Block> event) {
        event.getRegistry().register(MyBlocks.PRISMARINE_TORCH);
        event.getRegistry().register(MyBlocks.PRISMARINE_WALL_TORCH);
        event.getRegistry().register(MyBlocks.GLOWSTONE_TORCH);
        event.getRegistry().register(MyBlocks.GLOWSTONE_WALL_TORCH);
        event.getRegistry().register(MyBlocks.TORCHIKOMA);
        event.getRegistry().register(MyBlocks.ELECTRONIC_COMPONENT);
        event.getRegistry().register(MyBlocks.MECHANICAL_COMPONENT);
        event.getRegistry().register(MyBlocks.CLAY_EXPLOSIVES);
        event.getRegistry().register(MyBlocks.BLOCKLIGHT_DETECTOR);
    }

    @SubscribeEvent
    public static void onRegisterItem(net.minecraftforge.event.RegistryEvent.Register<Item> event) {
        event.getRegistry().register(MyItems.PRISMARINE_TORCH);
        event.getRegistry().register(MyItems.GLOWSTONE_TORCH);
        event.getRegistry().register(MyItems.TORCH_LAUNCHER);
        event.getRegistry().register(MyItems.TORCH_GATLING);
        event.getRegistry().register(MyItems.TORCH_CANNON);
        event.getRegistry().register(MyItems.CLUSTERED_TORCH);
        event.getRegistry().register(MyItems.CLUSTERED_SOUL_TORCH);
        event.getRegistry().register(MyItems.CLUSTERED_REDSTONE_TORCH);
        event.getRegistry().register(MyItems.CLUSTERED_PRISMARINE_TORCH);
        event.getRegistry().register(MyItems.CLUSTERED_GLOWSTONE_TORCH);
        event.getRegistry().register(MyItems.TORCHIKOMA);
        event.getRegistry().register(MyItems.ELECTRONIC_COMPONENT);
        event.getRegistry().register(MyItems.MECHANICAL_COMPONENT);
        event.getRegistry().register(MyItems.CLAY_EXPLOSIVES);
        event.getRegistry().register(MyItems.BLOCKLIGHT_DETECTOR);
    }

    @SubscribeEvent
    public static void onRegisterMenu(net.minecraftforge.event.RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().register(MyMenuTypes.TORCH_TOOL_MENU);
    }

    @SubscribeEvent
    public static void onRegisterEntity(net.minecraftforge.event.RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(MyEntities.TORCH);
        event.getRegistry().register(MyEntities.TORCHIKOMA);
    }

    @SubscribeEvent
    public static void onRegisterBlockEntity(net.minecraftforge.event.RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(MyBlockEntities.BLOCKLIGHT_DETECTOR);
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(MyEntities.TORCHIKOMA, Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D).build());
    }
}
