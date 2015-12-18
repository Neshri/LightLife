package gui;

import java.util.List;

import game.first.lighting.PointLight;
import game.first.lightlife.PlayActivity;

/**
 * Created by Anton Lundgren on 2015-11-30.
 */
public class LevelSuccess {

    private PlayActivity act;
    private List<PointLight> lights;

    public LevelSuccess(PlayActivity act, List<PointLight> lights) {
        this.act = act;
        this.lights = lights;
    }

    public void fadeOutCurrent() {
        float mult = 0.99f;
        while (mult > 0) {
            for (PointLight a : lights) {
                float tmp = a.getStrength();
                a.setStrength(tmp * mult);
            }
            mult -= 0.01f;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void fadeInNext() {
        act.startNextLevel();
    }
}
