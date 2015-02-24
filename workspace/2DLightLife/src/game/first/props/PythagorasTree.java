package game.first.props;

import java.util.ArrayList;

public class PythagorasTree extends Shape{
	
	

	public PythagorasTree(float x, float y, float z, float size) {
		super(x, y, z);
		double downScale = Math.sqrt(2)/2;
		ArrayList<Float> list = new ArrayList<Float>();
	}
	
	private static void createVertices(ArrayList<Float> list, float size, double downScale, int iteration, float x, float y, float angle) {
		
		
		if (iteration == 10) {
			
		}
		
		
		
	}

	@Override
	public void draw(float[] mvpMatrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getShaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
