package dev.dubhe.torchikoma.entity.ai.goal;

import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class FollowOwnerGoal extends Goal {
    private final TorchikomaEntity torchikoma;
    private final double speedModifier;
    private int timeToRecalcPath;

    public FollowOwnerGoal(TorchikomaEntity torchikoma, double speed) {
        this.torchikoma = torchikoma;
        this.speedModifier = speed;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        return !this.torchikoma.isVehicle();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        if (this.torchikoma.getOwner() == null || !this.torchikoma.getOwner().isAlive()) {
            return false;
        } else {
            double d0 = this.torchikoma.distanceToSqr(this.torchikoma.getOwner());
            return !(d0 < 9.0D) && !(d0 > 256.0D);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.timeToRecalcPath = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (--this.timeToRecalcPath <= 0 && this.torchikoma.getOwner() != null) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.torchikoma.getNavigation().moveTo(this.torchikoma.getOwner(), this.speedModifier);
        }
    }
}
