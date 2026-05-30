package com.example.client;

import com.example.client.handler.CameraActionHandler;
import com.example.client.keybind.KeyBindingManager;
import net.fabricmc.api.ClientModInitializer;

public class ExampleModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBindingManager.register();
		CameraActionHandler.register();
	}
}