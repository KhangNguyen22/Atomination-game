import java.util.*;
public class Player {
	private PlayerColour colour;
	private int gridsOwned;
	
	public Player(PlayerColour colour,int gridsOwned){
		this.colour=colour;
		this.gridsOwned=gridsOwned;
	}
	
	public int getGridsOwned(Grid currentGrid){
		gridsOwned=0;
		HashMap<int[],Cell> map = currentGrid.getCellPoints();
	  	for(int[] arr:map.keySet()){
			if(map.get(arr).getPlayerColour()== this.colour){
				this.gridsOwned += 1;
			}
		}
		return this.gridsOwned;
	}

	public PlayerColour getPlayerColour(){
		return this.colour;
	}
	
}
