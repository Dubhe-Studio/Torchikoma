package dev.dubhe.torchikoma.entity.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.dubhe.torchikoma.Torchikoma;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
    private static final TypeToken<Map<String, String>> TORCHIKOMA_CUSTOME_TEXTURE_TYPE = new TypeToken<>() {};
    private static final Gson GSON = new Gson();

    private Map<String, String> itemMap;

    public void reload(ResourceManager pResourceManager) throws IOException {
        Resource resource = pResourceManager.getResource(Torchikoma.of("textures/entity/torchikoma/bind.json")).get();  // TODO: 优化下这里的写法
        try (InputStream inputStream = resource.open(); Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            this.itemMap = GsonHelper.fromJson(GSON, reader, TORCHIKOMA_CUSTOME_TEXTURE_TYPE);
        } catch (JsonParseException e) {
            Torchikoma.LOGGER.error("Invalid torchikoma:textures/entity/torchikoma/bind.json");
        }
    }

    public String get(String pKey) {
        if (this.itemMap.containsKey(pKey)) return this.itemMap.get(pKey);
        ItemParser.ItemResult item = null;  // TODO: 优化下这里的写法
        try {
            item = ItemParser.parseForItem(BuiltInRegistries.ITEM.asLookup(), new StringReader(pKey));
        } catch (CommandSyntaxException e) {
            Torchikoma.LOGGER.error("Read Item Faild");
        }
        ItemStack itemStack = new ItemStack(item.item().get());
        if (itemStack.getTags().toList().size() != 0) {
            for (Map.Entry<String, String> entry : this.itemMap.entrySet()) {
                if (entry.getKey().charAt(0) != '#') continue;
                try {
                    TagKey<Item> itemTag = TagKey.create(Registries.ITEM, ResourceLocation.read(new StringReader(entry.getKey().substring(1))));
                    if (itemStack.is(itemTag)) return entry.getValue();
                } catch (NullPointerException | CommandSyntaxException e) {
                    Torchikoma.LOGGER.warn(entry.getKey() + " is not a approved tag");
                }
            }
        }
        return null;
    }
}
