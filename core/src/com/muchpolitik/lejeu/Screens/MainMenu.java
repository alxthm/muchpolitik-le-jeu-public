package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;

public class MainMenu implements CustomScreen {

    private LeJeu game;
    private CustomScreen thisScreen = this;
    private Stage stage;
    private Skin skin;
    private Music music;

    private Sprite background;
    private Button shareButton, webButton, fbButton, rateButton, volumeButton;
    private TextButton levelMapButton, shopButton, aboutButton;

    public MainMenu(LeJeu game) {
        stage = new Stage(new FitViewport(2560, 1440));
        this.game = game;
        skin = game.getSkin();
    }

    @Override
    public void load() {
        Gdx.input.setInputProcessor(stage);

        // create table and set background
        Table table = new Table(skin);
        table.setFillParent(true);

        background = new Sprite(new Texture(Gdx.files.internal("graphics/backgrounds/mainmenu-background.png")));
        background.setSize(2560, 1440);
        table.setBackground(new SpriteDrawable(background));

        // create widgets
        Label title = new Label("Much politik - le jeu\n" +
                "menu principal", skin, "title");
        title.setAlignment(Align.center);
        createButtons();

        // add widgets to the table
        table.defaults().center().pad(50, 100, 50, 100).prefSize(700, 150);
        table.add(title).colspan(3);
        table.row();
        table.add(shareButton).prefSize(144);
        table.add(levelMapButton).expandX();
        table.add().prefSize(144);
        table.row();
        table.add(webButton).prefSize(144);
        table.add(shopButton).expandX();
        table.add(rateButton).prefSize(144);
        table.row();
        table.add(fbButton).prefSize(144);
        table.add(aboutButton).expandX();
        table.add(volumeButton).prefSize(144);
        stage.addActor(table);
        levelMapButton.getPadLeftValue();


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/main_menu.ogg"));
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
        stage.getViewport().update(width, height, true);
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
        background.getTexture().dispose();
        stage.dispose();
        music.dispose();
    }

    public void createButtons() {
        // main buttons
        levelMapButton = new TextButton("joué", skin);
        levelMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // load level map
                game.changeScreen(thisScreen, new LevelMap(game));
            }
        });
        shopButton = new TextButton("boutiqque", skin);
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // load store screen
                game.changeScreen(thisScreen, new Shop(game));
            }
        });
        aboutButton = new TextButton("aide", skin);
        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // load credit screen
                game.changeScreen(thisScreen, new CreditScreen(game));
            }
        });

        // small buttons
        shareButton = new Button(skin, "button-share");
        webButton = new Button(skin, "button-web");
        webButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("http://www.muchpolitik.fr");
            }
        });
        fbButton = new Button(skin, "button-facebook");
        fbButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://www.facebook.com/MuchPolitik/");
            }
        });
        rateButton = new Button(skin, "button-rate");
        volumeButton = new Button(skin, "button-volume");
        boolean musicOff = (game.getMusicVolume() == 0);
        volumeButton.setChecked(musicOff);
        volumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!volumeButton.isChecked()) {
                    game.setMusicVolume(LeJeu.DEFAULT_VOLUME);
                    game.setSoundVolume(LeJeu.DEFAULT_VOLUME);
                } else {
                    game.setMusicVolume(0);
                    game.setSoundVolume(0);
                }
                music.setVolume(game.getMusicVolume());
            }
        });
    }
}
