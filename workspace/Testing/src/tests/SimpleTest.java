package tests;
import game.first.math.*;

public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		float[] a = {0,2};
		float[] b = a;
		b[0] = 5;
		System.out.println(a[0]);

	}

}
