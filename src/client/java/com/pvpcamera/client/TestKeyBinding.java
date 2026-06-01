package com.pvpcamera.client;
import net.minecraft.client.option.KeyBinding;
public class TestKeyBinding {
    public static void test() {
        for (java.lang.reflect.Method m : KeyBinding.class.getDeclaredMethods()) {
            System.out.println("KB METHOD: " + m);
        }
        for (Class<?> c : KeyBinding.class.getDeclaredClasses()) {
            System.out.println("KB CLASS: " + c);
        }
    }
}
