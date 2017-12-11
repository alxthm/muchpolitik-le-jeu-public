package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * A popup to ask the player if he wants to rate the app on Google Play
 */

public class RatingsPopup extends PopUp{

    public RatingsPopup(Skin skin, final Preferences prefs, final Stage mainStage) {
        super(skin);

        // create popup window and table container
        Table table = new Table(skin);
        table.setFillParent(true);
        Window popupWindow = new Window("noter le jeu sur le play store", skin);
        popupWindow.setMovable(false);

        // create widgets
        Label text = new Label("tu aime ce jeu? mci de nou le faire savoir et de nous encouragé !", skin);
        text.setWrap(true);
        TextButton yesButton = new TextButton("oui !", skin);
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // stop asking ratings after that
                prefs.putBoolean("askForRatings", false);
                prefs.flush();

                // remove popup and give back input to the level map
                removePopUp();
                Gdx.input.setInputProcessor(mainStage);

                // open the Play Store
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.muchpolitik.lejeu");
            }
        });
        TextButton noButton = new TextButton("nn tg", skin);
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // stop asking ratings after that
                prefs.putBoolean("askForRatings", false);
                prefs.flush();

                // remove popup and give back input to the level map
                removePopUp();
                Gdx.input.setInputProcessor(mainStage);
            }
        });
        TextButton laterButton = new TextButton("plus tard", skin);
        laterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // remove popup and give back input to the level map
                removePopUp();
                Gdx.input.setInputProcessor(mainStage);
            }
        });


        // add all widgets to the table
        popupWindow.defaults().pad(50);
        popupWindow.add(text).prefWidth(1000).colspan(3);
        popupWindow.row();
        popupWindow.defaults().prefSize(250, 130);
        popupWindow.add(yesButton);
        popupWindow.add(noButton);
        popupWindow.add(laterButton);


        table.add(popupWindow).padTop(200).top();
        addActor(table);
    }
}
