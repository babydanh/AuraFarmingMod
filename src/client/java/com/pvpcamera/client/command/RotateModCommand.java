package com.pvpcamera.client.command;

import com.pvpcamera.client.ExampleModClient;
import com.pvpcamera.client.gui.RotateModConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class RotateModCommand {
    public static void register() {
        try {
            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(ClientCommandManager.literal("rotatemod")
                    .executes(context -> {
                        ExampleModClient.LOGGER.info("[PvPCamera] Mở menu cấu hình qua lệnh Brigadier chuẩn.");
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.send(() -> client.setScreen(new RotateModConfigScreen(client.currentScreen)));
                        return 1;
                    }));
            });
        } catch (Throwable t) {
            ExampleModClient.LOGGER.warn("[PvPCamera] Không thể đăng ký lệnh Brigadier. Có thể bạn đang thiếu Fabric API.", t);
        }
    }
}
