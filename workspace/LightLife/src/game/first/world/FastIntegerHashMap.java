package game.first.world;

public class FastIntegerHashMap {

	
	private int size;
	private Cell[] cellList;
	
	public FastIntegerHashMap(int size) {
		this.size = size;
		cellList = new Cell[size];
	}
	
	public void add(int add) {
		int a = add % size;
		if (cellList[a] == null) {
			cellList[a] = new Cell(add);
		} else {
			cellList[a].setNext(new Cell(add));
		}
		
		
	}
	
	public boolean contains(int check) {
		int c = check % size;
		return cellList[c].contains(check);
	}
	
	private class Cell {
		private int value;
		private Cell nextCell;
		public Cell(int value) {
			this.value = value;
		}
		
		public void setNext(Cell nextCell) {
			this.nextCell = nextCell;
		}
		
		public boolean contains(int check) {
			if (value == check) {
				return true;
			} else if (nextCell != null) {
				return nextCell.contains(check);
			} else {
				return false;
			}	
		}
	}
	
	

}
