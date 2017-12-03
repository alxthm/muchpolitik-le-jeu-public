package com.muchpolitik.lejeu.MenuObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.muchpolitik.lejeu.Screens.Shop;

/**
 * A card displayed in the credit screen, for every one who worked on the game.
 */
public class CreditCard extends Window {

    /**
     * Create a new window for credits.
     *
     * @param skin
     * @param imageTexture
     * @param name
     * @param websiteURL
     * @param text
     */
    public CreditCard(Skin skin, Texture imageTexture, String name, final String websiteURL, String text) {
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
        textTable.defaults().pad(10).top().left();
        textTable.add(nameLabel);
        textTable.row();

        // add a label with a link to the website if necessary
        if (websiteURL != null) {
            Label websiteLabel = new Label(websiteURL, skin, "website-link");
            websiteLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Gdx.net.openURI(websiteURL);
                }
            });
            textTable.add(websiteLabel);
            textTable.row();
        }

        // important otherwise the text label does not get enough space inside the nested table
        textTable.add(textLabel).growX();


        // add all widgets to the table
        defaults().pad(30);
        add(image).size(400);
        // important so the nested table also gets enough space
        add(textTable).growX();
    }

    /**
     * Create a new window for credits, without a website label.
     *
     * @param skin
     * @param imageTexture
     * @param name
     * @param text
     */
    public CreditCard(Skin skin, Texture imageTexture, String name, String text) {
        this(skin, imageTexture, name, null, text);
    }

}
