package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.block.ColdFireTorchBlock;
import dev.dubhe.torchikoma.entity.TorchEntity;
import dev.dubhe.torchikoma.screen.ScreenProvider;
import dev.dubhe.torchikoma.menu.TorchToolMenu;
import dev.dubhe.torchikoma.screen.TorchLauncherScreen;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

public class TorchLauncherItem extends Item implements ScreenProvider<ItemStack> {

    private final int cooldown;

    public TorchLauncherItem(Properties pProperties) {
        this(pProperties, 20);
    }

    protected TorchLauncherItem(Properties pProperties, int cooldown) {
        super(pProperties);
        this.cooldown = cooldown;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer,@Nonnull InteractionHand pUsedHand) {
        ItemStack item = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            if (shootTorch(pLevel, pPlayer, item)) {
                pPlayer.getCooldowns().addCooldown(this, this.cooldown);
            }
        }
        return InteractionResultHolder.pass(item);
    }

    @Override
    public void openGUI(Player pPlayer, ItemStack item) {
        NetworkHooks.openScreen((ServerPlayer) pPlayer, this.getMenu(
                this.getDescription(),
                (id, inv, player) -> new TorchToolMenu(id, inv, item)
        ), buffer -> buffer.writeItem(item));
    }

    public boolean shootTorch(Level pLevel, Player pPlayer, ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        int shoots = nbt.getInt("Shoots");
        if (shoots == 0) {
            return false;
        }
        NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
        ListTag torches = nbt.getList("Torches", 10);
        ItemStack item;
        CompoundTag tag;
        for (int i = 0; i < torches.size(); i++) {
            tag = torches.getCompound(i);
            item = ItemStack.of(tag);
            if (!item.isEmpty() && isTorch(item)) {
                items.set(tag.getByte("Slot"), item);
            }
        }
        item = ItemStack.EMPTY;
        for (ItemStack itemStack : items) {
            if (!itemStack.isEmpty()) {
                item = itemStack;
                break;
            }
        }
        if (item.isEmpty()) {
            return false;
        }
        ItemStack stack = item.copy();
        stack.setCount(1);
        shoot(pLevel, pPlayer, stack);
        item.shrink(1);
        nbt.putInt("Shoots", --shoots);
        saveItems(nbt, items);
        if (shoots <= 84 && nbt.contains("Gunpowder")) {
            item = ItemStack.of(nbt.getCompound("Gunpowder"));
            if (!item.isEmpty() && item.getItem() == Items.GUNPOWDER) {
                item.shrink(1);
                nbt.put("Gunpowder", item.save(new CompoundTag()));
                nbt.putInt("Shoots", shoots + 16);
            }
        }
        return true;
    }

    protected void shoot(Level pLevel, Player pPlayer, ItemStack pStack) {
        TorchEntity entity = new TorchEntity(pPlayer, pStack);
        entity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.0F, 1.0F);
        pLevel.addFreshEntity(entity);
    }

    public boolean isTorch(ItemStack pStack) {
        return pStack.getItem() instanceof BlockItem item &&
                (item.getBlock() instanceof TorchBlock || item.getBlock() instanceof ColdFireTorchBlock);
    }

    protected static void saveItems(CompoundTag pNbt, List<ItemStack> pStacks) {
        ListTag torches = new ListTag();
        CompoundTag nbt;
        ItemStack item;
        for (int i = 0; i < pStacks.size(); i++) {
            item = pStacks.get(i);
            if (!item.isEmpty()) {
                nbt = item.save(new CompoundTag());
                nbt.putByte("Slot", (byte) i);
                torches.add(nbt);
            }
        }
        pNbt.put("Torches", torches);
    }

    @OnlyIn(Dist.CLIENT)
    public TorchLauncherScreen.RenderSize getRenderSize() {
        return TorchLauncherScreen.RenderSize.DEFAULT;
    }
}
