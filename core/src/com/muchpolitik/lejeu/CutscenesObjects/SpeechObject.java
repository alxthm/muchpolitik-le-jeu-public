package com.muchpolitik.lejeu.CutscenesObjects;

/**
 * A reply made by a character, containing the character's number (1st or 2nd) and his text.
 * It can also contain all the info to display an image instead of a text.
 */
public class SpeechObject {
    private int charNb;
    private String text, imageName;

    public int getChar() {
        return charNb;
    }

    public String getText() {
        return text;
    }

    public String getImageName() {
        return imageName;
    }
}
