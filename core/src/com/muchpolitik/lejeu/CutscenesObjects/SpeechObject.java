package com.muchpolitik.lejeu.CutscenesObjects;

/**
 * A reply made by a character, containing the character's type (1st or 2nd) and his text
 */
public class SpeechObject {
    private int charNb;
    private String text;

    public int getChar() {
        return charNb;
    }

    public String getText() {
        return text;
    }
}
