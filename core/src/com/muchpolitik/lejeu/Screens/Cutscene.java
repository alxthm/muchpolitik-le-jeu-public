package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.CutscenesObjects.Dialog;
import com.muchpolitik.lejeu.CutscenesObjects.Dude;
import com.muchpolitik.lejeu.CutscenesObjects.SpeechObject;
import com.muchpolitik.lejeu.CutscenesObjects.SpeechBubble;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Stages.PopUp;

/**
 * The parent class for cutscenes. It can be used directly for simple scenes, with no movement.
 * Otherwise it should be extended and special movements should be defined.
 */
public class Cutscene extends InputAdapter implements CustomScreen {

    private Stage stage;
    private Skin skin;
    private LeJeu game;

    private SpeechBubble speechBubble1, speechBubble2, currentBubble;
    private Array<SpeechObject> dialogText;
    private Sprite backgroundSprite, character1Sprite, character2Sprite;
    private Music music;
    private PopUp popUp;

    private boolean displayingText;
    private int currentSpeechIndex;
    private String jsonFileName, followingCutscene, followingLevel;


    public Cutscene(LeJeu game, String jsonFileName) {
        stage = new Stage(new FitViewport(2560, 1440));
        this.game = game;
        this.jsonFileName = jsonFileName;
    }

    @Override
    public void load() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        loadAssets();

        // create skip button
        TextButton skipButton = new TextButton("skip", skin);
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToNextScreen();
            }
        });
        table.top().right();
        table.add(skipButton).size(250, 130).pad(50);

        // create actors
        SpriteDrawable background = new SpriteDrawable(backgroundSprite);
        Dude dude1 = new Dude(character1Sprite);
        dude1.setPosition(0, 500);
        Dude dude2 = new Dude(character2Sprite);
        dude2.setPosition(2128, 500);
        speechBubble1 = new SpeechBubble(true, skin);
        speechBubble2 = new SpeechBubble(false, skin);

        // add actors to the stage
        table.setBackground(background);
        stage.addActor(dude1);
        stage.addActor(dude2);
        stage.addActor(speechBubble1);
        stage.addActor(speechBubble2);

        // start displaying text
        currentSpeechIndex = 0;
        doNextAction(currentSpeechIndex);


        InputMultiplexer multiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (popUp != null && popUp.isOpen()) {
            popUp.act();
            popUp.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().update();

        if (popUp != null && popUp.isOpen()) {
            popUp.getViewport().update(width, height);
            popUp.getCamera().update();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        music.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundSprite.getTexture().dispose();
        character1Sprite.getTexture().dispose();
        character2Sprite.getTexture().dispose();
        music.dispose();
        if (popUp != null)
            popUp.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (displayingText && !currentBubble.isTextFullyDisplayed())
            // if there is more text to display
            currentBubble.finishDispText();

        else {
            // if all the text is already displayed

            if (currentSpeechIndex + 1 < dialogText.size) {
                // if dialog is not finished

                if (doNextAction(currentSpeechIndex + 1))
                    // if an action was executed
                    currentSpeechIndex++;
            } else
                // if dialog is finished
                goToNextScreen();
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return touchDown(0, 0, 0, 0);           // go next on any key typed
    }

    private void loadAssets() {
        // load dialog file containing all info
        skin = game.getSkin();
        Json json = new Json();
        Dialog dialog = json.fromJson(Dialog.class, Gdx.files.internal("data/dialogs/" + jsonFileName + ".json"));

        // load images
        backgroundSprite = new Sprite(new Texture("graphics/backgrounds/cutscenes/" + dialog.getBackgroundName()));
        character1Sprite = new Sprite(new Texture("graphics/heads/" + dialog.getChar1Name()));
        character2Sprite = new Sprite(new Texture("graphics/heads/" + dialog.getChar2Name()));
        character2Sprite.flip(true, false);

        // load text and following cutscene and level info
        dialogText = dialog.getText();
        followingCutscene = dialog.getFollowingCutscene();
        followingLevel = dialog.getFollowingLevel();

        // load music
        String musicName = dialog.getMusicName();
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/" + musicName + ".ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());
    }

    /**
     * Start displaying next speech bubble or a custom action.
     *
     * @param speechIndex the index of the next speech object
     * @return If a new action was executed and if the speechIndex should be incremented
     */
    private boolean doNextAction(int speechIndex) {
        String text = dialogText.get(speechIndex).getText();

        displayingText = false; // by default

        // if there is a popup on the screen
        if (popUp!= null && popUp.isOpen()) {
            if (!popUp.isMovingOut())
                // if it has not begun moving out, remove it
                popUp.removePopUp();

            // in any case, no new speech was displayed
            return false;

        } else {
            // if there is no popup open on the screen, we can do the next action
            switch (text) {
                case "zoom":
                    zoomOnChar(dialogText.get(speechIndex).getChar());
                    break;
                case "dezoom":
                    dezoomOnChar(dialogText.get(speechIndex).getChar());
                    break;
                case "displayImage":
                    String imageName = dialogText.get(speechIndex).getImageName();
                    if (popUp != null)
                        popUp.dispose();
                    popUp = new PopUp(imageName);
                    popUp.displayPopUp();
                    break;
                default:
                    // in case this is the first bubble, and currentBubble has not been initialized
                    if (currentBubble != null)
                        currentBubble.hide();
                    // display next bubble
                    displayNextBubble(speechIndex);
                    break;
            }
        }
        return true;
    }

    private void zoomOnChar(final int charNb) {
        final OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        Action zoomIn = new Action() {
            @Override
            public boolean act(float delta) {
                camera.zoom -= 0.01f;
                camera.update();
                return true;
            }
        };
        Action moveRight = new Action() {
            @Override
            public boolean act(float delta) {
                if (charNb == 1)
                    camera.translate(-12, 0);
                else if (charNb == 2)
                    camera.translate(12, 0);
                camera.update();
                return true;
            }
        };

        stage.addAction(Actions.repeat(40, Actions.parallel(zoomIn, moveRight)));
    }

    private void dezoomOnChar(final int charNb) {
        final OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        Action zoomOut = new Action() {
            @Override
            public boolean act(float delta) {
                camera.zoom += 0.01f;
                camera.update();
                return true;
            }
        };
        Action moveLeft = new Action() {
            @Override
            public boolean act(float delta) {
                if (charNb == 1)
                    camera.translate(12, 0);
                else if (charNb == 2)
                    camera.translate(-12, 0);
                camera.update();
                return true;
            }
        };
        stage.addAction(Actions.repeat(40, Actions.parallel(zoomOut, moveLeft)));
    }

    /**
     * Start displaying next speech with the right bubble (number 1 or 2)
     */
    private void displayNextBubble(int speechIndex) {
        int currentCharacter = dialogText.get(speechIndex).getChar();
        currentBubble = (currentCharacter == 1) ? speechBubble1 : speechBubble2;
        currentBubble.startDispText(dialogText.get(speechIndex).getText());
        displayingText = true;
    }

    /**
     * As the dialog is finished, go to the next cutscene, the next level, or go back to the level map
     */
    public void goToNextScreen() {
        if (followingCutscene != null)
            game.changeScreen(this, new Cutscene(game, followingCutscene));
        else if (followingLevel != null)
            game.changeScreen(this, new Level(game, followingLevel));
        else
            game.changeScreen(this, new LevelMap(game));
    }
}
