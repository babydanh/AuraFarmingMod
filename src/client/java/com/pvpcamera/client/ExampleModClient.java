package com.pvpcamera.client;

import com.pvpcamera.client.config.RotateModConfig;
import com.pvpcamera.client.handler.CameraActionHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("pvp_camera");

	@Override
	public void onInitializeClient() {
		LOGGER.info("[PvPCamera] Bắt đầu khởi tạo mod trên Client!");
		
		try {
			RotateModConfig.load();
			LOGGER.info("[PvPCamera] Đã tải cấu hình thành công.");
			
			CameraActionHandler.register();
			LOGGER.info("[PvPCamera] Đã đăng ký phím tắt (KeyBindings).");
			
			com.pvpcamera.client.command.RotateModCommand.register();
			LOGGER.info("[PvPCamera] Đã đăng ký lệnh /rotatemod qua Mixin thành công.");
		} catch (Exception e) {
			LOGGER.error("[PvPCamera] CÓ LỖI XẢY RA KHI KHỞI TẠO MOD: ", e);
		}
		
		LOGGER.info("[PvPCamera] Khởi tạo mod hoàn tất!");
	}
}
