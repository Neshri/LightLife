package game.first.lightlife;

import game.first.mechanics.Objective;
import game.first.pawn.Controller;
import game.first.pawn.Player;
import game.first.world.World;

public class GameLoop extends Thread {
	
	private volatile boolean shouldPause;
	private volatile Object lock = new Object();
	private PlayView view;
	private World world;
	private Controller control;
	private float[] controlVals;
	private Objective objective;
	private long sleepTime;
	
	public GameLoop(PlayView view, Player player) {
		this.view = view;
		objective = player.getLevel().getObjective();
		shouldPause = false;
		world = player.getWorld();
		control = player.getController();
	}
	
	public void onPause() {
		shouldPause = true;
	}
	
	public void onResume() {
		shouldPause = false;
		synchronized (lock) {
			lock.notify();
		}
	}
	
	
	
	@Override
	public void run() {
		while(true) {
			sleepTime = System.currentTimeMillis();
			world.step();
			controlVals = view.controlVals;
			control.update(controlVals[0], controlVals[1], controlVals[2], controlVals[3]);
			if (objective != null && objective.isCompleted()) {				
				view.objectiveSuccess();
			}
			sleepTime = 10 - (System.currentTimeMillis() - sleepTime);
			if (sleepTime < 0) {
				sleepTime = 0;
			}
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (shouldPause) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
