package com.muchpolitik.lejeu.MenuObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.muchpolitik.lejeu.Screens.Shop;

/**
 * Button for costume cards in the shop. It can be in 3 states, depending on whether
 * the costume is owned or not, and equipped or not.
 */
public class CostumeButton extends TextButton {

    protected enum State {
        notOwned,
        notEquipped,
        Equipped
    }
    private State state;
    private Preferences prefs;

    private String name;
    private int price;

    public CostumeButton(Skin skin, final Shop shop, final CostumeCard container, boolean owned, boolean equipped) {
        super("", skin);
        prefs = shop.getPrefs();
        name = container.getCostumeName();
        price = container.getCostumePrice();

        if (equipped) {
            state = State.Equipped;
            setText("costume equipé");
            setDisabled(true);
        }
        else if (owned) {
            state = State.notEquipped;
            setText("équiper");
        }
        else {
            state = State.notOwned;
            setText("acheter : " + price + " pièce");
        }

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (state) {
                    case notOwned:
                        if (shop.getMoney() >= price) {
                            Gdx.app.debug("shop", "on a " + shop.getMoney());


                            // buy costume, equip it and save preferences
                            shop.updateMoney(shop.getMoney() - price);
                            prefs.putInteger("money", shop.getMoney());
                            prefs.putBoolean(name + "CostumeOwned", true);
                            prefs.putString("equippedCostume", name);
                            prefs.flush();

                            Gdx.app.debug("shop", "costume acheté, et il reste maintenant " + shop.getMoney());

                            // change style
                            shop.unequipCostume(container);
                            state = State.Equipped;
                            setText("costume equipé");
                            setDisabled(true);
                        } else {
                            shop.displayInfoPopUp();
                        }
                        break;

                    case notEquipped:
                        // equip the costume and save it in preferences
                        prefs.putString("equippedCostume", name);
                        prefs.flush();

                        // change style
                        shop.unequipCostume(container);
                        state = State.Equipped;
                        setText("costume equipé");
                        setDisabled(true);
                        break;

                    case Equipped: // impossible - button disabled
                        break;
                }
            }
        });

    }

    public void setState(State state) {
        this.state = state;
    }
}
