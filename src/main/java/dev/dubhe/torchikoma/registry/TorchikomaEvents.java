package dev.dubhe.torchikoma.registry;

import com.mojang.blaze3d.platform.ScreenManager;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
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
            event.getRegistry().register(MyBlocks.PRISMARINE_WALL_TORCH);
            event.getRegistry().register(MyBlocks.GLOWSTONE_TORCH);
            event.getRegistry().register(MyBlocks.GLOWSTONE_WALL_TORCH);
            event.getRegistry().register(MyBlocks.TORCHIKOMA);
        }

        @SubscribeEvent
        public static void onRegisterItem(RegistryEvent.Register<Item> event) {
            event.getRegistry().register(MyItems.PRISMARINE_TORCH);
            event.getRegistry().register(MyItems.GLOWSTONE_TORCH);
            event.getRegistry().register(MyItems.TORCH_LAUNCHER);
            event.getRegistry().register(MyItems.TORCH_GATLING);
            event.getRegistry().register(MyItems.TORCH_CANNON);
            event.getRegistry().register(MyItems.CLUSTERED_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_REDSTONE_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_SOUL_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_GLOWSTONE_TORCH);
            event.getRegistry().register(MyItems.CLUSTERED_PRISMARINE_TORCH);
            event.getRegistry().register(MyItems.TORCHIKOMA);
        }
        @SubscribeEvent
        public static void onRegisterMenu(RegistryEvent.Register<MenuType<?>> event) {
            event.getRegistry().register(MyMenuTypes.TORCH_LAUNCHER);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetUpEvent(FMLClientSetupEvent event) {
            MenuScreens.register(MyMenuTypes.TORCH_LAUNCHER, TorchLauncherScreen::new);

            ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_WALL_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_TORCH, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_WALL_TORCH, RenderType.cutout());
        }
    }
}
