package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.screen.ScreenProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class C2SKeyPacket {
    private final Command command;

    public C2SKeyPacket(FriendlyByteBuf buffer) {
        this.command = Command.values()[buffer.readInt()];
    }

    public C2SKeyPacket(Command command) {
        this.command = command;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.command.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        this.command.accept(ctx.get());
        ctx.get().setPacketHandled(true);
    }

    public enum Command {
        OPEN_GUN_GUI(ctx -> {
            ServerPlayer player = ctx.getSender();
            assert player != null;
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!stack.isEmpty() && stack.getItem() instanceof ScreenProvider item) item.openGUI(player, stack);
        });

        private final Consumer<NetworkEvent.Context> consumer;
        Command(Consumer<NetworkEvent.Context> consumer) {
            this.consumer = consumer;
        }

        public void accept(NetworkEvent.Context ctx) {
            this.consumer.accept(ctx);
        }
    }
}
