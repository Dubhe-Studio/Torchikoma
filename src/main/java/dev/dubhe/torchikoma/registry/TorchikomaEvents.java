package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TorchikomaEvents {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    static class GlobalModEvents {
        @SubscribeEvent
        public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(MyBlocks.PRISMARINE_TORCH);
            event.getRegistry().register(MyBlocks.WALL_PRISMARINE_TORCH);
            event.getRegistry().register(MyBlocks.GLOWSTONE_TORCH);
            event.getRegistry().register(MyBlocks.WALL_GLOWSTONE_TORCH);
        }

        @SubscribeEvent
        public static void onRegisterItem(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(MyItems.PRISMARINE_TORCH);
            event.getRegistry().register(MyItems.GLOWSTONE_TORCH);
            event.getRegistry().register(MyItems.TORCH_LAUNCHER);
            event.getRegistry().register(MyItems.CLUSTERED_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_REDSTONE_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_SOUL_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_GLOWSTONE_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_PRISMARINE_TORCH);
        }
        @SubscribeEvent
        public static void onRegisterMenu(RegistryEvent.Register<MenuType<?>> event) {
            event.getRegistry().register(MyMenuTypes.TORCH_LAUNCHER);
        }
    }

    static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetUpEvent(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.WALL_GLOWSTONE_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.WALL_PRISMARINE_TORCH, RenderType.cutout());
        }
    }
}
