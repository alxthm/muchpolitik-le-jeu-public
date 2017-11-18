package com.muchpolitik.lejeu.MenuObjects;

import java.util.ArrayList;

/**
 * This class is made to store in JSON all information required to load a level.
 */
public class LevelInfo {

    private boolean timedLevel;
    private float timeToFinishLevel;
    private String world, precedingCutscene, followingCutscene, backgroundName;
    private ArrayList<String> levelsUnlocked;

    public String getWorld() {
        return world;
    }

    public String getBackgroundName() {
        return backgroundName;
    }

    public boolean isTimedLevel() {
        return timedLevel;
    }

    public float getTimeToFinishLevel() {
        return timeToFinishLevel;
    }

    public String getFollowingCutscene() {
        return followingCutscene;
    }

    public String getPrecedingCutscene() {
        return  precedingCutscene;
    }

    public ArrayList<String> getLevelsUnlocked() {
        return levelsUnlocked;
    }
}
