package tests;
import game.first.math.*;

public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Vect3 a = new Vect3(5, 5, 10);
		Vect3 b = new Vect3(3, 5, 18);
		String c = "abs";
		System.out.println(a.hashCode());
		
		System.out.println(b.hashCode());
		
		System.out.println(a.equals(b));
		
		System.out.println(a.equals(c));
		
		
		// TODO Auto-generated method stub

	}

}
