package dev.dubhe.torchikoma.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SKeyPacket {
    private final Command command;
    private final ItemStack stack;
    private final boolean openGui;

    public C2SKeyPacket(FriendlyByteBuf buffer) {
        this.command = Command.values()[buffer.readInt()];
        this.stack = buffer.readItem();
        this.openGui = buffer.readBoolean();
    }

    public C2SKeyPacket(Command command, ItemStack stack, boolean openGui) {
        this.command = command;
        this.stack = stack;
        this.openGui = openGui;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.command.ordinal());
        buf.writeItem(this.stack);
        buf.writeBoolean(this.openGui);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        player.containerMenu.slots.get(0);

        ctx.get().setPacketHandled(true);
    }

    enum Command {
        OPEN_GUN_GUI();

        Command() {
        }
    }
}
