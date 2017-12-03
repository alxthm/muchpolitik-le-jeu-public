package com.muchpolitik.lejeu.Screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;

/**
 * A custom screen for fade out transitions between screens.
 */
public class TransitionScreen implements Screen {

    private LeJeu game;
    private CustomScreen currentScreen, nextScreen;
    private Stage stage;
    private Table table;

    private float duration = 0.4f;
    private boolean isFadingIn = true, needToLoadNextScreen = false, needToSetNextScreen = false;

    public TransitionScreen(LeJeu game, CustomScreen currentScreen, CustomScreen nextScreen) {
        this.game = game;
        this.currentScreen = currentScreen;
        this.nextScreen = nextScreen;

    }

    @Override
    public void show() {
        // create stage with a black table
        stage = new Stage(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        table = new Table(game.getSkin());
        table.setFillParent(true);
        table.setBackground("white");
        table.setColor(0, 0, 0, 0);
        stage.addActor(table);

        // to avoid creating many transition screens when spamming button
        Gdx.input.setInputProcessor(null);


        table.addAction(sequence(fadeIn(duration/2f), run(new Runnable() {
            @Override
            public void run() {
                // when fade in action is complete, set a flag so we can load nextScreen
                isFadingIn = false;
                needToLoadNextScreen = true;
            }
        }), fadeOut(duration/2f), run(new Runnable() {
            @Override
            public void run() {
                // when the transition is done
                needToSetNextScreen = true;
            }
        })));

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        // load next screen when fade in is done
        if (needToLoadNextScreen) {
            nextScreen.load();
            needToLoadNextScreen = false;
            game.setCurrentScreenState(nextScreen); // so the game can keep track of current screen
        }

        // render appropriate screen behind the black table
        if (isFadingIn)
            currentScreen.render(delta);
        else
            nextScreen.render(delta);
        stage.draw();

        // when fade out is done, move on next screen
        if (needToSetNextScreen) {
            game.setScreen(nextScreen);
            currentScreen.dispose();
        }
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public CustomScreen getCurrentScreen() {
        if (isFadingIn)
            return currentScreen;
        else
            return nextScreen;
    }
}
