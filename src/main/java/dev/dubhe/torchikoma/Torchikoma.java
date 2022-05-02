package dev.dubhe.torchikoma;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(Torchikoma.ID)
public class Torchikoma {
    public static final String ID = "torchikoma";

    public Torchikoma() {
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(ID, path);
    }
}
