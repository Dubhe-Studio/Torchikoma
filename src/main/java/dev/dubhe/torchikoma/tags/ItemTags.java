package dev.dubhe.torchikoma.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ItemTags {
    public static final TagKey<Item> TOUCH = bind("touch");
    public static final TagKey<Item> CLUSTERED_TOUCH = bind("clustered_touch");

    private static TagKey<Item> bind(String pName) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(pName));
    }

    public static TagKey<Item> create(final ResourceLocation name) {
        return TagKey.create(Registry.ITEM_REGISTRY, name);
    }
}
