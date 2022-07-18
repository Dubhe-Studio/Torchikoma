package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TorchikomaBlockMenu extends TorchikomaMenu<TorchikomaBlockEntity> {

    public TorchikomaBlockMenu(int pContainerId, Inventory inventory, @Nonnull TorchikomaBlockEntity blockEntity) {
        super(MyMenuTypes.TORCHIKOMA_BLOCK, pContainerId, inventory, blockEntity);
    }

    @Override
    protected Container getInventory() {
        return this.supplier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TorchikomaEntity getEntity() {
        return this.supplier.genEntity();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public byte getStatus() {
        return 2;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getHealth() {
        return this.supplier.getHealth();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getEnergy() {
        return this.supplier.getEnergy();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canStandby() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setStatus(byte status) {
    }
}