package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.menu.TorchToolMenu;
import dev.dubhe.torchikoma.menu.TorchikomaMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

@SuppressWarnings("ConstantConditions")
public class MyMenuTypes {
    public static final MenuType<TorchToolMenu> TORCH_TOOL = forgeMenu("torch_tool", (containerId, inv, buffer) -> new TorchToolMenu(containerId, inv, buffer.readItem()));
    public static final MenuType<TorchikomaMenu> TORCHIKOMA = forgeMenu("torchikoma", (containerId, inv, buffer) -> new TorchikomaMenu(containerId, inv, (TorchikomaEntity) inv.player.level.getEntity(buffer.readInt())));

    private static <T extends AbstractContainerMenu> MenuType<T> vanillaMenu(String id, MenuType.MenuSupplier<T> factory) {
        MenuType<T> menuType = new MenuType<>(factory);
        menuType.setRegistryName(Torchikoma.getId(id));
        return menuType;
    }

    private static <T extends AbstractContainerMenu> MenuType<T> forgeMenu(String id, IContainerFactory<T> factory) {
        MenuType<T> menuType = IForgeMenuType.create(factory);
        menuType.setRegistryName(Torchikoma.getId(id));
        return menuType;
    }
}
