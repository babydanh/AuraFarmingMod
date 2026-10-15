package com.pvpcamera.client.handler;

import com.pvpcamera.client.config.RotateModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class CameraActionHandler {
    private static final Random random = new Random();

    private static boolean wasMenuPressed = false;
    private static boolean wasSnapPressed = false;
    private static boolean wasSpinPressed = false;
    private static boolean wasPitchPressed = false;

    // Trạng thái Animation (chuyển động mượt)
    private static boolean isAnimating = false;
    private static boolean returning = false; // Đang trong pha quay về hay không
    private static float startYaw;
    private static float startPitch;
    private static float targetYaw;
    private static float targetPitch;

    private static int animationTicks = 0;
    private static int totalAnimationTicks = 1;
    private static boolean returnAfter = false; // Có cần tự quay về góc cũ sau khi tới đích không
    private static float lastEasedProgress = 0f;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null)
                return;
            long window = client.getWindow().getHandle();

            // Mở menu cấu hình (Phím R-CTRL mặc định)
            boolean isMenuPressed = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
            if (isMenuPressed && !wasMenuPressed) {
                client.setScreen(new com.pvpcamera.client.gui.RotateModConfigScreen(client.currentScreen));
            }
            wasMenuPressed = isMenuPressed;

            // Bấm Z (Random Snap 60-80 độ)
            boolean isSnapPressed = GLFW.glfwGetKey(window, RotateModConfig.INSTANCE.randomSnapKey) == GLFW.GLFW_PRESS;
            if (isSnapPressed && !wasSnapPressed && !isAnimating) {
                startRandomSnap(client.player);
            }
            wasSnapPressed = isSnapPressed;

            // Bấm X (Spin 360 độ)
            boolean isSpinPressed = GLFW.glfwGetKey(window, RotateModConfig.INSTANCE.spin360Key) == GLFW.GLFW_PRESS;
            if (isSpinPressed && !wasSpinPressed && !isAnimating) {
                startSpin360(client.player);
            }
            wasSpinPressed = isSpinPressed;

            // Bấm C (Random Lên/Xuống/Chéo)
            boolean isPitchPressed = GLFW.glfwGetKey(window,
                    RotateModConfig.INSTANCE.randomPitchKey) == GLFW.GLFW_PRESS;
            if (isPitchPressed && !wasPitchPressed && !isAnimating) {
                startRandomPitchSnap(client.player);
            }
            wasPitchPressed = isPitchPressed;

            // Xử lý chuyển động mượt mỗi frame
            if (isAnimating) {
                animateCamera(client.player);
            }
        });
    }

    private static void startRandomSnap(ClientPlayerEntity player) {
        startYaw = player.getYaw();
        startPitch = player.getPitch();

        // Random góc từ 60 đến 80 độ
        float yawChange = 60f + random.nextFloat() * 20f;
        if (random.nextBoolean()) {
            yawChange = -yawChange; // Ngẫu nhiên quay trái hoặc phải
        }

        targetYaw = startYaw + yawChange;
        targetPitch = startPitch;

        isAnimating = true;
        returning = false;
        returnAfter = true; // Bắt buộc quay về góc cũ
        animationTicks = 0;
        lastEasedProgress = 0f;
        totalAnimationTicks = 1; // Chỉnh xuống 1 tick
    }

    private static void startSpin360(ClientPlayerEntity player) {
        startYaw = player.getYaw();
        startPitch = player.getPitch();

        float spin = 360f;
        if (random.nextBoolean()) {
            spin = -spin; // Quay trái hoặc phải tuỳ ý
        }

        targetYaw = startYaw + spin;
        targetPitch = startPitch;

        isAnimating = true;
        returning = false;
        returnAfter = false; // Quay 360 độ xong là đã ở vị trí cũ rồi, không cần quay lùi lại
        animationTicks = 0;
        lastEasedProgress = 0f;
        totalAnimationTicks = 1; // Chỉnh xuống 1 tick
    }

    private static void startRandomPitchSnap(ClientPlayerEntity player) {
        startYaw = player.getYaw();
        startPitch = player.getPitch();

        // Random góc Pitch (lên hoặc xuống) 60 đến 80 độ
        float pitchChange = 60f + random.nextFloat() * 20f;
        if (random.nextBoolean()) {
            pitchChange = -pitchChange;
        }

        // Random góc Yaw (trái hoặc phải) 60 đến 80 độ ĐỂ TẠO GÓC CHÉO
        // 50% tỉ lệ chỉ nhìn lên/xuống, 50% tỉ lệ nhìn chéo
        float yawChange = 0f;
        if (random.nextBoolean()) {
            yawChange = 60f + random.nextFloat() * 20f;
            if (random.nextBoolean())
                yawChange = -yawChange;
        }

        targetYaw = startYaw + yawChange;
        targetPitch = startPitch + pitchChange;

        // Giới hạn góc nhìn lên xuống không vượt quá 90 độ
        if (targetPitch > 90f)
            targetPitch = 90f;
        if (targetPitch < -90f)
            targetPitch = -90f;

        isAnimating = true;
        returning = false;
        returnAfter = true; // Bắt buộc quay về góc cũ
        animationTicks = 0;
        lastEasedProgress = 0f;
        totalAnimationTicks = 1; // Chỉnh xuống 1 tick
    }

    private static void animateCamera(ClientPlayerEntity player) {
        animationTicks++;

        // Tính toán phần trăm hoàn thành (0.0 đến 1.0)
        float progress = (float) animationTicks / totalAnimationTicks;
        if (progress > 1.0f)
            progress = 1.0f;

        // Dùng Cubic Ease-Out (1 - (1-x)^3) rất tối ưu cho số lượng tick thấp (như 4 ticks)
        // Nó sẽ quay cực nhanh ở tick đầu tiên và phanh cực kỳ êm ở các tick cuối, tạo cảm giác mượt mà
        float inv = 1.0f - progress;
        float easedProgress = 1.0f - (inv * inv * inv);

        // Tính delta (sự thay đổi) trong tick này
        float deltaEased = easedProgress - lastEasedProgress;
        lastEasedProgress = easedProgress;

        // Cộng thêm góc vào thay vì set cứng, giúp người chơi vẫn có thể lia chuột lúc đang quay
        float moveYaw = (targetYaw - startYaw) * deltaEased;
        float movePitch = (targetPitch - startPitch) * deltaEased;

        float newYaw = player.getYaw() + moveYaw;
        player.setYaw(newYaw);
        player.setPitch(player.getPitch() + movePitch);

        // Cập nhật góc quay của cả cơ thể và đầu để góc nhìn thứ 3 (F5) thấy cả người xoay
        player.setBodyYaw(newYaw);
        player.setHeadYaw(newYaw);

        if (animationTicks >= totalAnimationTicks) {
            if (returnAfter && !returning) {
                // Đã tới đích, bắt đầu pha quay về (đảo ngược start và target)
                returning = true;
                animationTicks = 0;
                lastEasedProgress = 0f;

                float tempY = startYaw;
                startYaw = targetYaw;
                targetYaw = tempY;

                float tempP = startPitch;
                startPitch = targetPitch;
                targetPitch = tempP;
            } else {
                // Đã hoàn thành mọi pha chuyển động
                isAnimating = false;
            }
        }
    }
}
