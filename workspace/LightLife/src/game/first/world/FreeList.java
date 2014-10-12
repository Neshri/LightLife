package game.first.world;

public class FreeList {

	private int nextCell, size, type;
	private int[] freeList;
	private World world;
	
	/**
	 * Creates a list of ID values that are free to use, type = 0||1 where 0=static and 1=dynamic
	 * @param size
	 */
	public FreeList(int size, World world, int type) {
		this.type = type;
		this.world = world;
		freeList = new int[size];
		this.size = size;
		nextCell = 0;
		for (int i = 0; i < size; i++) {
			freeList[i] = i+1;
		}
	}
	
	/**
	 * Returns a non-used ID
	 * @return FreeID
	 */
	public int getFreeId() {
		if (nextCell == size) {
			int[] newFree = new int[size * 2];
			for (int i = size; i < size * 2; i++) {
				newFree[i] = i;
			}
			if (type == 0) {
				world.increaseStatic();
			} else {
				world.increaseDynamic();
			}
			size = size * 2;
		}
		int send = freeList[nextCell];
		freeList[nextCell] = 0;
		nextCell++;
		return send;
	}
	
	/**
	 * Stores an ID that's no longer in use for future usage
	 * @param id
	 */
	public void storeId(int id) {
		if (nextCell == 0) {
			return;
		}
		nextCell--;
		freeList[nextCell] = id;
	}
	
	/**
	 * 
	 * @return true if there are no non-used IDs left
	 * 	else false
	 */
	public boolean isEmpty() {
		if (nextCell == size) {
			return true;
		}
		return false;
	}
}
