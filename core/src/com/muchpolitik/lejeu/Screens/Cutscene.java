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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.CutscenesObjects.Dialog;
import com.muchpolitik.lejeu.CutscenesObjects.Dude;
import com.muchpolitik.lejeu.CutscenesObjects.SpeechObject;
import com.muchpolitik.lejeu.CutscenesObjects.SpeechBubble;
import com.muchpolitik.lejeu.LeJeu;

/**
 * The parent class for cutscenes. It can be used directly for simple scenes, with no movement.
 * Otherwise it should be extended and special movements should be defined.
 */
public class Cutscene extends InputAdapter implements CustomScreen {

    private Stage stage;
    private Skin skin;
    private LeJeu game;

    private boolean displayingText, acceptingInput = true;
    private int currentSpeechIndex;
    private String jsonFileName;
    private SpeechBubble speechBubble1, speechBubble2, currentBubble;
    private Array<SpeechObject> dialogText;
    private Sprite backgroundSprite, character1Sprite, character2Sprite;
    private Music music;

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
        dude1.setPosition(50, 500);
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
        displayNextBubble();


        InputMultiplexer multiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(multiplexer);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/dialog_trkl.ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());
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
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().update();
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
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (displayingText && !currentBubble.isTextFullyDisplayed())
            currentBubble.finishDispText();

        else if (acceptingInput) {
            currentSpeechIndex += 1;

            if (currentSpeechIndex < dialogText.size) {
                // if dialog is not finished
                doNextAction(dialogText.get(currentSpeechIndex).getText());

            } else {
                goToNextScreen();
            }
        }

        return false;
    }


    @Override
    public boolean keyTyped(char character) {
        return touchDown(0, 0, 0, 0);           // go next on any key typed
    }

    public void loadAssets() {
        // load dialog file containing all info
        skin = game.getSkin();
        Json json = new Json();
        Dialog dialog = json.fromJson(Dialog.class, Gdx.files.internal("data/dialogs/" + jsonFileName + ".json"));

        backgroundSprite = new Sprite(new Texture("graphics/backgrounds/cutscenes/" + dialog.getBackgroundName()));
        character1Sprite = new Sprite(new Texture("graphics/heads/" + dialog.getChar1Name()));
        character2Sprite = new Sprite(new Texture("graphics/heads/" + dialog.getChar2Name()));
        character2Sprite.flip(true, false);
        dialogText = dialog.getText();
    }

    /**
     * start displaying next speech bubble or a custom action.
     * @param text the text of the next speech object
     */
    public void doNextAction(String text) {

        displayingText = false; // by default

        if (text.equals("zoom")) {
            zoomOnChar2();
        }
        else if (text.equals("dezoom")) {
            dezoomOnChar2();
        }
        else {
            // display next bubble
            currentBubble.hide();
            displayNextBubble();
            displayingText = true;
        }
    }

    public void zoomOnChar2() {
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
                camera.translate(12, 0);
                camera.update();
                return true;
            }
        };

        stage.addAction(Actions.repeat(50, Actions.parallel(zoomIn,moveRight)));
    }

    public void dezoomOnChar2() {
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
                camera.translate(-12, 0);
                camera.update();
                return true;
            }
        };
        stage.addAction(Actions.repeat(50, Actions.parallel(zoomOut,moveLeft)));
    }

    /**
     * Start displaying next speech with the right bubble (number 1 or 2)
     */
    public void displayNextBubble() {
        int currentChar = dialogText.get(currentSpeechIndex).getChar();
        currentBubble = currentChar == 1? speechBubble1 : speechBubble2;
        currentBubble.startDispText(dialogText.get(currentSpeechIndex).getText());
        displayingText = true;
    }

    public void goToNextScreen() {
        // dialog is finished, go back to level map
        game.changeScreen(this, new LevelMap(game));
    }
}
