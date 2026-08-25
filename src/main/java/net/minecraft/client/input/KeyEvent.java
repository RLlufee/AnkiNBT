package net.minecraft.client.input;

public class KeyEvent implements InputWithModifiers {
    private final int key;
    private final int scanCode;
    private final int modifiers;

    public KeyEvent(int key, int scanCode, int modifiers) {
        this.key = key;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return this.key;
    }

    public int key() {
        return this.key;
    }

    public int input() {
        return this.key;
    }

    public int getInput() {
        return this.key;
    }

    public int getScanCode() {
        return this.scanCode;
    }

    public int scancode() {
        return this.scanCode;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public int modifiers() {
        return this.modifiers;
    }
}
