package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class Network {
    private static final String VERSION = "1.0";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Torchikoma.of("network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static int id = 0;

    private static int nextID() {
        return id++;
    }

    public static void register() {
        registerC2S(C2SKeyPacket.class, C2SKeyPacket::new);
        registerC2S(C2STorchikomaBlock2Entity.class, C2STorchikomaBlock2Entity::new);
        registerC2S(C2STorchikomaEntity2Block.class, C2STorchikomaEntity2Block::new);
    }

    private static <T extends IPacket> void registerC2S(final Class<T> type, Function<FriendlyByteBuf, T> decoder) {
        INSTANCE.messageBuilder(type, nextID(), NetworkDirection.PLAY_TO_SERVER).encoder(T::write).decoder(decoder).consumerNetworkThread(T::handle).add();  // TODO: 检查这里的调用是否符合原意
    }

    public static <T> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }

}
