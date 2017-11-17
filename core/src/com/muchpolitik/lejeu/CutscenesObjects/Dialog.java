package com.muchpolitik.lejeu.CutscenesObjects;

import com.badlogic.gdx.utils.Array;

/**
 * Contains an array of Speech, and file locations for assets (background, character images)
 */
public class Dialog {
    private String backgroundName, char1Name, char2Name;
    private Array<SpeechObject> text;

    public String getBackgroundName() {
        return backgroundName;
    }

    public String getChar1Name() {
        return char1Name;
    }

    public String getChar2Name() {
        return char2Name;
    }

    public Array<SpeechObject> getText() {
        return text;
    }

    public void setText(Array<SpeechObject> text) {
        this.text = text;
    }
}
