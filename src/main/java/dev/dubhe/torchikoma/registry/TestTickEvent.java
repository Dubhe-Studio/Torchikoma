package dev.dubhe.torchikoma.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class TestTickEvent {

    @SubscribeEvent
    public static void TickEvent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player) {
            LivingEntity entity = rayItem(player, Mob.class);
            if (entity != null) {
                player.displayClientMessage(new TextComponent(entity.getHealth() + "/" + entity.getMaxHealth()).withStyle(ChatFormatting.GREEN), true);
            }
        }
    }

    @Nullable
    public static <T extends LivingEntity> LivingEntity rayItem(ServerPlayer player, Class<T> clazz) {
        double length = 0.05D;
        Vec3 playerPos = player.getEyePosition();
        double yaw = player.getYRot();
        double pitch = player.getXRot();
        double y = -Math.sin(pitch * Math.PI / 180D) * length;
        double x = -Math.sin(yaw * Math.PI / 180D);
        double z = Math.cos(yaw * Math.PI / 180D);
        double proportion = Math.sqrt((((length * length) - (y * y)) / ((x * x) + (z * z))));
        x *= proportion;
        z *= proportion;
        for (Vec3 pos = playerPos; Math.sqrt(Math.pow(pos.x - playerPos.x, 2) + Math.pow(pos.y - playerPos.y, 2) + Math.pow(pos.z - playerPos.z, 2)) < 5; pos = pos.add(x, y, z)) {
            if (player.level.getBlockState(new BlockPos(pos)).getBlock() != Blocks.AIR) {
                return null;
            }
            AABB box = new AABB(pos.x - 0.005, pos.y - 0.2, pos.z - 0.005, pos.x + 0.005, pos.y + 0.005, pos.z + 0.005);
            List<T> list = player.level.getEntitiesOfClass(clazz, box, entity -> entity != null && entity.isAlive());
            if (!list.isEmpty()) return list.get(0);
        }
        return null;
    }

}
