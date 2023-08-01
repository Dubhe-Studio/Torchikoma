package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.network.C2STorchikomaEntity2Block;
import dev.dubhe.torchikoma.network.Network;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public class TorchikomaEntityMenu extends TorchikomaMenu<TorchikomaEntity> {

    public TorchikomaEntityMenu(int pContainerId, Inventory inventory, @Nonnull TorchikomaEntity entity) {
        super(MyMenuTypes.TORCHIKOMA_ENTITY, pContainerId, inventory, entity);
    }

    @Override
    protected Container getInventory() {
        return this.supplier.getInventory();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public TorchikomaEntity getEntity() {
        return this.supplier;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public byte getStatus() {
        return this.supplier.getStatus();
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
        return this.supplier.canStandby();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setStatus(byte status) {
        Network.sendToServer(new C2STorchikomaEntity2Block(this.supplier.getId(), status));
    }
}
