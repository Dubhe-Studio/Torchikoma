package dev.dubhe.torchikoma;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.network.C2STorchikomaEntityShoot;
import dev.dubhe.torchikoma.network.Network;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Torchikoma.ID)
public class Torchikoma {
    public static final Logger LOGGER = LoggerFactory.getLogger("Torchikoma");
    public static final String ID = "torchikoma";

    public Torchikoma() {
        MinecraftForge.EVENT_BUS.addListener(Torchikoma::onPlayerRightClick);
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof TorchikomaEntity) {
            Network.sendToServer(new C2STorchikomaEntityShoot(player.getUUID()));
        }
    }

    public static ResourceLocation of(String path) {
        return new ResourceLocation(ID, path);
    }
}
