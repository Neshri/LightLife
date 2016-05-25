package gui;

import android.os.Message;
import android.util.Log;

import java.util.List;

import game.first.lighting.PointLight;
import game.first.lightlife.ActivityTasks;
import game.first.lightlife.PlayActivity;
import game.first.world.World;

/**
 * Created by Anton Lundgren on 2015-11-30.
 */
public class LevelSuccess implements Runnable{

    private PlayActivity act;

    public LevelSuccess(PlayActivity act) {
        this.act = act;
    }

    private void fadeOutCurrent() {
        while (World.LIGHT_STRENGTH_MULTIPLIER - 0.015f > 0) {
            World.LIGHT_STRENGTH_MULTIPLIER -= 0.015f;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        World.LIGHT_STRENGTH_MULTIPLIER = 0;
    }

    private void fadeInNext() {
        act.assignTask(ActivityTasks.START_NEXT_LEVEL);
    }


    @Override
    public void run() {
        fadeOutCurrent();
        fadeInNext();
    }
}
