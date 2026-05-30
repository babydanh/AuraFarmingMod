import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class test {
    public static void main(String[] args) throws Exception {
        URL clientUrl = new URL("file:///D:/Duancanhan/fabric-example-mod/.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-clientOnly-48f5f74c97/1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2/minecraft-clientOnly-48f5f74c97-1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2.jar");
        URL commonUrl = new URL("file:///D:/Duancanhan/fabric-example-mod/.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-common-48f5f74c97/1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2/minecraft-common-48f5f74c97-1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2.jar");
        URL libsUrl = new URL("file:///D:/Duancanhan/fabric-example-mod/.gradle/loom-cache/minecraftMaven/net/minecraft/minecraft-common-52430b475d/1.21.11/minecraft-common-52430b475d-1.21.11.jar");
        
        URLClassLoader loader = new URLClassLoader(new URL[]{clientUrl, commonUrl, libsUrl});
        Class<?> clazz = loader.loadClass("net.minecraft.client.KeyMapping");
        for (Constructor<?> c : clazz.getConstructors()) {
            System.out.println(c);
        }
    }
}
