package dev.dubhe.torchikoma.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {

    void write(FriendlyByteBuf buf);

    void handle(Supplier<NetworkEvent.Context> ctx);

}
