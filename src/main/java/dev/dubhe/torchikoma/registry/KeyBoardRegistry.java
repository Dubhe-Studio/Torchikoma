package dev.dubhe.torchikoma.registry;

import com.mojang.blaze3d.platform.InputConstants;
import dev.dubhe.torchikoma.Torchikoma;
import dev.dubhe.torchikoma.network.C2SKeyPacket;
import dev.dubhe.torchikoma.network.Network;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public class KeyBoardRegistry {
    private static final String CATEGORY = "key.category." + Torchikoma.ID;
    private static final KeyMapping MESSAGE_KEY = createKey("key.torchikoma.open_gui", GLFW.GLFW_KEY_R);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class KeyRegister {
        @SubscribeEvent
        public static void onKeyboardRegister(RegisterKeyMappingsEvent event) {
            event.register(MESSAGE_KEY);
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    static class KeyInput {
        @SubscribeEvent
        public static void onKeyboardInput(InputEvent.Key event) {
            if (MESSAGE_KEY.isDown()) Network.sendToServer(new C2SKeyPacket(C2SKeyPacket.Command.OPEN_GUN_GUI));
        }
    }

    public static KeyMapping createKey(String id, int key) {
        return new KeyMapping(id, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, key, CATEGORY);
    }

}
