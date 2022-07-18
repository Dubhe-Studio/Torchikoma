package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2STorchikomaBlock2Entity implements IPacket{
    private final BlockPos pos;
    private final boolean isFollowMode;

    public C2STorchikomaBlock2Entity(BlockPos pos, boolean isFollowMode) {
        this.pos = pos;
        this.isFollowMode = isFollowMode;
    }

    public C2STorchikomaBlock2Entity(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.isFollowMode = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeBoolean(this.isFollowMode);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        BlockEntity blockEntity = context.getSender().level.getBlockEntity(this.pos);
        if (blockEntity instanceof TorchikomaBlockEntity tbe) tbe.setStatus(this.isFollowMode);
        context.setPacketHandled(true);
    }

}
