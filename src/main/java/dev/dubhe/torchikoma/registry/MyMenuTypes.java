package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

public class MyMenuTypes {
    public static final MenuType<TorchLauncherMenu> TORCH_LAUNCHER = forgeMenu("torch_launcher", (containerId, inv, buffer) -> TorchLauncherMenu.createLauncher(containerId, inv, buffer.readItem()));

    public static final MenuType<TorchLauncherMenu> TORCH_GATLING = forgeMenu("torch_gatling", (containerId, inv, buffer) -> TorchLauncherMenu.createGatling(containerId, inv, buffer.readItem()));

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
