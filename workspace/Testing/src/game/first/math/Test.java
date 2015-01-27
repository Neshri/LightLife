package game.first.math;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vect3 a = new Vect3(1f,0f,0f);
		a.rotateY(90);
	System.out.println(a);
//		System.out.println((float)-1f* Math.sin(Math.toRadians(90f))+ (float) Math.cos(Math.toRadians(90f)*0));
	}

}
