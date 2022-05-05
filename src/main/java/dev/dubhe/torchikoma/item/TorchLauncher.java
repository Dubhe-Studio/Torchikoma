package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.entity.TorchEntity;
import dev.dubhe.torchikoma.menu.ProviderMenu;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class TorchLauncher extends Item implements ProviderMenu {

    public TorchLauncher(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack item = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            if (pPlayer.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayer) pPlayer, getMenuProvider(
                        this.getDescription(),
                        (id, inv, player) -> new TorchLauncherMenu(id, inv, item)
                ), buffer -> buffer.writeItem(item));
                return InteractionResultHolder.success(item);
            } else { //TODO 发射
                TorchEntity entity = new TorchEntity(pLevel, pPlayer, new ItemStack(Items.TORCH));
                entity.setDeltaMovement(pPlayer.getLookAngle().multiply(2,2,2));
                pLevel.addFreshEntity(entity);
                handleGunpowder(item);
            }
        }
        return InteractionResultHolder.pass(item);
    }
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    public static void handleGunpowder(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.contains("Gunpowder")) return;
        int shoots = nbt.getInt("Shoots");
        if (shoots > 84) return;
        ItemStack item = ItemStack.of(nbt.getCompound("Gunpowder"));
        if (!item.isEmpty() && item.getItem() == Items.GUNPOWDER) {
            item.shrink(1);
            nbt.put("Gunpowder", item.save(new CompoundTag()));
            nbt.putInt("Shoots", shoots + 16);
        }
    }
}
