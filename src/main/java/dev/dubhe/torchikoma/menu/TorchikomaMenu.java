package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public class TorchikomaMenu extends AbstractContainerMenu {

    public static int t1;

    private final TorchikomaEntity entity;

    public TorchikomaMenu(int pContainerId, Inventory inventory, @Nonnull TorchikomaEntity entity) {
        super(MyMenuTypes.TORCHIKOMA, pContainerId);
        this.entity = entity;
        this.entity.getInventory().startOpen(inventory.player);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.addSlot(new Slot(this.entity.getInventory(), j + i * 2, 99 + j * 18, 16 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            this.addSlot(new Slot(this.entity.getInventory(), 12 + i, 9, 16 + i * 18));
        }


        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 9 + j * 18, 132 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 9 + i * 18, 190));
        }

    }

    public TorchikomaEntity getEntity() {
        return this.entity;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.entity.getInventory().stopOpen(pPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isEmpty(int pIndex) {
        return this.entity.getInventory().getItem(pIndex).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public float getHealth() {
        return this.entity.getHealth();
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnergy() {
        return this.entity.getEnergy();
    }

    @OnlyIn(Dist.CLIENT)
    public byte getStatus() {
        return this.entity.getStatus();
    }
}
