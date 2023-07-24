package dev.dubhe.torchikoma.registry;

import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.block.entity.TorchikomaBlockEntity;
import dev.dubhe.torchikoma.entity.TorchikomaEntity;
import dev.dubhe.torchikoma.menu.ClusteredTorchToolMenu;
import dev.dubhe.torchikoma.menu.TorchToolMenu;
import dev.dubhe.torchikoma.menu.TorchikomaBlockMenu;
import dev.dubhe.torchikoma.menu.TorchikomaEntityMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

@SuppressWarnings("ConstantConditions")
public class MyMenuTypes {
    public static final MenuType<TorchToolMenu> TORCH_TOOL = forgeMenu("torch_tool", (containerId, inv, buffer) -> new TorchToolMenu(containerId, inv, buffer.readItem()));
    public static final MenuType<ClusteredTorchToolMenu> CLUSTERED_TORCH_TOOL = forgeMenu("clustered_torch_tool", (containerId, inv, buffer) -> new ClusteredTorchToolMenu(containerId, inv, buffer.readItem()));
    public static final MenuType<TorchikomaEntityMenu> TORCHIKOMA_ENTITY = forgeMenu("torchikoma_entity", (containerId, inv, buffer) -> new TorchikomaEntityMenu(containerId, inv, (TorchikomaEntity) inv.player.level().getEntity(buffer.readInt())));
    public static final MenuType<TorchikomaBlockMenu> TORCHIKOMA_BLOCK = forgeMenu("torchikoma_block", (containerId, inv, buffer) -> new TorchikomaBlockMenu(containerId, inv, (TorchikomaBlockEntity) inv.player.level().getBlockEntity(buffer.readBlockPos())));

    private static <T extends AbstractContainerMenu> MenuType<T> vanillaMenu(String id, MenuType.MenuSupplier<T> factory) {
        MenuType<T> menuType = new MenuType<>(factory);
        menuType.setRegistryName(Torchikoma.of(id));
        return menuType;
    }

    private static <T extends AbstractContainerMenu> MenuType<T> forgeMenu(String id, IContainerFactory<T> factory) {
        MenuType<T> menuType = IForgeMenuType.create(factory);
        menuType.setRegistryName(Torchikoma.of(id));
        return menuType;
    }
}
