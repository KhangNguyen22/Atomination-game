
public class Cell{
	private int[] coordinate;
	private int atomNumber=0;
	private Player cur;
	private PlayerColour colour;
	private final String cellType;
	private final int limit;
	
	public Cell(Player person, PlayerColour personColour, int[] coordinate, String cellType,int limit){
		this.cur=person;
		this.colour = personColour;
		this.coordinate = coordinate;
		this.cellType = cellType;
		this.limit = limit;
	}
	
	public int[] getCoordinate(){
		return this.coordinate;
	}
	
	public boolean isCellEmpty(){
		if (atomNumber==0){
			return true;
		}
		return false;
	}
	
	
	public void incrementAtom(){
		this.atomNumber+=1;
	}
	public PlayerColour getPlayerColour(){
		return this.colour;
	}
	
	public Player getOwner(){
		return this.cur;
	}
	
	public void changeOwner(Player person, PlayerColour colour){
		this.cur=person;
		this.colour=colour;
	}
	
	public int getAtomNumber(){
		return this.atomNumber;
	}
	
	public String getCellType(){
		return this.cellType;
	}
	
	public int getLimit(){
		return this.limit;
	}
}
