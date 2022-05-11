package dev.dubhe.torchikoma.menu;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.item.TorchLauncher;
import dev.dubhe.torchikoma.registry.MyMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;


public class TorchikomaMenu extends AbstractContainerMenu {

    private final TorchikomaEntity entity;

    public TorchikomaMenu(int pContainerId, Inventory inventory, @Nonnull TorchikomaEntity entity) {
        super(MyMenuTypes.TORCHIKOMA, pContainerId);
        this.entity = entity;
        this.entity.getInventory().startOpen(inventory.player);

        //TODO 还没定位
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                this.addSlot(new Slot(this.entity.getInventory(), j + i * 2, 135 + j * 18, 16 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return TorchLauncher.isTorchItem(pStack);
                    }
                });
            }
        }

        this.addSlot(new Slot(this.entity.getInventory(), 4, 52, 25) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.getItem() == Items.GUNPOWDER;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 9 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 9 + i * 18, 142));
        }

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
    public boolean buttonIsOn(int index) {
        return switch (index) {
            case 1 -> this.entity.isFollowing();
            case 2 -> this.entity.isInSitu();
            case 3 -> this.entity.isSleeping();
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }
}
