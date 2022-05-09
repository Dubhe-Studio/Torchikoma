package dev.dubhe.torchikoma.network;

import dev.dubhe.torchikoma.Torchikoma;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private static final String VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Torchikoma.getId("network"), () -> VERSION, VERSION::equals, VERSION::equals);
    private static int id = 0;

    private static int nextID() {
        return id++;
    }

    public static void register() {
        INSTANCE.messageBuilder(C2SKeyPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SKeyPacket::write)
                .decoder(C2SKeyPacket::new)
                .consumer(C2SKeyPacket::handle)
                .add();
    }

}
