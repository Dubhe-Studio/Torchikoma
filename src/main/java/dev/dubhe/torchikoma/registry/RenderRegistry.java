package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.entity.render.TorchRender;
import dev.dubhe.torchikoma.entity.render.TorchikomaRender;
import dev.dubhe.torchikoma.screen.TorchCannonScreen;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import dev.dubhe.torchikoma.screen.TorchikomaScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RenderRegistry {
    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        MenuScreens.register(MyMenuTypes.TORCH_TOOL, TorchLauncherScreen::new);
        MenuScreens.register(MyMenuTypes.CLUSTERED_TORCH_TOOL, TorchCannonScreen::new);
        MenuScreens.register(MyMenuTypes.TORCHIKOMA_ENTITY, TorchikomaScreen::new);
        MenuScreens.register(MyMenuTypes.TORCHIKOMA_BLOCK, TorchikomaScreen::new);

        EntityRenderers.register(MyEntities.TORCH, TorchRender::new);
        EntityRenderers.register(MyEntities.CLUSTERED_TORCH, TorchRender::new);
        EntityRenderers.register(MyEntities.TORCHIKOMA, TorchikomaRender::new);

        ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.PRISMARINE_WALL_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.GLOWSTONE_WALL_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.ELECTRONIC_COMPONENT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.MECHANICAL_COMPONENT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.BLOCKLIGHT_DETECTOR, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(MyBlocks.TORCHIKOMA, RenderType.cutout());
    }
}
