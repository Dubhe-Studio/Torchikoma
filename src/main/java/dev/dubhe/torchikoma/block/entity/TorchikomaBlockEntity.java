package dev.dubhe.torchikoma.block.entity;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.menu.TorchikomaBlockMenu;
import dev.dubhe.torchikoma.registry.MyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;


public class TorchikomaBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(15, ItemStack.EMPTY);
    private UUID owner;
    private float health;
    private int energy;

    public TorchikomaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MyBlockEntities.TORCHIKOMA, pPos, pBlockState);
        this.owner = null;
        this.health = 80.0F;
        this.energy = 0;
    }
    public TorchikomaBlockEntity(BlockPos pPos, BlockState pBlockState, TorchikomaEntity entity) {
        super(MyBlockEntities.TORCHIKOMA, pPos, pBlockState);
        this.owner = entity.getOwnerUUID();
        this.health = entity.getHealth();
        this.energy = entity.getEnergy();
        for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
            this.items.set(i, entity.getInventory().getItem(i));
        }
    }

    public void tick() {
        ItemStack itemStack = this.items.get(13);
        if (itemStack.getItem() instanceof EnergyCoreItem item) {
            this.addEnergy(item.getRecovery());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items.clear();
        if (pTag.contains("Owner")) this.owner = pTag.getUUID("Owner");
        if (pTag.contains("Energy")) this.energy = pTag.getInt("Energy");
        if (pTag.contains("Health")) this.health = pTag.getInt("Health");
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.owner != null) pTag.putUUID("Owner", this.owner);
        pTag.putInt("Energy", this.energy);
        pTag.putFloat("Health", this.health);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    public TorchikomaEntity genEntity() {
        return TorchikomaEntity.of(this.level, this.getBlockPos(), this.items, this.owner, this.health, this.energy);
    }

    public void setOwner(UUID pOwner) {
        this.owner = pOwner;
    }

    public float getHealth() {
        return this.health;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int energy) {
        int now = this.getEnergy();
        if (now < 20000) {
            this.setEnergy(Math.min(now + energy, 20000));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void setStatus(boolean follow) {
        TorchikomaEntity entity = this.genEntity();
        entity.setStatus((byte) (follow ? 0 : 1));
        this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
        this.level.addFreshEntity(entity);
    }

    @Nonnull
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@Nonnull NonNullList<ItemStack> pItemStacks) {
        this.items = pItemStacks;
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("torchikoma.title");
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, @Nonnull Inventory pInventory) {
        return new TorchikomaBlockMenu(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }
}
