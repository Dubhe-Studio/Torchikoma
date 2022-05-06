package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.entity.render.TorchRender;
import dev.dubhe.torchikoma.entity.render.TorchikomaRender;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderEvent {
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        MenuScreens.register(MyMenuTypes.TORCH_LAUNCHER, TorchLauncherScreen::new);

        EntityRenderers.register(MyEntities.TORCH, TorchRender::new);
        EntityRenderers.register(MyEntities.TORCHIKOMA, TorchikomaRender::new);

        ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_WALL_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_WALL_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.ELECTRONIC_COMPONENT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.MECHANICAL_COMPONENT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.BLOCKLIGHT_DETECTOR, RenderType.cutout());
    }
}
