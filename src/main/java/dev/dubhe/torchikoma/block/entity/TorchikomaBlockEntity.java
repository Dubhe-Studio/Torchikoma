package dev.dubhe.torchikoma.block.entity;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.registry.MyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;


public class TorchikomaBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(15, ItemStack.EMPTY);
    private UUID owner;
    private String painting;
    private float health;
    private int energy;
    private byte status;

    public TorchikomaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MyBlockEntities.TORCHIKOMA, pPos, pBlockState);
        this.owner = null;
        this.painting = "minecraft:blaze_powder";
        this.health = 80.0F;
        this.energy = 0;
        this.status = 1;
    }
    public TorchikomaBlockEntity(BlockPos pPos, BlockState pBlockState, TorchikomaEntity entity) {
        super(MyBlockEntities.TORCHIKOMA, pPos, pBlockState);
        this.owner = entity.getOwnerUUID();
        this.painting = entity.getPainting();
        this.health = entity.getHealth();
        this.energy = entity.getEnergy();
        this.status = entity.getStatus();
        for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
            this.items.set(i, entity.getInventory().getItem(i));
        }
    }

    public void tick() {

    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.items.clear();
        if (pTag.contains("Owner")) this.owner = pTag.getUUID("Owner");
        if (pTag.contains("PaintingItem")) this.painting = pTag.getString("PaintingItem");
        if (pTag.contains("Energy")) this.energy = pTag.getInt("Energy");
        if (pTag.contains("Health")) this.health = pTag.getInt("Health");
        if (pTag.contains("Status")) this.status = pTag.getByte("Status");
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.owner != null) pTag.putUUID("owner", this.owner);
        pTag.putString("PaintingItem", this.painting);
        pTag.putInt("Energy", this.energy);
        pTag.putFloat("Health", this.health);
        pTag.putByte("Status", this.status);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    public void setOwner(UUID pOwner) {
        this.owner = pOwner;
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
        return new TextComponent("Torchikoma");
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, @Nonnull Inventory pInventory) {
        return ChestMenu.threeRows(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }
}
