package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.entity.TorchEntity;
import dev.dubhe.torchikoma.menu.ProviderMenu;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.network.chat.TextComponent;
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
        if (!pLevel.isClientSide) {
            if (pPlayer.isShiftKeyDown()) {
                NetworkHooks.openGui((ServerPlayer) pPlayer, getMenuProvider(
                        this.getDescription(),
                        (id, inv, player) -> new TorchLauncherMenu(id, inv)
                ));
                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
            } else {
                TorchEntity entity = new TorchEntity(pLevel, pPlayer, new ItemStack(Items.TORCH));
                pLevel.addFreshEntity(entity);
                //TODO 发射
            }
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }
    public @NotNull net.minecraft.world.item.UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }
}
