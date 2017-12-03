package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.MenuObjects.AnimatedDrawable;

public class MainMenu implements CustomScreen {

    private LeJeu game;
    private CustomScreen thisScreen = this;
    private Stage stage;
    private Skin skin;
    private Music music;
    private TextureAtlas uiSkinAtlas;

    private Button shareButton, webButton, fbButton, rateButton, volumeButton;
    private TextButton levelMapButton, shopButton, aboutButton;

    public MainMenu(LeJeu game) {
        stage = new Stage(new ExtendViewport(2560, 1440));
        this.game = game;
        skin = game.getSkin();
    }

    @Override
    public void load() {
        Gdx.input.setInputProcessor(stage);

        // create table and set background
        Table table = new Table(skin);
        table.setFillParent(true);
        table.setBackground("background-blue");

        // load animations
        uiSkinAtlas = new TextureAtlas("ui/ui_skin.atlas");
        Animation<TextureRegion> filibereAnimation = new Animation<TextureRegion>(0.12f,
                uiSkinAtlas.findRegions("mainmenu_filibere"), Animation.PlayMode.LOOP);

        // create widgets
        Image title = new Image(skin, "mainmenu_title");
        Image echarpe = new Image(skin, "mainmenu_echarpe");
        Image filibere = new Image(new AnimatedDrawable(filibereAnimation));
        createButtons();

        // create nested tables
        Table leftButtons = new Table(skin);
        Table middleButtons = new Table(skin);
        Table rightButtons = new Table(skin);

        // add widgets to the left table
        leftButtons.defaults().prefSize(100).padTop(100);
        leftButtons.add(shareButton);
        leftButtons.row();
        leftButtons.add(webButton);
        leftButtons.row();
        leftButtons.add(fbButton);

        // add widgets to the right table
        rightButtons.defaults().prefSize(100).padTop(100);
        rightButtons.add(rateButton);
        rightButtons.row();
        rightButtons.add(volumeButton);

        // add widgets to the middle table
        middleButtons.defaults().prefSize(700, 150).padTop(50).padBottom(50);
        middleButtons.add(levelMapButton);
        middleButtons.row();
        middleButtons.add(shopButton);
        middleButtons.row();
        middleButtons.add(aboutButton);

        // add widgets to the main table
        table.defaults().center().pad(50);
        table.add(title).colspan(5);
        table.row();
        table.add(leftButtons);
        table.add(filibere);
        table.add(middleButtons).expandX();
        table.add(echarpe);
        table.add(rightButtons);
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
        stage.dispose();
        music.dispose();
        uiSkinAtlas.dispose();
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

        aboutButton = new TextButton("la dream tim", skin);
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
        // button checked : audio off, button not checked : audio on
        volumeButton.setChecked(!game.isAudioOn());
        volumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean audioOn = !volumeButton.isChecked();
                game.setAudioOn(audioOn);
                music.setVolume(game.getMusicVolume());
            }
        });
    }
}
