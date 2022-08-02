package dev.dubhe.torchikoma.item;

import net.minecraft.world.item.Item;

public class ClusteredTorchItem extends Item {

    Item torch;

    public ClusteredTorchItem(Properties pProperties, Item torch) {
        super(pProperties);
        this.torch = torch;
    }

    public Item getTorch() {
        return this.torch;
    }
}
