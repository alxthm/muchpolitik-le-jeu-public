package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.MenuObjects.CostumeCard;
import com.muchpolitik.lejeu.MenuObjects.ItemInfo;
import com.muchpolitik.lejeu.Stages.PopUp;

import java.util.ArrayList;

public class Shop implements CustomScreen {

    private LeJeu game;
    private CustomScreen thisScreen = this;
    private Stage stage;
    private PopUp popUp;
    private Skin skin;
    private TextureAtlas gameObjectsAtlas;
    private Preferences prefs;

    private Music music;

    private Label moneyLabel;
    private CostumeCard equippedCostumeCard;

    private int money;

    public Shop(LeJeu game) {
        stage = new Stage(new ExtendViewport(2560, 1440));
        this.game = game;
        skin = game.getSkin();
    }

    @Override
    public void load() {
        Gdx.input.setInputProcessor(stage);

        // get shop preferences
        prefs = game.getPrefs();
        money = prefs.getInteger("money");
        gameObjectsAtlas = new TextureAtlas("graphics/gameObjects.atlas");

        // create ui widgets on the left
        Table leftTable = new Table(skin);
        TextButton backButton = new TextButton("retour", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(thisScreen, new MainMenu(game));
            }
        });
        Image coinImage = new Image(skin, "coin");
        moneyLabel = new Label("x " + money, skin, "ui-white");
        leftTable.defaults().left();
        leftTable.add(backButton).size(250, 130).pad(50).colspan(2);
        leftTable.row();
        leftTable.add(coinImage).prefSize(144);
        leftTable.add(moneyLabel);


        // create an info popup
        popUp = new PopUp(skin, "achat impossible", "il te manqe de l'argen !", "game-ui-red");

        // set up costumes ScrollPane, which contains one widget : costumesTable
        Table costumesTable = new Table();
        ScrollPane costumesPane = new ScrollPane(costumesTable, skin, "shop");
        costumesPane.setFadeScrollBars(false);
        Json json = new Json();
        ArrayList<ItemInfo> costumesList = json.fromJson(ArrayList.class, ItemInfo.class,
                Gdx.files.internal("data/costumes.json")); // get all costumes info from json to an array list

        for (ItemInfo i : costumesList) {
            // check if costume is owned and equipped
            boolean owned, equipped;
            String costumeName = i.getItemName(), equippedCostumeName = prefs.getString("equippedCostume");
            if (costumeName.equals(equippedCostumeName)) {
                equipped = true;
                owned = true;
            } else if (prefs.getBoolean(costumeName + "CostumeOwned")) {
                equipped = false;
                owned = true;
            } else {
                equipped = false;
                owned = false;
            }

            // set up costume card
            CostumeCard card = new CostumeCard(skin, this, i, owned, equipped);
            costumesTable.add(card).size(1800, 700).pad(80);
            costumesTable.row();

            if (equipped)
                equippedCostumeCard = card;
        }


        // put everything in the container Table (left widgets that don't move, and the scroll pane)
        Table container = new Table(skin);
        container.setFillParent(true);
        container.background("background-blue");
        container.left().top();
        container.add(leftTable).top();
        container.add(costumesPane).expand().fill();

        stage.addActor(container);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/menu_shop.ogg"));
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

        if (popUp.isOpen()) {
            popUp.act();
            popUp.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().update();

        if (popUp.isOpen()) {
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
    }

    /**
     * Displays a popup to inform user he doesn't have enough money to buy the costume
     * (if there is no popup currently open).
     */
    public void displayInfoPopUp() {
        if (!popUp.isOpen())
            popUp.displayPopUp();
    }

    /**
     * Change the state of the current equipped costume card to 'notEquipped', so the player
     * can equip it again.
     *
     * @param newCostumeCard The costume card of the newly equipped costume. It will be the only
     *                       one in a 'Equipped' state.
     */
    public void unequipCostume(CostumeCard newCostumeCard) {
        equippedCostumeCard.unequipCostume();
        equippedCostumeCard = newCostumeCard;
    }

    public void updateMoney(int newAmount) {
        money = newAmount;
        moneyLabel.setText("argent : " + money + " pièces");
    }

    public int getMoney() {
        return money;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public TextureAtlas getAtlas() {
        return gameObjectsAtlas;
    }

}
