package com.muchpolitik.lejeu.GameActors.Enemies.Bosses;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.muchpolitik.lejeu.AI.BossStates.BossState;
import com.muchpolitik.lejeu.AI.BossStates.MarieNougatinePhases;
import com.muchpolitik.lejeu.GameActors.Enemies.Enemy;
import com.muchpolitik.lejeu.GameActors.Enemies.Ghosts.Fantome;
import com.muchpolitik.lejeu.GameActors.Enemies.ProjectileThrowers.Hutler;
import com.muchpolitik.lejeu.Stages.GameStage;


/**
 * Boss of the KKK world.
 */

public class MarieNougatine extends Boss {

    public final float TIME_BETWEEN_ATTACKS = 4;

    public StateMachine<MarieNougatine, MarieNougatinePhases> phasesStateMachine;

    /**
     * Tracks free/occupied platforms by storing their index.
     */
    private Array<Integer> freePlatforms, occupiedPlatforms;
    private Array<Vector2> bossPlatforms, minionPlatforms;
    private Array<Float> minionsRange;
    /**
     * Tracks minions, ordered by the index of their platform.
     */
    private Array<Enemy> minions;


    public MarieNougatine(GameStage gameStage){
        // the position and range don't matter as position is set up later and the boss doesn't walk
        super(1, 1, 0, gameStage);

        // set custom variables
        defenseType = DefenseType.Tank;
        lives = 6; // the player has to hit 6 times the boss in order to win
        deathTime = 4;
        // /!\ has to be > than 2 * atkDelay, so all ATK messages are received in STUNNED state and don't interfere once the boss reenter ATK state
        stunnedTime = 1.5f;
        timeBetweenAttacks = TIME_BETWEEN_ATTACKS;

        setUpPlatforms();

        // load graphics and state machines
        // rem : Marie Nougatine doesn't walk so her 'walking' animations are actually 'stay put' animations
        //TODO: add marie nougatine graphics
        loadAnimations(gameStage.getLevel().getGameObjectsAtlas(), "marmule", true);
        loadStunnedAnimations(gameStage.getLevel().getGameObjectsAtlas(), "marmule");
        facingRight = MathUtils.randomBoolean();
        createStateMachines();

        // set hitbox
        setSize(1.5f, 1.5f);
        hitboxWidth = getWidth();
        bounds = new Rectangle(getX(), getY(), hitboxWidth, getHeight());

        // add a health bar (does not work atm)
        //healthBar = new HealthBar(this);
        //gameStage.addActor(healthBar);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // check if there are dead minions
        for (int i=0 ; i < occupiedPlatforms.size ; i++) {
            int platformIndex = occupiedPlatforms.get(i);

            if (!minions.get(platformIndex).hasParent()) {
                // if a minion on an occupied platform is dead, mark the platform as free
                occupiedPlatforms.removeIndex(i);
                freePlatforms.add(platformIndex);

                // if enough minions are killed
                if (occupiedPlatforms.size == 3)
                    phasesStateMachine.changeState(MarieNougatinePhases.PHASE_1);
            }
        }
    }

    /**
     * Marie Nougatine does not move
     */
    @Override
    public void moveWithinRangeOfAction() {

    }

    @Override
    public void updateLivingStateMachine() {
        livingStateMachine.update();
    }

    @Override
    public boolean handleLivingStateMachineMessage(Telegram telegram) {
        return livingStateMachine.handleMessage(telegram);
    }

    @Override
    public void createStateMachines() {
        super.createStateMachines();

        // create livingStateMachine specific to projectile throwers
        livingStateMachine = new DefaultStateMachine<Boss, BossState>(this);

        // create phasesStateMachine
        phasesStateMachine = new DefaultStateMachine<>(this);
        phasesStateMachine.changeState(MarieNougatinePhases.PHASE_1);
    }

    @Override
    public void turnAround() {
        switch (livingStateMachine.getCurrentState()) {
            case WALK:
                facingRight = !facingRight;
                currentAnimation = facingRight? walkRightAnimation : walkLeftAnimation;
                break;

            case ATTACK:
                facingRight = !facingRight;
                currentAnimation = facingRight? attackingRightAnimation : attackingLeftAnimation;
                break;
        }
    }

    /**
     * Lose a life, get stunned and check if the phase is finished (if the boss has lost enough lives)
     */
    @Override
    public void loseOneLife() {
        // change phase every 2 hits
        if (lives % 2 == 0) {
            switch (phasesStateMachine.getCurrentState()) {
                case PHASE_1:
                    phasesStateMachine.changeState(MarieNougatinePhases.PHASE_2);
                    break;

                case PHASE_2:
                    phasesStateMachine.changeState(MarieNougatinePhases.PHASE_1);
                    break;
            }
            // lose one life without getting stunned, as the boss is going into HIDING state
            lives -= 1;
        } else {
            // get stunned regularly
            super.loseOneLife();
        }
    }

    /**
     * Create the lists of platforms (coordinates and index) where boss and minions can spawn.
     */
    private void setUpPlatforms() {
        // array to keep track of the platforms (by index)
        freePlatforms = new Array<>(6);
        for (int i=0; i<6; i++)
            freePlatforms.add(i);
        occupiedPlatforms = new Array<>(6);

        // array to keep track of the minions, has to contain 6 elements
        minions = new Array<>(6);
        for (int i=0; i<6; i++) {
            minions.add(new Hutler(0, 0, 0, gameStage));
        }

        // the coordinates of boss spots, in the order
        bossPlatforms = new Array<>(6);
        bossPlatforms.add(new Vector2(4, 1));
        bossPlatforms.add(new Vector2(3, 4));
        bossPlatforms.add(new Vector2(2, 8));
        bossPlatforms.add(new Vector2(14, 1));
        bossPlatforms.add(new Vector2(15, 4));
        bossPlatforms.add(new Vector2(16, 8));

        // the coordinates of minions spots, in the order
        minionPlatforms = new Array<>(6);
        minionPlatforms.add(new Vector2(4, 1));
        minionPlatforms.add(new Vector2(2.5f, 4));
        minionPlatforms.add(new Vector2(1, 8));
        minionPlatforms.add(new Vector2(14, 1));
        minionPlatforms.add(new Vector2(16, 4));
        minionPlatforms.add(new Vector2(16.5f, 8));

        // the range of minions in each spot
        minionsRange = new Array<>(6);
        minionsRange.add(2f);
        minionsRange.add(0.5f);
        minionsRange.add(1f);
        minionsRange.add(2f);
        minionsRange.add(1f);
        minionsRange.add(0.5f);
    }

    /**
     * @return the index of a random platform not occupied, and -1 if there is no free platform
     */
    public int getRandomFreePlatform() {
        if (freePlatforms.size > 0)
            return freePlatforms.random();
        else
            return -1;
    }

    /**
     * Move the boss to a new platform. There is no need to move the platform to the occupied list,
     * as the boss disappears anyway when minions are spawned.
     * @param i index of the new platform
     */
    public void changePlatform(int i) {
        Vector2 newPosition = bossPlatforms.get(i);
        setPosition(newPosition.x, newPosition.y);
    }

    /**
     * Spawn minions in the free spots.
     */
    public void spawnMinions() {

        while (freePlatforms.size > 0) {
            // for each free platform, get its coordinates and the range corresponding
            int platformIndex = freePlatforms.get(0);
            Vector2 platformPosition = minionPlatforms.get(platformIndex);
            float range = minionsRange.get(platformIndex);

            // spawn randomly a minion
            if (MathUtils.randomBoolean())
                minions.set(platformIndex, new Hutler(platformPosition.x, platformPosition.y, range, gameStage));
            else
                minions.set(platformIndex, new  Fantome(platformPosition.x, platformPosition.y, range, gameStage));
            gameStage.addActor(minions.get(platformIndex));

            // mark the platform as occupied
            freePlatforms.removeIndex(0);
            occupiedPlatforms.add(platformIndex);
        }

    }

    @Override
    public boolean handlePhasesStateMachine(Telegram telegram) {
        return phasesStateMachine.handleMessage(telegram);
    }
}
