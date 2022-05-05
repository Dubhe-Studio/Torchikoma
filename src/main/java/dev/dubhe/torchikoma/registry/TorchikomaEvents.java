package dev.dubhe.torchikoma.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.dubhe.torchikoma.entity.render.TorchRender;
import dev.dubhe.torchikoma.entity.render.TorchikomaRender;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

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
            event.getRegistry().register(MyBlocks.ELECTRONIC_COMPONENT);
            event.getRegistry().register(MyBlocks.MECHANICAL_COMPONENT);
            event.getRegistry().register(MyBlocks.CLAY_EXPLOSIVES);

        }

        @SubscribeEvent
        public static void onRegisterItem(RegistryEvent.Register<Item> event) {
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
        }
        @SubscribeEvent
        public static void onRegisterMenu(RegistryEvent.Register<MenuType<?>> event) {
            event.getRegistry().register(MyMenuTypes.TORCH_LAUNCHER);
        }

        @SubscribeEvent
        public static void onRegisterEntity(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(MyEntities.TORCH);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
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
        }
    }

    @Mod.EventBusSubscriber
    static class GlobalForgeEvents {
        @SubscribeEvent
        public static void onCommandRegister(RegisterCommandsEvent event) {
            LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("torchikoma");
            root.then(tempCommand("t1", val -> TorchLauncherScreen.temp1 = val));
            root.then(tempCommand("t2", val -> TorchLauncherScreen.temp2 = val));
            root.then(tempCommand("t3", val -> TorchLauncherScreen.temp3 = val));
            root.then(tempCommand("t4", val -> TorchLauncherScreen.temp4 = val));
            root.then(tempCommand("m1", val -> TorchLauncherMenu.temp1 = val));
            root.then(tempCommand("m2", val -> TorchLauncherMenu.temp2 = val));
            root.then(tempCommand("m3", val -> TorchLauncherMenu.temp3 = val));
            root.then(tempCommand("m4", val -> TorchLauncherMenu.temp4 = val));
            event.getDispatcher().register(root);
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> tempCommand(String name, Consumer<Integer> consumer) {
        return Commands.literal(name).then(Commands.argument("value", IntegerArgumentType.integer()).executes(ctx -> {
            consumer.accept(IntegerArgumentType.getInteger(ctx, "value"));
            ctx.getSource().sendSuccess(new TextComponent("Ok"), false);
            return Command.SINGLE_SUCCESS;
        }));
    }

}
