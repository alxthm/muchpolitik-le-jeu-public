package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Screens.MainMenu;

/**
 * Stage containing game over menu.
 */
public class GameOverMenu extends CustomStage {

    private Level level;
    private Music music;


    public GameOverMenu(Level lvl, final LeJeu game) {
        super(new FitViewport(2560, 1440));
        level = lvl;

        // load skin
        Skin skin = game.getSkin();

        // create a table filling the screen and the menu window
        Table table = new Table();
        table.setFillParent(true);
        Window window = new Window("game over", skin);
        window.setMovable(false);

        Label infoLabel = new Label("maleurseument c'est perdu !!", skin, "ui-white");

        TextButton restartButton = new TextButton("recommoncer", skin);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // reload level test
                game.changeScreen(level, new Level(game, level.getName()));
            }
        });
        TextButton mainMenuButton = new TextButton("menu principale", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(level, new MainMenu(game));
            }
        });

        // add actors to the window
        window.add(infoLabel).pad(40);
        window.row();
        window.add(restartButton).pad(60).prefSize(500,100);
        window.row();
        window.add(mainMenuButton).pad(60).prefSize(500,100);

        table.add(window);
        addActor(table);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/menu_pause.ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());

        // start playing menu music
        music.play();


        moveIn();
    }


    @Override
    public void dispose() {
        super.dispose();

        music.stop();
        music.dispose();
    }
}
