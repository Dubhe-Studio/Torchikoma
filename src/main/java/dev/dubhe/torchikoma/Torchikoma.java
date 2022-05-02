package dev.dubhe.torchikoma;

import dev.dubhe.torchikoma.registry.*;
import net.minecraftforge.fml.common.Mod;

@Mod(Torchikoma.ID)
public class Torchikoma {
    public static final String ID = "torchikoma";

    public Torchikoma() {
        MyBlocks.register();
        MyItems.register();
    }
}
