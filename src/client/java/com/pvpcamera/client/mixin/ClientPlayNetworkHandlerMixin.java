package com.pvpcamera.client.mixin;

import com.pvpcamera.client.gui.RotateModConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void onSendChatCommand(String command, CallbackInfo ci) {
        if (command.toLowerCase().startsWith("rotatemod")) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.send(() -> client.setScreen(new RotateModConfigScreen(client.currentScreen)));
            ci.cancel();
        }
    }

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String content, CallbackInfo ci) {
        if (content.toLowerCase().startsWith("/rotatemod")) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.send(() -> client.setScreen(new RotateModConfigScreen(client.currentScreen)));
            ci.cancel();
        }
    }
}
