package game.first.mechanics;

import game.first.world.World;

/**
 * Created by Anton Lundgren on 2016-05-25.
 */
public class LevelFadeIn implements Runnable {

    @Override
    public void run() {
        while (World.LIGHT_STRENGTH_MULTIPLIER < 1) {
            World.LIGHT_STRENGTH_MULTIPLIER += 0.015f;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        World.LIGHT_STRENGTH_MULTIPLIER = 1f;
    }
}
