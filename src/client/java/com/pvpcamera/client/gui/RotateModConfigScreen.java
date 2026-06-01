package com.pvpcamera.client.gui;

import com.pvpcamera.client.config.RotateModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class RotateModConfigScreen extends Screen {
    private final Screen parent;
    private ButtonWidget snapButton;
    private ButtonWidget spinButton;
    private ButtonWidget pitchButton;
    
    private boolean listeningForSnap = false;
    private boolean listeningForSpin = false;
    private boolean listeningForPitch = false;

    public RotateModConfigScreen(Screen parent) {
        super(Text.literal("PvP Camera Mod Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.snapButton = ButtonWidget.builder(getSnapText(), button -> {
            listeningForSnap = true;
            listeningForSpin = false;
            listeningForPitch = false;
            button.setMessage(Text.literal("> Press any key <"));
        }).dimensions(centerX - 100, centerY - 45, 200, 20).build();

        this.spinButton = ButtonWidget.builder(getSpinText(), button -> {
            listeningForSpin = true;
            listeningForSnap = false;
            listeningForPitch = false;
            button.setMessage(Text.literal("> Press any key <"));
        }).dimensions(centerX - 100, centerY - 15, 200, 20).build();

        this.pitchButton = ButtonWidget.builder(getPitchText(), button -> {
            listeningForPitch = true;
            listeningForSnap = false;
            listeningForSpin = false;
            button.setMessage(Text.literal("> Press any key <"));
        }).dimensions(centerX - 100, centerY + 15, 200, 20).build();

        ButtonWidget doneButton = ButtonWidget.builder(Text.literal("Done"), button -> {
            this.close();
        }).dimensions(centerX - 100, centerY + 60, 200, 20).build();

        this.addDrawableChild(snapButton);
        this.addDrawableChild(spinButton);
        this.addDrawableChild(pitchButton);
        this.addDrawableChild(doneButton);
    }

    private Text getSnapText() {
        String name = GLFW.glfwGetKeyName(RotateModConfig.INSTANCE.randomSnapKey, 0);
        return Text.literal("Random Snap Key: " + (name != null ? name.toUpperCase() : "Key " + RotateModConfig.INSTANCE.randomSnapKey));
    }

    private Text getSpinText() {
        String name = GLFW.glfwGetKeyName(RotateModConfig.INSTANCE.spin360Key, 0);
        return Text.literal("360 Spin Key: " + (name != null ? name.toUpperCase() : "Key " + RotateModConfig.INSTANCE.spin360Key));
    }

    private Text getPitchText() {
        String name = GLFW.glfwGetKeyName(RotateModConfig.INSTANCE.randomPitchKey, 0);
        return Text.literal("Random Pitch/Diag Key: " + (name != null ? name.toUpperCase() : "Key " + RotateModConfig.INSTANCE.randomPitchKey));
    }

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        int keyCode = input.key();
        if (listeningForSnap) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                listeningForSnap = false;
                snapButton.setMessage(getSnapText());
                return true;
            }
            RotateModConfig.INSTANCE.randomSnapKey = keyCode;
            RotateModConfig.save();
            listeningForSnap = false;
            snapButton.setMessage(getSnapText());
            return true;
        } else if (listeningForSpin) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                listeningForSpin = false;
                spinButton.setMessage(getSpinText());
                return true;
            }
            RotateModConfig.INSTANCE.spin360Key = keyCode;
            RotateModConfig.save();
            listeningForSpin = false;
            spinButton.setMessage(getSpinText());
            return true;
        } else if (listeningForPitch) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                listeningForPitch = false;
                pitchButton.setMessage(getPitchText());
                return true;
            }
            RotateModConfig.INSTANCE.randomPitchKey = keyCode;
            RotateModConfig.save();
            listeningForPitch = false;
            pitchButton.setMessage(getPitchText());
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
