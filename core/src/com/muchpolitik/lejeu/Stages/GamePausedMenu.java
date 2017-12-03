package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.GameActors.Player;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Screens.MainMenu;

/**
 * Stage containing pause menu
 */
public class GamePausedMenu extends CustomStage {

    private Level level;
    private LeJeu game;
    private Player player;
    private Music music;

    public GamePausedMenu(Level lvl, final LeJeu gam) {
        super(new ExtendViewport(2560, 1440));
        level = lvl;
        game = gam;
        player = level.getPlayer();

        // load skin
        Skin skin = game.getSkin();

        // create a table filling the screen and a window
        Table table = new Table();
        table.setFillParent(true);
        Window window = new Window("menu pause", skin);
        window.setMovable(false);

        // create widgets
        TextButton resumeButton = new TextButton("reprendre", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moveOut();
            }
        });
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

        // for testing
        final Label gravityLabel = new Label("gravité : " + player.getGRAVITY(), skin);
        final Slider gravitySetter = new Slider(0.05f, 3, 0.05f, false, skin);
        gravitySetter.setValue(player.getGRAVITY());
        gravitySetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setGRAVITY(gravitySetter.getValue());
                gravityLabel.setText("gravité : " + player.getGRAVITY());
            }
        });
        final Label jumpForceLabel = new Label("force du saut : " + player.getFIRST_JUMP_SPEED(), skin);
        final Slider jumpForceSetter = new Slider(1, 40, 1, false, skin);
        jumpForceSetter.setValue(player.getFIRST_JUMP_SPEED());
        jumpForceSetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setFIRST_JUMP_SPEED(jumpForceSetter.getValue());
                jumpForceLabel.setText("force du saut : " + player.getFIRST_JUMP_SPEED());
            }
        });
        final Label speedLabel = new Label("vitesse : " + player.getMAX_SPEED_X(), skin);
        final Slider speedSetter = new Slider(1, 15, 1, false, skin);
        speedSetter.setValue(player.getMAX_SPEED_X());
        speedSetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setMAX_SPEED_X(speedSetter.getValue());
                speedLabel.setText("vitesse : " + player.getMAX_SPEED_X());
            }
        });
        final Label accLabel = new Label("accélération : " + player.getACCELERATION(), skin);
        final Slider accSetter = new Slider(0, 1, 0.01f, false, skin);
        accSetter.setValue(player.getACCELERATION());
        accSetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setACCELERATION(accSetter.getValue());
                accLabel.setText("accélération : " + player.getACCELERATION());
            }
        });
        final Label decLabel = new Label("décélération : " + player.getDECELERATION(), skin);
        final Slider decSetter = new Slider(0, 1, 0.01f, false, skin);
        decSetter.setValue(player.getDECELERATION());
        decSetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setDECELERATION(decSetter.getValue());
                decLabel.setText("décélération : " + player.getDECELERATION());
            }
        });
        final Label frcLabel = new Label("friction : " + player.getFRICTION(), skin);
        final Slider frcSetter = new Slider(0, 0.5f, 0.01f, false, skin);
        frcSetter.setValue(player.getFRICTION());
        frcSetter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                player.setFRICTION(frcSetter.getValue());
                frcLabel.setText("friction : " + player.getFRICTION());
            }
        });


        // add actors to the window
        window.defaults().pad(60, 60, 0, 60).prefSize(500, 100);
        window.add(resumeButton);
        window.row();
        window.add(restartButton);
        window.row();
        window.add(mainMenuButton).padBottom(50);
        window.row();
        window.defaults().pad(15);
        window.add(gravityLabel);
        window.add(gravitySetter);
        window.add(accLabel);
        window.add(accSetter);
        window.row();
        window.add(jumpForceLabel);
        window.add(jumpForceSetter);
        window.add(decLabel);
        window.add(decSetter);
        window.row();
        window.add(speedLabel);
        window.add(speedSetter);
        window.add(frcLabel);
        window.add(frcSetter);

        table.add(window);
        addActor(table);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/menu_pause.ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());

        // start playing pause menu music and move the stage in
        music.play();
        moveIn();
    }

    /**
     * Stop the music playing and start the move out animation.
     * Once the animation is done, input goes back to the game.
     */
    private void moveOut() {
        Action lastAction = new Action() {
            @Override
            public boolean act(float delta) {
                level.resumeGame();
                return true;
            }
        };

        super.moveOut(false, lastAction);
        music.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
    }
}
