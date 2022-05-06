package dev.dubhe.torchikoma.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TorchikomaEntity extends TamableAnimal implements IAnimatable, IAnimationTickable {
    AnimationFactory factory = new AnimationFactory(this);
    public TorchikomaEntity(EntityType<? extends TamableAnimal> type, Level inLevel) {
        super(type, inLevel);
        this.noCulling = true;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public void tick(){
        super.tick();
        Player player = (Player) this.getOwner();
        float f = getOwnerDistance();
        if (f > 6.0F) {
            double d0 = (player.getX() - this.getX()) / (double)f;
            double d1 = (player.getY() - this.getY()) / (double)f;
            double d2 = (player.getZ() - this.getZ()) / (double)f;
            this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
        } else {
            this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
            Vec3 vec3 = (new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ())).normalize().scale(Math.max(f - 2.0F, 0.0F));
            this.getNavigation().moveTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z, this.followLeashSpeed());
        }
    }

    @Override
    public int tickTimer() {
        return 0;
    }

    protected float getOwnerDistance() {
        Player player = (Player) this.getOwner();
        if (player != null){
            return this.distanceTo(player);
        }else {
            return 0;
        }
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("creeper_walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("creeper_idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<TorchikomaEntity>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
