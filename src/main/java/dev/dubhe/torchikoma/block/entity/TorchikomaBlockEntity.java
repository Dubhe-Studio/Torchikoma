package dev.dubhe.torchikoma.block.entity;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.item.EnergyCoreItem;
import dev.dubhe.torchikoma.registry.MyBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.UUID;


public class TorchikomaBlockEntity extends BlockEntity implements Container, Nameable {
    private LazyOptional<?> itemHandler = LazyOptional.of(this::createUnSidedHandler);
    private final NonNullList<ItemStack> items = NonNullList.withSize(15, ItemStack.EMPTY);
    private Component name;
    private UUID owner;
    private float health;
    private int energy;

    public TorchikomaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MyBlockEntities.TORCHIKOMA, pPos, pBlockState);
        this.owner = null;
        this.health = 80.0F;
        this.energy = 0;
    }

    public void initFromEntity(TorchikomaEntity entity) {
        this.owner = entity.getOwnerUUID();
        this.health = entity.getHealth();
        this.energy = entity.getEnergy();
        for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
            this.items.set(i, entity.getInventory().getItem(i));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void tick() {
        ItemStack itemStack = this.items.get(13);
        if (!this.level.isClientSide && itemStack.getItem() instanceof EnergyCoreItem item) {
            this.addEnergy(item.getRecovery());
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        this.items.clear();
        super.load(pTag);
        if (pTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }
        if (pTag.contains("Owner")) this.owner = pTag.getUUID("Owner");
        if (pTag.contains("Energy")) this.energy = pTag.getInt("Energy");
        if (pTag.contains("Health")) this.health = pTag.getInt("Health");
        ContainerHelper.loadAllItems(pTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        if (this.owner != null) pTag.putUUID("Owner", this.owner);
        pTag.putInt("Energy", this.energy);
        pTag.putFloat("Health", this.health);
        ContainerHelper.saveAllItems(pTag, this.items);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putFloat("Health", this.health);
        nbt.putInt("Energy", this.energy);
        return nbt;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag nbt = pkt.getTag();
        assert nbt != null;
        this.health = nbt.getFloat("Health");
        this.energy = nbt.getInt("Energy");
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
    public TorchikomaEntity setStatus(boolean follow) {
        TorchikomaEntity entity = this.genEntity();
        entity.setStatus((byte) (follow ? 0 : 1));
        this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
        this.level.addFreshEntity(entity);
        return entity;
    }

    public void setCustomName(Component pName) {
        this.name = pName;
    }

    public Component getName() {
        return this.name != null ? this.name : Component.translatable("torchikoma.title");
    }

    public Component getDisplayName() {
        return this.getName();
    }

    public Component getCustomName() {
        return this.name;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, pIndex, pCount);
        if (!itemstack.isEmpty()) this.setChanged();
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.items.set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER)
            return itemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(this::createUnSidedHandler);
    }
}
