package com.muchpolitik.lejeu.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Json;
import com.muchpolitik.lejeu.GameActors.Player;
import com.muchpolitik.lejeu.MenuObjects.LevelInfo;
import com.muchpolitik.lejeu.Stages.BackgroundStage;
import com.muchpolitik.lejeu.Stages.ConfirmExitMenu;
import com.muchpolitik.lejeu.Stages.GameOverMenu;
import com.muchpolitik.lejeu.Stages.GamePausedMenu;
import com.muchpolitik.lejeu.Stages.GameStage;
import com.muchpolitik.lejeu.LeJeu;
import com.muchpolitik.lejeu.Stages.GameUIStage;
import com.muchpolitik.lejeu.Stages.GameWinMenu;

import java.util.Objects;

import javax.xml.soap.Text;

/**
 * The world class.
 */
public class Level implements CustomScreen {

    private int money = 0, nbOfKeys, nbOfKeysFound = 0;
    private String name;
    private String followingCutscene;
    private boolean confirmExitMenuOpen = false;

    public enum GameState {
        Playing,
        Paused,
        GameOver,
        GameWin
    }

    public enum GameOverCause {
        PlayerDead,
        TimesUp
    }

    private GameState state;
    private BackgroundStage bgStage;
    private GameStage gameStage;
    private GameUIStage gameUIStage;
    private GamePausedMenu gamePausedMenu;
    private GameOverMenu gameOverMenu;
    private GameWinMenu gameWinMenu;
    private ConfirmExitMenu confirmExitMenu;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TextureAtlas gameObjectsAtlas;
    private static ShapeRenderer shapeRenderer; // for debugging

    private LeJeu game;
    private Preferences prefs;
    private LevelInfo levelInfo;

    private Music levelMusic;


    /**
     * Create a new Level, able to load itself with its name.
     *
     * @param game current game
     * @param name level name (ex : Anar/0, KKK/3, Mairie/Boss, etc)
     */
    public Level(LeJeu game, String name) {
        this.game = game;
        this.name = name;
        state = GameState.Playing;
    }

    @Override
    public void load() {
        // get game preferences
        prefs = game.getPrefs();

        // load level info
        Json json = new Json();
        levelInfo = json.fromJson(LevelInfo.class, Gdx.files.internal("data/levels/" + name + ".json"));
        // TODO: comment out for a normal release - ok
        // FOR EASIER LEVEL DESIGN (load level data from an external directory at the root of the app)
        //levelInfo = json.fromJson(LevelInfo.class, Gdx.files.local(name + ".json"));
        followingCutscene = levelInfo.getFollowingCutscene();

        // TODO: comment out for a normal release - ok
        // FOR EASIER LEVEL DESIGN (load level data from an external directory at the root of the app)
        //FileHandleResolver debugFileHandleReolver = new LocalFileHandleResolver();
        //TmxMapLoader debugMapLoader = new TmxMapLoader(debugFileHandleReolver);
        //map = debugMapLoader.load(name + "-map.tmx");


        // load map and create mapRenderer
        map = new TmxMapLoader().load("data/levels/" + name + "-map.tmx");
        int tileSize = map.getTileSets().getTileSet("tileset").getProperties().get("tilewidth", int.class);
        float unitScale = (float) 1 / tileSize;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        // load most graphics
        gameObjectsAtlas = new TextureAtlas("graphics/gameObjects.atlas");

        // load background
        Texture backgroundTexture = new Texture("graphics/backgrounds/levels/" + levelInfo.getBackgroundName());

        // set game stages
        bgStage = new BackgroundStage(backgroundTexture);
        gameStage = new GameStage(this, levelInfo.getWorld());
        if (levelInfo.isTimedLevel())
            gameUIStage = new GameUIStage(this, game.getSkin(), levelInfo.getTimeToFinishLevel());
        else
            gameUIStage = new GameUIStage(this, game.getSkin());
        Gdx.input.setInputProcessor(gameUIStage);


        // load levelMusic
        String worldName;
        if (name.toLowerCase().contains("anar"))
            worldName = "anar";
        else if (name.toLowerCase().contains("kkk"))
            worldName = "kkk";
        else if (name.toLowerCase().contains("sovietski"))
            worldName = "sovietski";
        else
            worldName = "ville";
        levelMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/level_" + worldName + ".ogg"));
        levelMusic.setLooping(true);
        levelMusic.setVolume(game.getMusicVolume());
    }

    @Override
    public void show() {
        levelMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update actors and UI elements
        if (state != GameState.Paused) {
            gameStage.act();
            gameUIStage.act();
        }

        // render background
        bgStage.act();
        bgStage.draw();

        // render tiled map
        gameStage.getCamera().update();
        mapRenderer.setView((OrthographicCamera) gameStage.getCamera());
        mapRenderer.render();

        // render gameStage (and debug lines)
        shapeRenderer.setProjectionMatrix(gameStage.getCamera().combined); // for debugging
        shapeRenderer.begin();
        gameStage.draw();
        shapeRenderer.end();

        // render UI
        gameUIStage.draw();

        // render menus currently opened : gameOver, gameWin, or pauseMenu
        // and possibly a confirmExitMenu on top.
        if (state == GameState.GameOver) {
            gameOverMenu.act();
            gameOverMenu.draw();
        } else if (state == GameState.GameWin) {
            gameWinMenu.act();
            gameWinMenu.draw();
        } else if (state == GameState.Paused) {
            gamePausedMenu.act();
            gamePausedMenu.draw();
        }
        if (confirmExitMenuOpen) {
            confirmExitMenu.act();
            confirmExitMenu.draw();
        }


    }

    @Override
    public void resize(int width, int height) {
        bgStage.getViewport().update(width, height);
        bgStage.getCamera().update();
        gameStage.getViewport().update(width, height);
        gameStage.getCamera().update();
        if (gameOverMenu != null) {
            gameOverMenu.getViewport().update(width, height);
            gameOverMenu.getCamera().update();
        }
        if (gameWinMenu != null) {
            gameWinMenu.getViewport().update(width, height);
            gameWinMenu.getCamera().update();
        }
        if (gameUIStage != null) {
            gameUIStage.getViewport().update(width, height);
            gameUIStage.getCamera().update();
        }
        if (gamePausedMenu != null) {
            gamePausedMenu.getViewport().update(width, height);
            gamePausedMenu.getCamera().update();
        }
        if (confirmExitMenu != null) {
            confirmExitMenu.getViewport().update(width, height);
            confirmExitMenu.getCamera().update();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        // load pause menu when user goes back to the game (if the game isn't already paused or finished)
        pauseGame();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        //shapeRenderer.dispose(); -> cause bug when level is disposed by pause/gameOver menu then win menu

        // dispose game graphics
        gameObjectsAtlas.dispose();

        // dispose background
        bgStage.dispose();

        // dispose level music
        levelMusic.dispose();

        // dispose current open menu (if there is any) to dispose its music
        switch (state) {
            case Paused:
                gamePausedMenu.dispose();
                break;
            case GameOver:
                gameOverMenu.dispose();
                break;
            case GameWin:
                gameWinMenu.dispose();
                break;
        }
    }

    /**
     * Change game state and load game over menu.
     */
    public void loadGameOverMenu(GameOverCause cause) {
        if (state == GameState.Playing) {
            state = GameState.GameOver;
            gameUIStage.releaseButtons(); // so the player doesn't continue to move

            levelMusic.pause();
            gameOverMenu = new GameOverMenu(this, game, cause);
            Gdx.input.setInputProcessor(gameOverMenu);
        }
    }

    /**
     * Change game state and load the level completed / game win menu.
     */
    public void loadGameWinMenu() {
        if (state == GameState.Playing) {
            state = GameState.GameWin;
            gameStage.removePlayer(); // so the player disappears and cannot be killed
            gameUIStage.releaseButtons(); // so the player doesn't continue to move

            // add money earned to game preferences
            int totalMoney = money + prefs.getInteger("money");
            prefs.putInteger("money", totalMoney);

            // save progress in game preferences
            prefs.putBoolean(name + "Done", true);

            // unlock next levels
            for (String levelUnlocked : levelInfo.getLevelsUnlocked())
                if (levelUnlocked.equals("Mairie/1")) {
                    // unlocking Mairie/1 requires having finished other levels
                    switch (name) {
                        case "Sovietski/timer":
                            if (prefs.getBoolean("KKK/timerDone") && prefs.getBoolean("Anar/timerDone"))
                                prefs.putBoolean(levelUnlocked + "Unlocked", true);
                            break;
                        case "KKK/timer":
                            if (prefs.getBoolean("Sovietski/timerDone") && prefs.getBoolean("Anar/timerDone"))
                                prefs.putBoolean(levelUnlocked + "Unlocked", true);
                            break;
                        case "Anar/timer":
                            if (prefs.getBoolean("KKK/timerDone") && prefs.getBoolean("Sovietski/timerDone"))
                                prefs.putBoolean(levelUnlocked + "Unlocked", true);
                            break;
                    }
                    Gdx.app.debug("levels finished", prefs.getBoolean("KKK/timerDone") + ", "
                            + prefs.getBoolean("Anar/timerDone") + ", "
                            + prefs.getBoolean("Sovietski/timerDone") + ", ");


                } else
                    prefs.putBoolean(levelUnlocked + "Unlocked", true);

            prefs.flush();

            levelMusic.pause();
            gameWinMenu = new GameWinMenu(this, game, totalMoney);
            Gdx.input.setInputProcessor(gameWinMenu);
        }
    }

    /**
     * Change game state and load pause menu (only if Playing state).
     */
    public void pauseGame() {
        if (state == GameState.Playing) {
            state = GameState.Paused;
            gameUIStage.releaseButtons(); // so the player doesn't continue to move

            levelMusic.pause();
            gamePausedMenu = new GamePausedMenu(this, game);
            Gdx.input.setInputProcessor(gamePausedMenu);
        }
    }

    /**
     * Change game state and resume game.
     */
    public void resumeGame() {
        state = GameState.Playing;
        Gdx.input.setInputProcessor(gameUIStage);

        // dispose game menu assets (such as music), and resume level music
        gamePausedMenu.dispose();
        levelMusic.play();
    }

    /**
     * Creates a confirmation dialog box. Should only be called on Paused, GameOver or GameWin state..
     */
    public void confirmExit() {
        if (!confirmExitMenuOpen) {
            confirmExitMenu = new ConfirmExitMenu(this, game);
            confirmExitMenuOpen = true;

            Gdx.input.setInputProcessor(confirmExitMenu);
        }
    }

    /**
     * Stops displaying confirm exit window, et give back input to currently opened menu.
     */
    public void closeConfirmExitMenu() {
        confirmExitMenuOpen = false;

        switch (state) {
            case Paused:
                Gdx.input.setInputProcessor(gamePausedMenu);
                break;
            case GameOver:
                Gdx.input.setInputProcessor(gameOverMenu);
                break;
            case GameWin:
                Gdx.input.setInputProcessor(gameWinMenu);
                break;
        }
    }

    /**
     * The player has found a new key : check if all keys are found and unlock the level if it's the case.
     */
    public void findKey() {
        nbOfKeysFound += 1;

        if (nbOfKeysFound == nbOfKeys)
            // unlock winTrigger once all keys are found
            gameStage.getWinTrigger().setDisabled(false);
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public String getFollowingCutscene() {
        return followingCutscene;
    }

    public Player getPlayer() {
        return gameStage.getPlayer();
    }

    public TiledMap getMap() {
        return map;
    }

    public TextureAtlas getGameObjectsAtlas() {
        return gameObjectsAtlas;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public GameState getState() {
        return state;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public BackgroundStage getBgStage() {
        return bgStage;
    }

    public LeJeu getGame() {
        return game;
    }

    public int getNbOfKeysFound() {
        return nbOfKeysFound;
    }

    public int getNbOfKeys() {
        return nbOfKeys;
    }

    public String getKeyType() {
        if (getName().equals("Ville/timer"))
            // for boss level in Ville world
            return "key-dossier-signatures";
        else if (getName().contains("timer"))
            // for other bosses level
            return "key-dossier-secret";
        else if (getName().contains("Ville"))
            // for regular Ville levels
            return  "key-signature";
        else
            // for regular levels in other worlds
            return  "key-default";

    }

    public void setNbOfKeys(int nbOfKeys) {
        this.nbOfKeys = nbOfKeys;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void addTime(int amount) {
        gameUIStage.addTime(amount);
    }
}
