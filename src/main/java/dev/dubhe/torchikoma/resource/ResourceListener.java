package dev.dubhe.torchikoma.resource;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

public class ResourceListener {
    public ReloadableResourceManager registerReloadListener() {
        Minecraft.getInstance().getResourceManager();
        ReloadableResourceManager reloadable = (ReloadableResourceManager) Minecraft.getInstance()
                .getResourceManager();
        reloadable.registerReloadListener(TorchikomaCache.getInstance()::reload);
        return reloadable;
    }
}