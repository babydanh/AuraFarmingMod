package com.example.client.handler;

import com.example.client.keybind.KeyBindingManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.Random;

public class CameraActionHandler {
    private static final Random random = new Random();

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (KeyBindingManager.randomSnapKey.wasPressed()) {
                handleRandomSnap(client.player);
            }

            while (KeyBindingManager.spin360Key.wasPressed()) {
                handleSpin360(client.player);
            }
        });
    }

    private static void handleRandomSnap(ClientPlayerEntity player) {
        float yawChange = 60f + random.nextFloat() * 20f;
        if (random.nextBoolean()) {
            yawChange = -yawChange;
        }
        player.setYaw(player.getYaw() + yawChange);

        float pitchChange = 60f + random.nextFloat() * 20f;
        if (random.nextBoolean()) {
            pitchChange = -pitchChange;
        }
        
        float newPitch = player.getPitch() + pitchChange;
        if (newPitch > 90f) newPitch = 90f;
        if (newPitch < -90f) newPitch = -90f;
        
        player.setPitch(newPitch);
    }

    private static void handleSpin360(ClientPlayerEntity player) {
        float spin = 360f;
        if (random.nextBoolean()) {
            spin = -spin;
        }
        player.setYaw(player.getYaw() + spin);
    }
}
