package net.minecraft.client.input;

public final class MouseButtonInfo implements InputWithModifiers {
    private final int button;
    private final int modifiers;

    public MouseButtonInfo(int button, int modifiers) {
        this.button = button;
        this.modifiers = modifiers;
    }

    public int button() {
        return this.button;
    }

    public int input() {
        return this.button;
    }

    public int getInput() {
        return this.button;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public int modifiers() {
        return this.modifiers;
    }
}
