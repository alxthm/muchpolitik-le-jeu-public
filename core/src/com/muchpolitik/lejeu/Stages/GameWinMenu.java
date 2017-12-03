package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Screens.Cutscene;
import com.muchpolitik.lejeu.Screens.Level;
import com.muchpolitik.lejeu.Screens.LevelMap;
import com.muchpolitik.lejeu.Screens.MainMenu;

/**
 * Stage containing the level completed/win menu.
 */
public class GameWinMenu extends CustomStage {

    private Level level;
    private LeJeu game;
    private Music music;

    public GameWinMenu(Level lvl, LeJeu game, int totalMoney) {
        super(new ExtendViewport(2560, 1440));

        level = lvl;
        this.game = game;

        // create a table filling the screen and a window
        Skin skin = game.getSkin();
        Table table = new Table();
        table.setFillParent(true);
        Window window = new Window("C gagné !", skin);
        window.setMovable(false);

        Label infoLabel = new Label("tu a fini le niveau c mirobolan !!!\n\n" +
                "tu as au total : " + totalMoney + " pièce\n" +
                "clik pour continuée", skin, "ui-white");
        infoLabel.setAlignment(Align.center);
        window.add(infoLabel).pad(80);
        table.add(window);

        addActor(table);


        // load music
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/menu_victory.ogg"));
        music.setLooping(true);
        music.setVolume(game.getMusicVolume());

        // start playing menu music
        music.play();


        moveIn();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // load following cutscene, or level map if there is none
        String cutsceneName = level.getFollowingCutscene();
        if (cutsceneName != null)
            game.changeScreen(level, new Cutscene(game, cutsceneName));
        else
            game.changeScreen(level, new LevelMap(game));

        return true;
    }

    @Override
    public void dispose() {
        super.dispose();

        music.stop();
        music.dispose();
    }
}