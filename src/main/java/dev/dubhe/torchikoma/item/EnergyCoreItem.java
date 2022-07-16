package dev.dubhe.torchikoma.item;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class EnergyCoreItem extends ItemNameBlockItem {
    private final int recovery;
    public EnergyCoreItem(Block pBlock, int recovery, Properties pProperties) {
        super(pBlock, pProperties);
        this.recovery = recovery;
    }

    public int getRecovery() {
        return this.recovery;
    }
}
