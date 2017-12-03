package com.muchpolitik.lejeu.Stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.muchpolitik.lejeu.GameActors.Enemies.Bosses.MarieNougatine;
import com.muchpolitik.lejeu.GameActors.Enemies.Bosses.Marmule;
import com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle.BlinkingObstacle;
import com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles.MarteauObstacle;
import com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles.PouceRouge;
import com.muchpolitik.lejeu.GameActors.Enemies.FallingObstacles.Stalactite;
import com.muchpolitik.lejeu.GameActors.GameObjects.Coin;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.GameActors.Enemies.Ghosts.Fantome;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Didiee;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.FedoraGuy;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Nanarchiste;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Punkh;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Swaggi;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Anonimousse;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.BadaudFemme;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.BadaudHomme;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.BadaudVieux;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Chien;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.Moustachu;
import com.muchpolitik.lejeu.GameActors.Enemies.Pacific.OiseauTwitter;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Draideux;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Hutler;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Russe;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.TouittosKeum;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.TouittosMeuf;
import com.muchpolitik.lejeu.GameActors.Enemies.Sprinters.ProfRoyaliste;
import com.muchpolitik.lejeu.GameActors.Enemies.Sprinters.Skieur;
import com.muchpolitik.lejeu.GameActors.Enemies.DangerousObstacle.StaticObstacle;
import com.muchpolitik.lejeu.GameActors.GameObjects.Hint;
import com.muchpolitik.lejeu.GameActors.GameObjects.InvincibilityBonus;
import com.muchpolitik.lejeu.GameActors.GameObjects.Key;
import com.muchpolitik.lejeu.GameActors.GameObjects.LifeBonus;
import com.muchpolitik.lejeu.GameActors.GameObjects.TimeBonus;
import com.muchpolitik.lejeu.GameActors.Player;
import com.muchpolitik.lejeu.GameActors.GameObjects.ExitDoor;
import com.muchpolitik.lejeu.Screens.Level;

/**
 * Stage containing all actors of the game screen.
 */
public class GameStage extends Stage {

    private String world;
    private Level level;
    private TiledMap map;

    private Player player;
    private Array<Enemy> enemies;
    private Array<Coin> coins;
    private Array<Actor> bonuses;
    private Array<Key> keys;
    private Array<Hint> hints;
    private ExitDoor winTrigger;

    private int tileSize;

    public GameStage(Level lvl, String world) {
        super(new ExtendViewport(10 * 16 / 9f, 10)); // create the stage

        this.world = world;
        level = lvl;
        map = level.getMap();
        tileSize = map.getTileSets().getTileSet("tileset").getProperties().get("tilewidth", int.class);

        // create actors
        player = new Player(level, (OrthographicCamera) getCamera(),
                level.getPrefs().getString("equippedCostume"), 2, 4);
        loadMapEnemies();
        loadMapObjects();
        loadBosses();

        // add actors to the stage
        if (enemies.size > 0) {
            for (Enemy enemy : enemies)
                addActor(enemy);
        }
        if (coins.size > 0) {
            for (Coin coin : coins)
                addActor(coin);
        }
        if (bonuses.size > 0) {
            for (Actor bonus : bonuses)
                addActor(bonus);
        }
        if (keys.size > 0) {
            for (Key key : keys)
                addActor(key);
        }
        if (winTrigger != null) {
            addActor(winTrigger);
        }
        if (hints.size > 0) {
            for (Hint hint : hints)
                addActor(hint);
        }
        addActor(player);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        GdxAI.getTimepiece().update(delta);
        MessageManager.getInstance().update();
    }

    /**
     * Load enemies from the tmx map object layer
     */
    private void loadMapEnemies() {
        // the array of enemies
        Array<Enemy> enemyArray = new Array<Enemy>(2048);

        // load all enemies in the 'enemies' layer of the map
        MapObjects enemies = map.getLayers().get("enemies").getObjects();

        // create a new enemy with the right properties for each one of the array
        for (MapObject enemy : enemies) {
            float startX = enemy.getProperties().get("x", float.class) / tileSize;
            float startY = enemy.getProperties().get("y", float.class) / tileSize;
            float number = 0; // range of action for regular enemies, apparition delay for blinking platforms
            try {
                number = Float.parseFloat(enemy.getProperties().get("type", String.class));
            } catch (Exception e) {
                Gdx.app.debug("chargement des ennemis", "range/apparition delay not defined");
            }


            int offSet = 9; // enemyType has an offset that depends on the number of MapObjects
            int enemyType = enemy.getProperties().get("gid", int.class) - offSet;


            if (world.equals("Ville")) {
                switch (enemyType) {
                    case 1:
                        enemyArray.add(new BadaudFemme(startX, startY, number, this));
                        break;
                    case 2:
                        enemyArray.add(new BadaudHomme(startX, startY, number, this));
                        break;
                    case 3:
                        enemyArray.add(new BadaudVieux(startX, startY, number, this));
                        break;
                    case 4:
                        enemyArray.add(new Chien(startX, startY, number, this));
                        break;
                    case 5:
                        enemyArray.add(new OiseauTwitter(startX, startY, number, this));
                        break;
                    case 6:
                        enemyArray.add(new StaticObstacle("ronces-0", startX, startY, this));
                        break;
                    case 7:
                        enemyArray.add(new StaticObstacle("ronces-1", startX, startY, this));
                        break;
                    case 8:
                        enemyArray.add(new BlinkingObstacle(startX, startY, number, this));
                        break;
                }

            } else if (world.equals("Anar")) {
                switch (enemyType) {
                    case 1:
                        enemyArray.add(new Anonimousse(startX, startY, number, this));
                        break;
                    case 2:
                        enemyArray.add(new Draideux(startX, startY, number, this));
                        break;
                    case 3:
                        enemyArray.add(new FedoraGuy(startX, startY, number, this));
                        break;
                    case 4:
                        enemyArray.add(new Nanarchiste(startX, startY, number, this));
                        break;
                    case 5:
                        enemyArray.add(new Punkh(startX, startY, number, this));
                        break;
                    case 6:
                        enemyArray.add(new StaticObstacle("seringues", startX, startY, this));
                        break;
                    case 7:
                        enemyArray.add(new StaticObstacle("brasero", startX, startY, this));
                        break;
                }

            } else if (world.equals("KKK")) {
                switch (enemyType) {
                    case 1:
                        enemyArray.add(new Didiee(startX, startY, number, this));
                        break;
                    case 2:
                        enemyArray.add(new Fantome(startX, startY, number, this));
                        break;
                    case 3:
                        enemyArray.add(new Hutler(startX, startY, number, this));
                        break;
                    case 4:
                        enemyArray.add(new StaticObstacle("feu-fn-0", startX, startY, this));
                        break;
                    case 5:
                        enemyArray.add(new StaticObstacle("feu-fn-1", startX, startY, this));
                        break;
                    case 6:
                        enemyArray.add(new StaticObstacle("pics-0", startX, startY, this));
                        break;
                    case 7:
                        enemyArray.add(new StaticObstacle("pics-1", startX, startY, this));
                        break;
                }
            } else if (world.equals("Sovietski")) {
                switch (enemyType) {
                    case 1:
                        enemyArray.add(new Moustachu(startX, startY, number, this));
                        break;
                    case 2:
                        enemyArray.add(new ProfRoyaliste(startX, startY, number, this));
                        break;
                    case 3:
                        enemyArray.add(new Russe(startX, startY, number, this));
                        break;
                    case 4:
                        enemyArray.add(new Skieur(startX, startY, number, this));
                        break;
                    case 5:
                        enemyArray.add(new MarteauObstacle(startX, startY, number, this));
                        break;
                    case 6:
                        enemyArray.add(new StaticObstacle("pics-0", startX, startY, this));
                        break;
                    case 7:
                        enemyArray.add(new StaticObstacle("pics-1", startX, startY, this));
                        break;
                    case 8:
                        enemyArray.add(new StaticObstacle("barbeles-0", startX, startY, this));
                        break;
                    case 9:
                        enemyArray.add(new StaticObstacle("barbeles-1", startX, startY, this));
                        break;
                    case 10:
                        enemyArray.add(new Stalactite(startX, startY, number, this));
                        break;
                }
            } else if (world.equals("Mairie")) {
                switch (enemyType) {
                    case 1:
                        enemyArray.add(new Swaggi(startX, startY, number, this));
                        break;
                    case 2:
                        enemyArray.add(new TouittosKeum(startX, startY, number, this));
                        break;
                    case 3:
                        enemyArray.add(new TouittosMeuf(startX, startY, number, this));
                        break;
                    case 4:
                        enemyArray.add(new PouceRouge(startX, startY, number, this));
                        break;
                    case 5:
                        enemyArray.add(new StaticObstacle("cables-electriques-0", startX, startY, this));
                        break;
                    case 6:
                        enemyArray.add(new StaticObstacle("cables-electriques-1", startX, startY, this));
                        break;
                }
            }
        }

        this.enemies = enemyArray;
    }

    /**
     * Load objects (bonuses, coins and triggers) from the tmx map object layer.
     */
    private void loadMapObjects() {
        coins = new Array<>(2048);
        bonuses = new Array<>(512);
        keys = new Array<>(128);
        hints = new Array<>(128);

        // load all objects in the 'objects-layer' of the map
        MapObjects objects = map.getLayers().get("objects").getObjects();

        // create objects
        for (MapObject object : objects) {
            float startX = object.getProperties().get("x", float.class) / tileSize;
            float startY = object.getProperties().get("y", float.class) / tileSize;
            int objectType = object.getProperties().get("gid", int.class);
            String imageName = object.getProperties().get("type", String.class);

            switch (objectType) {
                case 1:
                    // add an invincibility bonus
                    bonuses.add(new InvincibilityBonus(startX, startY, level));
                    break;
                case 2:
                    // add a time bonus
                    bonuses.add(new TimeBonus(startX, startY, level));
                    break;
                case 3:
                    // add a life bonus
                    bonuses.add(new LifeBonus(startX, startY, level));
                    break;
                case 4:
                    // add a coin
                    coins.add(new Coin(startX, startY, level));
                    break;
                case 5:
                    // create win trigger
                    winTrigger = new ExitDoor(startX, startY, level);
                    break;
                case 6:
                    // add a key
                    keys.add(new Key(startX, startY, level));
                    break;
                case 7:
                    // add a hint
                    hints.add(new Hint(startX, startY, level, imageName));
                    break;
            }
        }

        level.setNbOfKeys(keys.size);
        // if there are keys to find, the winTrigger is disabled
        if (winTrigger != null)
            winTrigger.setDisabled(keys.size > 0);
    }

    private void loadBosses() {
        switch (level.getName()) {
            // load bosses if the level is a Boss level
            case "Ville/Boss":
                Marmule marmule = new Marmule(this);
                addActor(marmule);
                break;
            case "Anar/Boss":
                break;
            case "KKK/Boss":
                MarieNougatine marieNougatine = new MarieNougatine(this);
                addActor(marieNougatine);
                break;
            case "Sovietski/Boss":
                break;
            case "Mairie/Boss":
                break;
        }

    }

    public void removePlayer() {
        player.remove();
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public ExitDoor getWinTrigger() {
        return winTrigger;
    }
}
