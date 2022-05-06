package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchToolMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

public class MyMenuTypes {
    public static final MenuType<TorchToolMenu> TORCH_TOOL_MENU = forgeMenu("torch_launcher", (containerId, inv, buffer) -> new TorchToolMenu(containerId, inv, buffer.readItem()));

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
