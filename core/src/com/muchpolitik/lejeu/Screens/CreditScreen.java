package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.GameActors.GameObjects.InvincibilityBonus;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.MenuObjects.CreditCard;

/**
 * The screen for basic help on game commands and credits.
 */

public class CreditScreen implements CustomScreen {

    private LeJeu game;
    private Stage stage;
    private CustomScreen thisScreen = this;

    private Skin skin;
    private Music music;
    private Texture billTexture;
    private Texture garbaTexture;
    private Texture florentTexture;
    private Texture julienTexture;
    private Texture leoTexture;


    public CreditScreen(LeJeu game) {
        stage = new Stage(new ExtendViewport(LeJeu.minWidth, LeJeu.minHeight, LeJeu.maxWidth, LeJeu.maxHeight));
        this.game = game;
        skin = game.getSkin();
    }

    @Override
    public void load() {
        Gdx.input.setInputProcessor(stage);

        // load images
        billTexture = new Texture("graphics/heads/billvezay.png");
        garbaTexture = new Texture("graphics/heads/alexandre.png");
        florentTexture = new Texture("graphics/heads/florentintin.png");
        julienTexture = new Texture("graphics/heads/julien.png");
        leoTexture = new Texture("graphics/heads/leo_kaupe.png");


        // create widgets
        Label title = new Label("la dream tim", skin, "title-white");
        CreditCard billCard = new CreditCard(skin, billTexture,
                "Tarsi - scénario, graphisme, UX design",
                "http://www.tarsi.fr",
                "Tarsi est un créateur multimedia de 1m75 sur 40cm. En-dehors de ses activités " +
                        "de rédac'chef de Much Politik, il réalise et écrit des histoires sur son " +
                        "site ouaibe de type 2.0");
        CreditCard garbaCard = new CreditCard(skin, garbaTexture,
                "Alexandre - développeur",
                "Ce jeune virtuoz du clavier métrise pa moins de 507 langages informatik différent " +
                        "et fée partie des anonymousses.\n" +
                        "Il sait aussi comment hacké la nsa et des comptes facebok");
        CreditCard florentCard = new CreditCard(skin, florentTexture,
                "Floran le hareng - graphisme, animations",
                "https://www.youtube.com/c/Floranlehareng",
                "Un mek qui fais des dessin et des vidé eaux !! Au fond de lui cest un " +
                        "harang mirobolan...");
        CreditCard julienCard = new CreditCard(skin, julienTexture, "julien", "text randomext randomext randomext randomext randomext randomext randomext randomext randomext random");
        CreditCard leoCard = new CreditCard(skin, leoTexture,
                "Leopzl - compositeur",
                "https://soundcloud.com/leo-kaupe",
                "musicien qui fait de la musique très musique à base de chiptune et de synthwave ppr");

        // create the container Table, ScrollPane, and back button
        Table container = new Table(skin);
        container.background("background-blue");
        ScrollPane scrollPane = new ScrollPane(container, skin);
        scrollPane.setFillParent(true);
        scrollPane.setOverscroll(false, false);
        TextButton backButton = new TextButton("retour", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(thisScreen, new MainMenu(game));
            }
        });
        backButton.setSize(250, 130);
        backButton.setPosition(50, stage.getHeight() - backButton.getHeight() - 50);


        // put all the widgets in the table
        container.defaults().pad(50, 500, 50, 100);
        container.add(title).center();
        container.row();
        container.defaults().prefWidth(stage.getWidth() - 600);
        container.add(billCard);
        container.row();
        container.add(garbaCard);
        container.row();
        container.add(florentCard);
        container.row();
        container.add(julienCard);
        container.row();
        container.add(leoCard);


        // add non-moving widgets directly to the stage
        stage.addActor(scrollPane);
        stage.addActor(backButton);


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

    }

    @Override
    public void dispose() {
        billTexture.dispose();
        garbaTexture.dispose();
        florentTexture.dispose();
        julienTexture.dispose();
        leoTexture.dispose();

        stage.dispose();
        music.dispose();
    }
}
