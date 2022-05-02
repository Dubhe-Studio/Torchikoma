package dev.dubhe.torchikoma.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class TorchikomaEvents {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    static class GlobalModEvents {
        @SubscribeEvent
        public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(MyBlocks.PRISMARINE_TORCH);
            event.getRegistry().register(MyBlocks.GLOWSTONE_TORCH);
        }

        @SubscribeEvent
        public static void onRegisterItem(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(MyItems.PRISMARINE_TORCH);
            event.getRegistry().register(MyItems.GLOWSTONE_TORCH);
            event.getRegistry().register(MyItems.TORCH_LAUNCHER);
        }
    }
}
