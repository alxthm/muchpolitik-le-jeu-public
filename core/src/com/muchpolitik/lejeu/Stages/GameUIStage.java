package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.GameActors.Player;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * Stage containing all HUD elements, displayed on top of the game stage.
 */
public class GameUIStage extends Stage {

    private Level level;
    private Player player;
    private Label livesLabel, moneyLabel, timeLeftLabel, keysLabel;
    private Button leftButton, rightButton, jumpButton, pauseButton;

    private boolean timedLevel, levelWithKeys;
    private float timeLeft;

    /**
     * Create gameUIStage for a non-timed level.
     */
    public GameUIStage(Level lvl, Skin skin) {
        super(new FitViewport(2560, 1440));
        level = lvl;
        player = level.getPlayer();
        timedLevel = false;
        levelWithKeys = level.getNbOfKeys() > 0;

        Table table = new Table();
        table.setFillParent(true);

        // load graphics
        //createButtonsStyles();
        leftButton = new Button(skin, "arrow-left");
        rightButton = new Button(skin, "arrow-right");
        jumpButton = new Button(skin, "arrow-jump");
        pauseButton = new Button(skin, "button-pause");

        // create UI elements
        Image heartImage = new Image(new Texture("graphics/ui/heart.png"));
        Image coinImage = new Image(new Texture("graphics/ui/coin.png"));
        livesLabel = new Label("x " + player.getLives(), skin, "game-ui-white");
        moneyLabel = new Label("x " + level.getMoney(), skin, "game-ui-white");
        timeLeftLabel = new Label("", skin, "game-ui-red");
        Table controlButtonsTable = new Table(skin);

        Image keyImage = new Image(new Texture("graphics/ui/key.png"));
        if (levelWithKeys)
            keysLabel = new Label(level.getNbOfKeysFound() + "/" + level.getNbOfKeys(), skin, "game-ui-white");

        // add button actions
        leftButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                player.setPressingLeft(true);
                leftButton.setChecked(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                player.setPressingLeft(false);
                leftButton.setChecked(false);
            }
        });
        rightButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                player.setPressingRight(true);
                rightButton.setChecked(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                player.setPressingRight(false);
                rightButton.setChecked(false);
            }
        });
        jumpButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                player.setJumping(true);
                jumpButton.setChecked(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                player.setJumping(false);
                jumpButton.setChecked(false);
            }
        });
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                level.pauseGame();
            }
        });


        // add UI elements to table
        table.defaults().left();
        table.add(heartImage).size(100).pad(12);
        table.add(livesLabel).expandX();
        table.add(pauseButton).size(180).pad(50).right();
        table.row();
        table.add(coinImage).size(100).pad(12);
        table.add(moneyLabel).expandX();
        table.row();
        if (levelWithKeys) {
            table.add(keyImage).size(100).pad(12);
            table.add(keysLabel).expandX();
            table.row();
        }
        table.add(timeLeftLabel).colspan(2).expand().top();

        // add buttons on mobile only
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            table.row();
            controlButtonsTable.add(leftButton).bottom().left();
            controlButtonsTable.add(rightButton).bottom();
            controlButtonsTable.add(jumpButton).padRight(150).bottom().right().expand();
            table.add(controlButtonsTable).colspan(3).bottom().fillX();
        }

        addActor(table);

    }

    /**
     * Create gameUIStage for a timed level.
     */
    public GameUIStage(Level lvl, Skin skin, float timeToFinishLevel) {
        this (lvl, skin);
        timedLevel = true;
        timeLeft = timeToFinishLevel;

        // display time left
        timeLeftLabel.setText("temps restant : " + (int) timeLeft + "s.");
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (timedLevel && level.getState() == Level.GameState.Playing) {
            // update time for timed level
            timeLeft -= delta;
            timeLeftLabel.setText("temps restant : " + (int) timeLeft + "s.");

            if (timeLeft <= 0) {
                // load gameOverMenu
                level.loadGameOverMenu(Level.GameOverCause.TimesUp);
                timeLeftLabel.setText("temps restant : 0s.");
            }
        }

        // update lives, money and keys label
        livesLabel.setText("x " + player.getLives());
        moneyLabel.setText("x " + level.getMoney());
        if (levelWithKeys)
            keysLabel.setText(level.getNbOfKeysFound() + "/" + level.getNbOfKeys());

    }

    @Override
    public boolean keyDown(int keyCode) {
        // start moving player in a direction

        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.enter);
        switch (keyCode) {
            case Input.Keys.LEFT:
                leftButton.fire(event);
                break;

            case Input.Keys.RIGHT:
                rightButton.fire(event);
                break;

            case Input.Keys.SPACE:
            case Input.Keys.UP:
                jumpButton.fire(event);
                break;
        }
        return true; // event has been handled
    }

    @Override
    public boolean keyUp(int keyCode) {
        // stop moving player in a direction
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.exit);
        switch (keyCode) {
            case Input.Keys.LEFT:
                leftButton.fire(event);
                break;
            case Input.Keys.RIGHT:
                rightButton.fire(event);
                break;
            case Input.Keys.SPACE:
            case Input.Keys.UP:
                jumpButton.fire(event);
                break;
        }
        return true; // event has been handled
    }

    /**
     * Release all pressed buttons
     */
    public void releaseButtons() {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.exit);

        if (leftButton.isChecked())
            leftButton.fire(event);
        if (rightButton.isChecked())
            rightButton.fire(event);
        if (jumpButton.isChecked())
            jumpButton.fire(event);
    }

    @Override
    public void dispose() {
    }

    public void addTime(int amount) {
        timeLeft += amount;
    }
}
