package net.minecraft.client.input;

public class CharacterEvent {
    private final int codePoint;
    private final int modifiers;

    public CharacterEvent(int codePoint) {
        this(codePoint, 0);
    }

    public CharacterEvent(char codePoint, int modifiers) {
        this((int) codePoint, modifiers);
    }

    public CharacterEvent(int codePoint, int modifiers) {
        this.codePoint = codePoint;
        this.modifiers = modifiers;
    }

    public int getCodePoint() {
        return this.codePoint;
    }

    public int codepoint() {
        return this.codePoint;
    }

    public String codepointAsString() {
        return new String(Character.toChars(this.codePoint));
    }

    public boolean isAllowedChatCharacter() {
        return this.codePoint != 167
                && this.codePoint >= 32
                && this.codePoint != 127
                && Character.isValidCodePoint(this.codePoint);
    }

    public int input() {
        return this.codePoint;
    }

    public int getInput() {
        return this.codePoint;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public int modifiers() {
        return this.modifiers;
    }
}
