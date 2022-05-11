package dev.dubhe.torchikoma.entity.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TorchikomaCustomTexture {
    private final Gson GSON = new GsonBuilder().create();
    private final TypeToken<Map<String, String>> TORCHIKOMA_CUSTOME_TEXTURE_TYPE = new TypeToken<>() {
    };
    private Map<String, String> itemMap;

    public TorchikomaCustomTexture() {
    }

    public void reload(ResourceManager pResourceManager) throws IOException {
        Resource resource = pResourceManager.getResource(new ResourceLocation(Torchikoma.ID, "textures/entity/torchikoma/bind.json"));
        try {
            InputStream inputstream = resource.getInputStream();
            Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
            this.itemMap = GsonHelper.fromJson(GSON, reader, TORCHIKOMA_CUSTOME_TEXTURE_TYPE);
            reader.close();
            inputstream.close();
        } catch (RuntimeException runtimeException) {
            Torchikoma.LOGGER.error("Invalid torchikoma:textures/entity/torchikoma/bind.json");
        } catch (IOException ioException){
            Torchikoma.LOGGER.warn("ioException");
        }
    }

    public Map<String, String> getItemMap() {
        return this.itemMap;
    }

    public boolean has(String pKey) {
        return this.itemMap.getOrDefault(pKey, null) != null;
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public String get(String pKey) {
        if (this.has(pKey)) {
            return this.getItemMap().get(pKey);
        }
        ItemParser itemParser = new ItemParser(new StringReader(pKey), false);
        try {
            itemParser.readItem();
        } catch (CommandSyntaxException exception) {
            Torchikoma.LOGGER.error("Read Item Faild");
        }
        ItemStack itemStack = new ItemStack(itemParser.getItem());
        if (itemStack.getTags().toList().size() != 0) {
            for (Map.Entry<String, String> entry : this.itemMap.entrySet()) {
                if (entry.getKey().charAt(0) != '#') continue;
                TagKey<Item> itemTag;
                try {
                    itemTag = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.read(new StringReader(removeCharAt(entry.getKey(), 0))));
                    try {
                        if (itemStack.is(itemTag)) {
                            return entry.getValue();
                        }
                    } catch (NullPointerException ignored) {
                        Torchikoma.LOGGER.error(entry.getKey() + " is not a approved tag");
                    }
                } catch (CommandSyntaxException ignored) {
                    Torchikoma.LOGGER.warn(entry.getKey() + " is not a approved tag");
                }
            }
        }
        return null;
    }
}
