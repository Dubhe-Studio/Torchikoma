package dev.dubhe.torchikoma.entity.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Arrays;
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
            try {
                Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
                try {
                    this.itemMap = GsonHelper.fromJson(GSON, reader, TORCHIKOMA_CUSTOME_TEXTURE_TYPE);
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

    public Map<String, String> getItemMap() {
        return this.itemMap;
    }

    public boolean has(String pKey) {
        return this.itemMap.getOrDefault(pKey, null) != null;
    }

    public String[] getItemList() {
        return this.itemMap.keySet().toArray(new String[0]);
    }

    private static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    public String get(String pKey) {
        if(this.has(pKey)){return this.getItemMap().get(pKey);}
        ItemParser itemParser1 = new ItemParser(new StringReader(pKey), false);
        try {
            itemParser1.readItem();
        }catch (CommandSyntaxException exception){
            Torchikoma.LOGGER.error("Read Item Faild");
        }
        ItemStack i = new ItemStack(itemParser1.getItem());
        if (i.getTags().toList().size() != 0) {
            for (Map.Entry<String, String> entry : this.itemMap.entrySet()) {
                if (entry.getKey().charAt(0) != '#') continue;
                TagKey<Item> item;
                try {
                    item = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.read(new StringReader(removeCharAt(entry.getKey(),0))));
                    try{
                        if (i.is(item)) {
                            return entry.getValue();
                        }
                    }catch (NullPointerException ignored){
                        Torchikoma.LOGGER.error("NullPointerException");
                    }
                }catch (CommandSyntaxException ignored){
                    Torchikoma.LOGGER.warn(entry.getKey()+"is not a approved tag");
                }
            }
        }
        return null;
    }
}
