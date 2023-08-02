package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class C2STorchikomaEntityShoot implements IPacket {
    private final UUID player;

    public C2STorchikomaEntityShoot(UUID player) {
        this.player = player;
    }

    public C2STorchikomaEntityShoot(FriendlyByteBuf buffer) {
        this(buffer.readUUID());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(()->{
            Level level = context.getSender().level();
            Player player = level.getPlayerByUUID(this.player);
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof TorchikomaEntity torchikoma) {
                torchikoma.shoot(player);
            }
        });
        context.setPacketHandled(true);
    }
}
