package dev.dubhe.torchikoma;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Torchikoma.ID)
public class Torchikoma {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "torchikoma";

    public Torchikoma() {
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(ID, path);
    }
}
