package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.entity.TorchEntity;
import dev.dubhe.torchikoma.menu.ProviderMenu;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
            } else {
                TorchEntity entity = new TorchEntity(pLevel, pPlayer, new ItemStack(Items.TORCH));
                entity.setDeltaMovement(pPlayer.getLookAngle().multiply(2,2,2));
                pLevel.addFreshEntity(entity);
                handleGunpowder(item);
                //TODO 发射
            }
        }
        return InteractionResultHolder.pass(item);
    }
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    public static void handleGunpowder(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        int gunpowder = nbt.getInt("Gunpowder");
        if (gunpowder > 84) return;
        ListTag torches = nbt.getList("Torches", 10);
        CompoundTag itemTag = null, temp;
        int index = 0;
        for (int i = 0; i < torches.size(); i++) {
            temp = nbt.getList("Torches", 10).getCompound(i);
            if (temp.getInt("Slot") == 4) {
                itemTag = temp;
                index = i;
            }
        }
        if (itemTag != null) {
            ItemStack itemStack = ItemStack.of(itemTag);
            if (!itemStack.isEmpty() && itemStack.getItem() == Items.GUNPOWDER) {
                itemStack.shrink(1);
                torches.set(index, itemStack.save(new CompoundTag()));
                nbt.putInt("Gunpowder", gunpowder + 16);
            }
        }
    }
}
