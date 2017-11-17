package com.muchpolitik.lejeu.MenuObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.muchpolitik.lejeu.Screens.Shop;

/**
 * A card displayed in the credit screen, for every one who worked on the game.
 */
public class CreditCard extends Window {

    public CreditCard(Skin skin, Texture imageTexture, String name, String text) {
        super("", skin, "creditCard");
        setMovable(false);
        setKeepWithinStage(false);

        // set up image and labels
        Image image = new Image(imageTexture);
        Label nameLabel = new Label(name, skin, "title-font", "white");
        Label textLabel = new Label(text, skin, "ui-white");
        textLabel.setWrap(true);

        // set up a nested table containing labels
        Table textTable = new Table();
        textTable.defaults().pad(30).top().left();
        textTable.add(nameLabel);
        textTable.row();
        // important otherwise the text label does not get enough space inside the nested table
        textTable.add(textLabel).growX();

        // add all widgets to the table
        defaults().pad(30);
        add(image).size(300);
        // important so the nested table also gets enough space
        add(textTable).growX();

    }
}
