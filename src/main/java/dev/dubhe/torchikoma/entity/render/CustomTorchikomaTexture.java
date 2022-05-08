package dev.dubhe.torchikoma.entity.render;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomTorchikomaTexture {
    private final Gson GSON = new GsonBuilder().create();
    private final TypeToken<Map<ResourceLocation, ResourceLocation>> CUSTOME_TORCHIKOMA_TEXTURE_TYPE = new TypeToken<>() {
    };
    private Map<ResourceLocation, ResourceLocation> itemMap;

    public CustomTorchikomaTexture() {
    }

    public void reload(ResourceManager pResourceManager) throws IOException {
        Resource resource = pResourceManager.getResource(new ResourceLocation(Torchikoma.ID, "textures/entity/torchikoma/bind.json"));
        try {
            InputStream inputstream = resource.getInputStream();
            try {
                Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
                try {
                    this.itemMap = GsonHelper.fromJson(GSON, reader, CUSTOME_TORCHIKOMA_TEXTURE_TYPE);
                } catch (Throwable throwable2) {
                    try {
                        reader.close();
                    } catch (Throwable throwable1) {
                        throwable2.addSuppressed(throwable1);
                    }
                    throw throwable2;
                }
            } catch (Throwable throwable3) {
                try {
                    inputstream.close();
                } catch (Throwable throwable) {
                    throwable3.addSuppressed(throwable);
                }
                throw throwable3;
            }
        } catch (RuntimeException runtimeexception) {
            Torchikoma.LOGGER.warn("Invalid torchikoma:textures/entity/torchikoma/bind.json");
        }
    }

    public Map<ResourceLocation, ResourceLocation> getItemMap(){
        return this.itemMap;
    }

    public ResourceLocation[] getItemList() {
        return this.itemMap.keySet().toArray(new ResourceLocation[0]);
    }
}
