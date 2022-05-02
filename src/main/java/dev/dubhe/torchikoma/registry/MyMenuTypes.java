package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MyMenuTypes {
    public static final MenuType<TorchLauncherMenu> TORCH_LAUNCHER = register("torch_launcher", TorchLauncherMenu::new);

    private static <T extends AbstractContainerMenu> MenuType<T> register(String id, MenuType.MenuSupplier<T> factory) {
        MenuType<T> menuType = new MenuType<>(factory);
        menuType.setRegistryName(Torchikoma.getId(id));
        return menuType;
    }
}
