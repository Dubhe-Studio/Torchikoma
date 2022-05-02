package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.menu.TorchLauncherMenu;
import net.minecraft.world.inventory.MenuType;

public class MyMenuTypes {
    public static final MenuType<TorchLauncherMenu> TORCH_LAUNCHER = new MenuType<>(TorchLauncherMenu::new);
}
