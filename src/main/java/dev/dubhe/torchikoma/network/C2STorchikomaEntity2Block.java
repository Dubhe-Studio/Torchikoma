package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2STorchikomaEntity2Block implements IPacket{
    private final int id;
    private final byte status;

    public C2STorchikomaEntity2Block(int id, byte status) {
        this.id = id;
        this.status = status;
    }

    public C2STorchikomaEntity2Block(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
        this.status = buffer.readByte();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.id);
        buf.writeByte(this.status);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        Entity entity = context.getSender().level().getEntity(this.id);
        if (entity instanceof TorchikomaEntity te) te.setStatus(this.status);
        context.setPacketHandled(true);
    }
}
