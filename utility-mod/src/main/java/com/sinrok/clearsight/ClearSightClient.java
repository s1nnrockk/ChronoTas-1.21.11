package com.sinrok.clearsight;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class ClearSightClient implements ClientModInitializer {
    public static final ClearSightConfig CONFIG = new ClearSightConfig();

    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        CONFIG.load();
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.clearsight.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClearSightSettingsScreen());
                }
            }
        });
    }

    public static Identifier id(String path) {
        return Identifier.of("clearsight", path);
    }
}
