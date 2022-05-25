package dev.dubhe.torchikoma.item;

import net.minecraft.world.item.Item;

public class EnergyCore extends Item {
    private final int recovery;
    public EnergyCore(int recovery, Properties pProperties) {
        super(pProperties);
        this.recovery = recovery;
    }

    public int getRecovery() {
        return this.recovery;
    }
}
