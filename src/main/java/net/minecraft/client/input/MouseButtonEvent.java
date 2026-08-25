package net.minecraft.client.input;

public class MouseButtonEvent implements InputWithModifiers {
    private final double x;
    private final double y;
    private final int button;
    private final int modifiers;
    private final int clickCount;

    public MouseButtonEvent(double x, double y, int button) {
        this(x, y, button, 1);
    }

    public MouseButtonEvent(double x, double y, int button, int clickCount) {
        this(x, y, button, 0, clickCount);
    }

    public MouseButtonEvent(double x, double y, int button, int modifiers, int clickCount) {
        this.x = x;
        this.y = y;
        this.button = button;
        this.modifiers = modifiers;
        this.clickCount = clickCount;
    }

    public MouseButtonEvent(double x, double y, MouseButtonInfo info) {
        this(x, y, info == null ? 0 : info.button(), info == null ? 0 : info.modifiers(), 1);
    }

    public MouseButtonInfo buttonInfo() {
        return new MouseButtonInfo(this.button, this.modifiers);
    }

    public double getX() {
        return this.x;
    }

    public double x() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double y() {
        return this.y;
    }

    public int getButton() {
        return this.button;
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

    public int getClickCount() {
        return this.clickCount;
    }

    public int clickCount() {
        return this.clickCount;
    }
}
