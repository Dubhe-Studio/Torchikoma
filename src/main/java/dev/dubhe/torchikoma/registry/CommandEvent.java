package dev.dubhe.torchikoma.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber
public class CommandEvent {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("torchikoma");
//        root.then(tempCommand("t1", val -> TorchLauncherScreen.temp1 = val));
//        root.then(tempCommand("t2", val -> TorchLauncherScreen.temp2 = val));
//        root.then(tempCommand("t3", val -> TorchLauncherScreen.temp3 = val));
//        root.then(tempCommand("t4", val -> TorchLauncherScreen.temp4 = val));
//        root.then(tempCommand("m1", val -> TorchLauncherMenu.temp1 = val));
//        root.then(tempCommand("m2", val -> TorchLauncherMenu.temp2 = val));
//        root.then(tempCommand("m3", val -> TorchLauncherMenu.temp3 = val));
//        root.then(tempCommand("m4", val -> TorchLauncherMenu.temp4 = val));
        event.getDispatcher().register(root);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> tempCommand(String name, Consumer<Integer> consumer) {
        return Commands.literal(name).then(Commands.argument("value", IntegerArgumentType.integer()).executes(ctx -> {
            consumer.accept(IntegerArgumentType.getInteger(ctx, "value"));
            ctx.getSource().sendSuccess(new TextComponent("Ok"), false);
            return Command.SINGLE_SUCCESS;
        }));
    }
}
