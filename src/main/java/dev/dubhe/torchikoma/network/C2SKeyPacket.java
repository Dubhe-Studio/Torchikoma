package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.screen.ScreenProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class C2SKeyPacket {
    private final Command command;
    private final int slot;

    public C2SKeyPacket(FriendlyByteBuf buffer) {
        this.command = Command.values()[buffer.readInt()];
        this.slot = buffer.readInt();
    }

    public C2SKeyPacket(Command command, int slot) {
        this.command = command;
        this.slot = slot;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.command.ordinal());
        buf.writeInt(this.slot);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        this.command.accept(ctx.get(), this.slot);
        ctx.get().setPacketHandled(true);
    }

    public enum Command {
        OPEN_GUN_GUI((ctx, slot) -> {
            ServerPlayer player = ctx.getSender();
            assert player != null;
            if (player.containerMenu instanceof InventoryMenu) {
                if (slot == 40) slot = 45;
                else if (slot >= 0 && slot < 9) slot = 36 + slot;
                else if (slot >= 36 && slot <= 39) slot = 44 - slot;
                else return;
            }

            ItemStack stack = slot == -1 ? player.getItemInHand(InteractionHand.MAIN_HAND) : player.containerMenu.slots.get(slot).getItem();
            System.out.println(player.containerMenu);
            System.out.println(player.containerMenu.slots.stream().map(slot1 -> "Slot " + slot1.getSlotIndex() + ": " + slot1.getItem()).collect(Collectors.joining("/")));
            System.out.println(slot);
            System.out.println(stack);
            if (!stack.isEmpty() && stack.getItem() instanceof ScreenProvider item) {
                System.out.println("open");
                item.openGUI(player, stack);
            }
        });

        private final ConsumerTwo consumer;
        Command(ConsumerTwo consumer) {
            this.consumer = consumer;
        }

        public void accept(NetworkEvent.Context ctx, int slot) {
            this.consumer.accept(ctx, slot);
        }
    }

    interface ConsumerTwo {
        void accept(NetworkEvent.Context ctx, int slot);
    }
}
