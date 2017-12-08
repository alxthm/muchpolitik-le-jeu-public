package com.muchpolitik.lejeu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.muchpolitik.lejeu.Screens.CreditScreen;
import com.muchpolitik.lejeu.Screens.CustomScreen;
import com.muchpolitik.lejeu.Screens.Cutscene;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Screens.LevelMap;
import com.muchpolitik.lejeu.Screens.MainMenu;
import com.muchpolitik.lejeu.Screens.Shop;
import com.muchpolitik.lejeu.Screens.TransitionScreen;

public class LeJeu extends Game {

    private Preferences prefs;
    private Skin skin;

    private enum CurrentScreenState {
        MainMenu,
        LevelMap,
        Shop,
        Credits,
        Level,
        Cutscene
    }

    private CurrentScreenState currentScreenState;

    public static final float DEFAULT_VOLUME = 0.6f;
    // used to specify viewports virtual screen size
    public static final int minWidth = 2560, minHeight = 1440, maxWidth = 2560, maxHeight = 1920;
    private float musicVolume, soundVolume;
    private boolean audioOn;

    @Override
    public void create() {

        switch (Gdx.app.getType()) {
            // on Android, catch back key
            case Android:
                Gdx.input.setCatchBackKey(true);
                break;
        }

        // TODO: set to LOG_INFO or LOG_NONE once the game is published
        Gdx.app.setLogLevel(Application.LOG_DEBUG); // log all messages


        // get preferences
        prefs = Gdx.app.getPreferences("MPprefs");

        // if game is played for the first time, set default preferences
        if (!prefs.getBoolean("alreadyPlayed")) {
            prefs.putBoolean("alreadyPlayed", true);
            prefs.putBoolean("Ville/0Unlocked", true);
            prefs.putBoolean("defaultCostumeOwned", true);
            prefs.putString("equippedCostume", "default");
            prefs.putBoolean("audioOn", true);
            prefs.flush();
        }

        // get sound preferences
        setAudioOn(prefs.getBoolean("audioOn"));

        // load ui skin
        skin = new Skin(Gdx.files.internal("ui/ui_skin.json"));


        // load main menu
        MainMenu mainMenu = new MainMenu(this);
        mainMenu.load();
        setScreen(mainMenu);
        currentScreenState = CurrentScreenState.MainMenu;
    }

    @Override
    public void render() {
        super.render();

        switch (Gdx.app.getType()) {
            case Android:
                // on Android, catch back key
                if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
                    onBackPressed(true);
                break;

            case Desktop:
            case WebGL:
                // on desktop and browser, catch esc key
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
                    onBackPressed(false);
                break;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }

    /**
     * Change screens with a fade out effect between, using a transition screen.
     */
    public void changeScreen(CustomScreen currentScreen, CustomScreen nextScreen) {
        TransitionScreen transitionScreen = new TransitionScreen(this, currentScreen, nextScreen);
        super.setScreen(transitionScreen);
    }

    /**
     * When back key is pressed on Android, doesn't exit the app immediately, but goes back to
     * another screen or display a confirmation dialog box.
     */
    private void onBackPressed(boolean onAndroid) {
        CustomScreen currentScreen;

        // check if screens aren't already switching
        if (!(getScreen() instanceof TransitionScreen)) {

            // get instance of the current screen
            currentScreen = (CustomScreen) getScreen();

            // change screen according to current screen state
            switch (currentScreenState) {
                case MainMenu:
                    if (onAndroid)
                        Gdx.app.exit();
                    break;

                case LevelMap:
                    changeScreen(currentScreen, new MainMenu(this));
                    break;

                case Shop:
                    changeScreen(currentScreen, new MainMenu(this));
                    break;

                case Credits:
                    changeScreen(currentScreen, new MainMenu(this));
                    break;

                case Level:
                    if (((Level) currentScreen).getState() == Level.GameState.Playing)
                        ((Level) currentScreen).pauseGame();
                    else
                        // pop up a confirmation dialog box that may quit the game.
                        ((Level) currentScreen).confirmExit();
                    break;

                case Cutscene:
                    // same effect as the skip button
                    ((Cutscene) currentScreen).goToNextScreen();
                    break;
            }
        }
    }

    public void setCurrentScreenState(CustomScreen newScreen) {

        if (newScreen instanceof MainMenu)
            currentScreenState = CurrentScreenState.MainMenu;
        else if (newScreen instanceof LevelMap)
            currentScreenState = CurrentScreenState.LevelMap;
        else if (newScreen instanceof Shop)
            currentScreenState = CurrentScreenState.Shop;
        else if (newScreen instanceof CreditScreen)
            currentScreenState = CurrentScreenState.Credits;
        else if (newScreen instanceof Level)
            currentScreenState = CurrentScreenState.Level;
        else if (newScreen instanceof Cutscene)
            currentScreenState = CurrentScreenState.Cutscene;

    }

    public Preferences getPrefs() {
        return prefs;
    }

    public Skin getSkin() {
        return skin;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public boolean isAudioOn() {
        return audioOn;
    }

    /**
     * Set audio and save it in game preferences.
     *
     * @param audioOn
     */
    public void setAudioOn(boolean audioOn) {
        // set preferences
        this.audioOn = audioOn;
        prefs.putBoolean("audioOn", audioOn);
        prefs.flush();

        // set volume
        musicVolume = audioOn ? DEFAULT_VOLUME : 0;
        soundVolume = audioOn ? DEFAULT_VOLUME : 0;
    }
}
