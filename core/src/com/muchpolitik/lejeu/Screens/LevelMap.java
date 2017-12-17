package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.MenuObjects.ItemInfo;
import com.muchpolitik.lejeu.Stages.RatingsPopup;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelMap implements CustomScreen {

    private LeJeu game;
    private CustomScreen thisScreen = this;
    private Stage stage;
    private Skin skin;
    private Preferences prefs;
    private Music music;

    private Texture backgroundTexture;
    private ArrayList<ImageTextButton> buttonsList;
    private ScrollPane scrollPane;
    private RatingsPopup ratingsPopup;

    private boolean askForRatings = false;

    public LevelMap(LeJeu game) {
        stage = new Stage(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        this.game = game;
        this.skin = game.getSkin();
    }

    public LevelMap(LeJeu game, boolean askForRatings) {
        this(game);
        this.askForRatings = askForRatings;
    }

    @Override
    public void load() {
        prefs = game.getPrefs();
        Gdx.input.setInputProcessor(stage);


        // create the back button
        TextButton backButton = new TextButton("retour", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(thisScreen, new MainMenu(game));
            }
        });
        backButton.setSize(250, 130);
        backButton.setPosition(50, stage.getHeight() - backButton.getHeight() - 50);


        // create the map table and the level buttons
        WidgetGroup map = new WidgetGroup();
        map.setSize(5444, 3396); // background image size, for a 2560x1440 screen


        // create background
        backgroundTexture = new Texture("graphics/backgrounds/levelmap.png");
        Image background = new Image(backgroundTexture);
        background.setBounds(0, 0, map.getWidth(), map.getHeight());


        // load level buttons info
        Json json = new Json();
        ArrayList<ItemInfo> buttonsInfoList = json.fromJson(ArrayList.class, ItemInfo.class,
                Gdx.files.internal("data/levelmap.json"));

        // create all level buttons
        buttonsList = new ArrayList<>();
        for (final ItemInfo levelInfo : buttonsInfoList)
            buttonsList.add(getButton(levelInfo));


        // add level buttons to the map
        float[][] positions = {{30.0f, 1621.0f}, {458.0f, 1671.0f}, {848.0f, 1736.0f}, {1290.0f, 1733.0f}, {1670.0f, 1655.0f}, {2445.0f, 1253.0f}, {3391.0f, 1056.0f}, {3330.0f, 448.0f}, {4192.0f, 12.0f}, {1191.0f, 1275.0f}, {575.0f, 647.0f}, {430.0f, 58.0f}, {1668.0f, 205.0f}, {2226.0f, 2240.0f}, {2908.0f, 2270.0f}, {3526.0f, 2317.0f}, {3747.0f, 2709.0f}, {2700.0f, 1753.0f}, {3373.0f, 1745.0f}, {4001.0f, 1745.0f}, {4668.0f, 1737.0f}};
        for (int i = 0; i < positions.length; i++) {
            float x = positions[i][0];
            float y = positions[i][1];
            ImageTextButton b = buttonsList.get(i);
            b.setPosition(x, y);
            map.addActor(b);
        }


        // create a scroll pane for the map and the background
        Stack stack = new Stack();
        stack.add(background);
        stack.add(map);
        scrollPane = new ScrollPane(stack, skin);
        scrollPane.setupFadeScrollBars(0, 0); // to hide scrollbars
        scrollPane.setOverscroll(false, false);
        Table container = new Table(skin);
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();


        // add the scroll pane and the back button to the stage
        stage.addActor(container);
        stage.addActor(backButton);


        // ONLY FOR DEVELOPER
        //arrangeMapManually(map, scrollPane);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/main_menu.ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());


        // if required, add a popup asking for ratings on the PlayStore
        if (askForRatings) {
            ratingsPopup = new RatingsPopup(skin, game.getPrefs(), stage);
            Gdx.input.setInputProcessor(ratingsPopup);

            // display the popup with an animation
            ratingsPopup.displayPopUp();
        }
    }

    @Override
    public void show() {
        // center the map
        scrollPane.setScrollPercentX(0.05f);
        scrollPane.setScrollPercentY(0.4f);

        // start playing music
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (ratingsPopup != null) {
            ratingsPopup.act();
            ratingsPopup.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getCamera().update();
        stage.getViewport().update(width, height);

        if (ratingsPopup != null) {
            ratingsPopup.act();
            ratingsPopup.draw();
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
        backgroundTexture.dispose();
        music.dispose();
    }

    /**
     * Create a button to load a level.
     *
     * @param levelInfo
     * @return the button
     */
    private ImageTextButton getButton(final ItemInfo levelInfo) {
        final String levelName = levelInfo.getItemName();

        // create an imageTextButton with label under the image
        ImageTextButton button = new ImageTextButton(levelInfo.getText(), skin);
        Label.LabelStyle style = skin.get("game-ui-white", Label.LabelStyle.class);
        button.getLabel().setStyle(style);
        button.setName(levelName);
        button.clearChildren();
        button.add(button.getImage()).row();
        button.add(button.getLabel());


        switch (LeJeu.distributionType) {
            // for normal releases : disable locked levels
            case Release:
                if (prefs.getBoolean(levelName + "Unlocked")) {
                    // add a listener to load either a cutscene or a level
                    if (levelInfo.getPrecedingCutscene() != null)
                        button.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                game.changeScreen(thisScreen, new Cutscene(game, levelInfo.getPrecedingCutscene()));
                            }
                        });
                    else
                        button.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                game.changeScreen(thisScreen, new Level(game, levelName));
                            }
                        });

                    // set button style
                    if (prefs.getBoolean(levelName + "Done")) {
                        if (levelName.toLowerCase().contains("boss")) {
                            button.setStyle(skin.get("boss-level-done", ImageTextButton.ImageTextButtonStyle.class));
                        } else {
                            button.setStyle(skin.get("basic-level-done", ImageTextButton.ImageTextButtonStyle.class));
                        }
                    } else if (levelName.contains("Boss")) {
                        button.setStyle(skin.get("boss-level-unlocked", ImageTextButton.ImageTextButtonStyle.class));
                    } else {
                        button.setStyle(skin.get("basic-level-unlocked", ImageTextButton.ImageTextButtonStyle.class));
                    }

                } else {
                    // if level is locked, set the right style (basic or boss), and set disabled
                    if (levelName.toLowerCase().contains("boss")) {
                        button.setStyle(skin.get("boss-level-done", ImageTextButton.ImageTextButtonStyle.class));
                    } else {
                        button.setStyle(skin.get("basic-level-done", ImageTextButton.ImageTextButtonStyle.class));
                    }
                    button.setDisabled(true);

                }
                break;


            // for level designers only : unlock all levels
            case LevelDesigner:
                // add a listener to load either a cutscene or a level
                if (levelInfo.getPrecedingCutscene() != null)
                    button.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.changeScreen(thisScreen, new Cutscene(game, levelInfo.getPrecedingCutscene()));
                        }
                    });
                else
                    button.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            game.changeScreen(thisScreen, new Level(game, levelName));
                        }
                    });

                // set button style
                if (prefs.getBoolean(levelName + "Done")) {
                    if (levelName.toLowerCase().contains("boss")) {
                        button.setStyle(skin.get("boss-level-done", ImageTextButton.ImageTextButtonStyle.class));
                    } else {
                        button.setStyle(skin.get("basic-level-done", ImageTextButton.ImageTextButtonStyle.class));
                    }
                } else if (levelName.contains("Boss")) {
                    button.setStyle(skin.get("boss-level-unlocked", ImageTextButton.ImageTextButtonStyle.class));
                } else {
                    button.setStyle(skin.get("basic-level-unlocked", ImageTextButton.ImageTextButtonStyle.class));
                }
                break;
        }


        return button;
    }

    /**
     * Creates a window that can be moved for every level button. Each time a window is moved,
     * the list of positions is printed out.
     * Allows the programmer to arrange manually level buttons position.
     */
    private void arrangeMapManually(WidgetGroup map, final ScrollPane scrollPane) {
        final ArrayList<Window> windowsList = new ArrayList<>();

        // allow disabling scrolling to position windows
        stage.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (character == 'd') {
                    scrollPane.setFlickScroll(false);
                } else if (character == 'e')
                    scrollPane.setFlickScroll(true);
                return super.keyTyped(event, character);
            }
        });

        // click listener called each time there is movement
        // print out the list of positions each time there is a position change.
        ClickListener listener = new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                float[][] positions = new float[windowsList.size()][2];
                for (int i = 0; i < windowsList.size(); i++) {
                    float posX = windowsList.get(i).getX();
                    float posY = windowsList.get(i).getY();
                    positions[i] = new float[]{posX, posY};
                }
                String positionString = Arrays.deepToString(positions).replaceAll("\\.0", ".0f")
                        .replace('[', '{').replace(']', '}');
                Gdx.app.debug("Level Map Arrangement", positionString);
            }
        };


        // create windows
        for (ImageTextButton b : buttonsList) {
            final Window w = new Window(b.getName(), skin);
            w.setKeepWithinStage(false);
            w.setSize(b.getWidth(), b.getHeight());
            w.setPosition(b.getX(), b.getY());
            windowsList.add(w);

            w.addListener(listener);

            map.addActor(w);
        }
    }
}
