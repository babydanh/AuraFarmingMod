Kế hoạch phát triển PvP Camera Mod
Dự án này sẽ tạo ra một mod PvP (client-side) cho Minecraft 1.21.1, cho phép người chơi thực hiện các pha xoay góc nhìn (camera) đột ngột bằng các phím tắt. Các kỹ thuật này thường được dùng để đánh lạc hướng đối thủ (như kỹ thuật "jerk" hoặc múa trong PvP).

⚠️ User Review Required
Bạn vui lòng xem qua các tính năng dưới đây xem đã đúng ý bạn chưa nhé. Nếu có chỗ nào cần sửa, hãy phản hồi lại cho mình.

❓ Open Questions
Quay 360 độ (Nút 2): Bạn muốn nhân vật quay 360 độ ngay lập tức trong 1 tích tắc, hay quay từ từ theo vòng tròn (nhìn thấy rõ quá trình xoay) trong khoảng nửa giây?
Nút bấm: Bạn muốn gán mặc định là nút nào trên bàn phím cho 2 chức năng này? (Ví dụ: phím V và B, hay Z và X? Sau này bạn vẫn có thể tự đổi trong phần Cài đặt điều khiển của game).
Các tính năng chính (Proposed Features)
1. Tính năng Random Snap (Quay góc ngẫu nhiên)
Hoạt động: Khi ấn nút, góc nhìn của bạn sẽ giật sang trái hoặc phải ngẫu nhiên từ 60 đến 80 độ. Đồng thời góc nhìn cũng sẽ hất lên hoặc cúp xuống ngẫu nhiên từ 60 đến 80 độ.
Kỹ thuật: Sử dụng KeyBindingHelper của Fabric để tạo phím tắt. Khi phím được nhấn, dùng hàm lấy số ngẫu nhiên để cộng/trừ ngẫu nhiên một lượng góc vào yaw (ngang) và pitch (dọc) của người chơi.
2. Tính năng 360 Spin (Quay 360 độ)
Hoạt động: Khi ấn nút, góc nhìn sẽ quay một vòng tròn 360 độ (sang trái hoặc phải ngẫu nhiên).
Kỹ thuật: Nếu cần quay mượt (nhìn thấy nhân vật quay vòng tròn), mình sẽ lưu trạng thái quay và tăng dần góc yaw lên trong mỗi Tick của game cho đến khi đủ 360 độ.
Kế hoạch lập trình (Proposed Changes)
Client Module (Bởi vì mod này chỉ tác động đến góc nhìn của bạn)
[NEW] com.example.client.keybind.KeyBindingManager.java
Khởi tạo 2 biến KeyBinding: randomSnapKey và spin360Key.
Đăng ký 2 nút bấm này vào hệ thống tuỳ chỉnh điều khiển của Minecraft.
[NEW] com.example.client.handler.CameraActionHandler.java
Chứa logic tính toán và thay đổi yaw (góc ngang) và pitch (góc dọc) của MinecraftClient.getInstance().player.
Đăng ký vào sự kiện ClientTickEvents.END_CLIENT_TICK.
Kiểm tra nếu randomSnapKey.wasPressed() thì thực thi logic đổi góc random.
Kiểm tra nếu spin360Key.wasPressed() thì kích hoạt trạng thái xoay vòng tròn.
[MODIFY] com.example.client.ExampleModClient.java
Gọi hàm khởi tạo của KeyBindingManager và CameraActionHandler trong hàm onInitializeClient().
Verification Plan
Khởi động game với lệnh ./gradlew runClient.
Vào phần Cài đặt Điều khiển (Controls -> Key Binds) kiểm tra xem 2 nút mới đã xuất hiện chưa.
Vào một thế giới đơn.
Bấm thử nút 1 để xem camera có bị giật ngẫu nhiên 60-80 độ không.
Bấm thử nút 2 để xem nhân vật có thực hiện một cú quay 360 độ trơn tru hay không.