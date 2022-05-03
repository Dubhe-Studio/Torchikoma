package dev.dubhe.torchikoma.item;

import dev.dubhe.torchikoma.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Vanishable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TorchGatling extends ProjectileWeaponItem implements Vanishable {
    public static final int DEFAULT_RANGE = 20;
    public TorchGatling(Properties itemProperties) {
        super(itemProperties);
    }

    public static final Predicate<ItemStack> TOUCH_ONLY = item -> item.is(ItemTags.TOUCH);

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return TOUCH_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return DEFAULT_RANGE;
    }
}
