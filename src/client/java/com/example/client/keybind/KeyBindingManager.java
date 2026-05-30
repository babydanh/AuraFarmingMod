package com.example.client.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingManager {
    public static KeyBinding randomSnapKey;
    public static KeyBinding spin360Key;

    public static void register() {
        randomSnapKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.example.random_snap",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.example.pvp_camera"
        ));

        spin360Key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.example.spin_360",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "category.example.pvp_camera"
        ));
    }
}
