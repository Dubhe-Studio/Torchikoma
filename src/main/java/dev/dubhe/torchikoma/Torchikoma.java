package dev.dubhe.torchikoma;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Torchikoma.ID)
public class Torchikoma {
    public static final Logger LOGGER = LoggerFactory.getLogger("Torchikoma");
    public static final String ID = "torchikoma";

    public Torchikoma() {
        LOGGER.info("test");
    }

    public static ResourceLocation of(String path) {
        return new ResourceLocation(ID, path);
    }
}
