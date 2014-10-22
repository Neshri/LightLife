//package game.first.lightlife;
//
//import android.graphics.Color;
//
//public class CellCalculator implements Runnable {
//
//	private boolean leftSide;
//	private int halfArray;
//
//	public CellCalculator(boolean leftSide) {
//		this.leftSide = leftSide;
//		halfArray = FrameCreator.pixels.length / 2;
//
//	}
//
//	public void calculate() {
//		if (leftSide) {
//			for (int i = 0; i < halfArray; i++) {
//				FrameCreator.pixels[i] = Color.WHITE;
//
//			}
//			// Do Stuff
//			//sendFrame();
//		} else {
//			for (int i = halfArray; i < halfArray * 2; i++) {
//				FrameCreator.pixels[i] = Color.BLACK;
//
//			}
//			// Do Stuff
//			//sendFrame();
//		}
//	}
//
//	@Override
//	public void run() {
//		calculate();
//	}
//}
