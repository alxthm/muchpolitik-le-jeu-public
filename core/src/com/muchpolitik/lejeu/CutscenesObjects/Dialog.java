package com.muchpolitik.lejeu.CutscenesObjects;

import com.badlogic.gdx.utils.Array;

/**
 * Contains an array of Speech, and file locations for assets (background, character images)
 */
public class Dialog {
    private String backgroundName, char1Name, char2Name, followingCutscene, followingLevel, musicName;
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

    public String getFollowingCutscene() {
        return followingCutscene;
    }

    public String getFollowingLevel() {
        return followingLevel;
    }

    public String getMusicName() {
        return musicName;
    }

    public Array<SpeechObject> getText() {
        return text;
    }

    public void setText(Array<SpeechObject> text) {
        this.text = text;
    }
}
