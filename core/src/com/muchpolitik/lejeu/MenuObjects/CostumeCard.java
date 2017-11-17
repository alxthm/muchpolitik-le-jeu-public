package com.muchpolitik.lejeu.MenuObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.muchpolitik.lejeu.Screens.Shop;

/**
 * A card displayed in the shop. Gives info about the costume, and allow player to
 * buy/equip/un-equip it.
 */
public class CostumeCard extends Window {

    private CostumeButton button;

    private String costumeName;
    private int costumePrice;

    public CostumeCard(Skin skin, Shop shop, ItemInfo i, boolean owned, boolean equipped) {
        super(i.getTitleText(), skin);
        setMovable(false);
        setKeepWithinStage(false);
        costumeName = i.getItemName();
        costumePrice = i.getPrice();

        // set up image and labels
        Image image = new Image(shop.getAtlas().findRegion(i.getPictureName(), 0));
        Label textLabel = new Label(i.getText(), skin, "ui-white");
        textLabel.setWrap(true);

        // set up button
        button = new CostumeButton(skin, shop, this, owned, equipped);

        // add all widgets to the table
        defaults().pad(30);
        add(image).size(300);
        add(textLabel).expand().fill();
        row();
        add(button).size(550, 130).colspan(2).right();
    }

    public void unequipCostume() {
        button.setDisabled(false);
        button.setText("équiper");
        button.setState(CostumeButton.State.notEquipped);
    }

    public String getCostumeName() {
        return costumeName;
    }

    public int getCostumePrice() {
        return costumePrice;
    }
}
