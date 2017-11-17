package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * Stage containing Exit Confirmation dialog box.
 */

public class ConfirmExitMenu extends CustomStage {

    private Level level;

    public ConfirmExitMenu(Level lvl, final LeJeu gam) {
        super(new FitViewport(2560, 1440));
        LeJeu game = gam;
        level = lvl;

        // load skin
        Skin skin = game.getSkin();

        // create a table filling the screen and a window
        Table table = new Table();
        table.setFillParent(true);
        Window window = new Window("confirmassion", skin, "dialog");
        window.setMovable(false);

        // create widgets
        Label infoLabel = new Label("es tu sur de vouloire quitter ??", skin, "ui-white");

        TextButton yesButton = new TextButton("oui", skin);
        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        TextButton noButton = new TextButton("non", skin);
        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                moveOut();
            }
        });

        // add actors to the window
        window.add(infoLabel).pad(100);
        window.row();
        window.add(yesButton).pad(50).size(200,130);
        window.add(noButton).pad(50).size(200,130);

        table.add(window);
        addActor(table);


        moveIn(true);
    }

    /**
     * Start the move out animation.
     * Once the animation is done, input goes back to the current open menu.
     */
    private void moveOut() {
        Action lastAction = new Action() {
            @Override
            public boolean act(float delta) {
                level.closeConfirmExitMenu();
                return true;
            }
        };
        super.moveOut(true, lastAction);
    }

}
