import java.util.*;

public class Grid {
	private int width;
	private int height;
	private static HashMap<int[],Cell> cellPoints = new HashMap<int[],Cell>();
	private int recursionStopper=0;

	public Grid(int width, int height){
		this.width = width;
		this.height = height;
	}

	public HashMap<int[],Cell> getCellPoints(){
		return this.cellPoints;
	}

	public void emptyGrid(){
		this.cellPoints.clear();
	}


	public int getGridWidth(){
		return this.width;
	}

	public int getGridHeight(){
		return this.height;
	}

	public boolean checkOccupied(int x, int y){
			if(this.cellPoints.get(this.getCellIntArray(x,y)) !=null){
				return true;
			}
		return false;
	}

	public void removeCell(Player person, int x ,int y){
		int[] coordinate=null;
		for(int[] cur :cellPoints.keySet()){
			if(cur[0]==x && cur[1]==y){
				coordinate=cur;
			}
		}
		this.cellPoints.remove(coordinate);

	}

	public Cell getCell(int x, int y){
		int[] key= this.getCellIntArray(x,y);
		return this.cellPoints.get(key);
	}

	public int[] getCellIntArray(int x, int y){
		int[] key=null;
		for(int[] cur :cellPoints.keySet()){
			if(cur[0]==x && cur[1]==y){
				key=cur;
			}
		}
		return key;
	}

	public String cellType(int x, int y){
		if (x==0 && y==0){ return "Top-Left-Corner"; }
		if (x==this.width-1 && y==0){ return "Top-Right-Corner"; }
		if (x==0 && y==this.height-1){ return "Bottom-Left-Corner"; }
		if (x==this.width-1 && y==this.height-1){ return "Bottom-Right-Corner"; }

		if (x>0 && x<this.width-1 && y==0){ return "Up-Side"; }
		if (x>0 && x<this.width-1 && y==this.height-1){ return "Down-Side"; }
		if (x==0 && y>0 && y<this.height-1){ return "Left-Side"; }
		if (x==this.width-1 && y>0 && y<this.height-1){ return "Right-Side"; }

		return "Middle";
	}

	public int cellLimit(String type){
		if (type.toLowerCase().indexOf("corner") != -1){ return 2;}
		if (type.toLowerCase().indexOf("side") != -1){ return 3;}
		return 4;
	}

	public void resetStopper(){
		this.recursionStopper=0;
	}
	public int getStopper(){
		return this.recursionStopper;
	}

	public void expand(Player person, PlayerColour colour, int x, int y){

		if(recursionStopper > 1000){
			return;
		}
		else{
		if(this.checkOccupied(x,y)  && !this.pointBelongsToPlayer(person,x,y)){
		// 		//find the cell, change owner to current player and add point to that cell
		// 		//remove from playerpoints LinkedList and within addPoint, add to current player LinkedList
				Cell changeCell = this.getCell(x,y);
				Player oldOwner = changeCell.getOwner();
		//
				changeCell.changeOwner(person,colour);
				// System.out.println("(1,0) cell current owner: "+changeCell.getPlayerColour());
				this.addPoint(person,colour, new int[]{x,y},this);

				// System.out.println("(1,0) occupied state: "+this.checkOccupied(1,0) );
			}
			else{
				// System.out.println("(1,0) occupied state: "+this.checkOccupied(1,0) );
				this.addPoint(person,colour, new int[]{x,y},this);
			}
		}
		recursionStopper+=1;
			// System.out.println(recursionStopper);

	}

	public void allocate(Player person, PlayerColour colour, Cell currentCell){
		int[] coordinates=currentCell.getCoordinate();
		int x = coordinates[0];
		int y = coordinates[1];
		// 	//If occupied,check if it belongs to player, just increment counter. But if not, remove owner cell and in
		// 	//If cell is occupied and does not belong to owner.
		if(currentCell.getCellType().equals("Top-Left-Corner")){
			this.expand(person,colour,1,0);
			this.expand(person,colour,0,1);
			// System.out.println("My recursion is: "+ Integer.toString(recursionStopper));
		}

		if(currentCell.getCellType().equals("Up-Side")){
			this.expand(person,colour,x+1,y);
			this.expand(person,colour,x,y+1);
			this.expand(person,colour,x-1,y);
		}

		if(currentCell.getCellType().equals("Top-Right-Corner")){
			this.expand(person,colour,x,y+1);
			this.expand(person,colour,x-1,y);
		}
		if(currentCell.getCellType().equals("Right-Side")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x,y+1);
			this.expand(person,colour,x-1,y);
		}
		if(currentCell.getCellType().equals("Bottom-Right-Corner")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x-1,y);
		}
		if(currentCell.getCellType().equals("Down-Side")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x+1,y);
			this.expand(person,colour,x-1,y);
		}
		if(currentCell.getCellType().equals("Bottom-Left-Corner")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x+1,y);
		}
		if(currentCell.getCellType().equals("Left-Side")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x+1,y);
			this.expand(person,colour,x,y+1);
		}
		if(currentCell.getCellType().equals("Middle")){
			this.expand(person,colour,x,y-1);
			this.expand(person,colour,x+1,y);
			this.expand(person,colour,x,y+1);
			this.expand(person,colour,x-1,y);
		}


	}



	public void addPoint(Player person, PlayerColour colour, int[] coordinate, Grid curGrid){
		 // occupiedPoints.add(coordinate);
		 int x=coordinate[0];
		 int y=coordinate[1];
		 if(cellPoints.get(this.getCellIntArray(x,y))!=null){
			 //Cell Exists
			 int[] key=this.getCellIntArray(x,y);


				 //Checking if explosion will occur from incrementing cell
			 if(this.pointBelongsToPlayer(person,x,y)){
				 Cell currentCell= cellPoints.get(key);
				 if(currentCell.getAtomNumber()+1 == currentCell.getLimit()){
				 //Do explosion
				 this.removeCell(person,x,y);
				 curGrid.allocate(person,colour,currentCell);


				 // System.out.println("Explosion is about to occur");
				 }
				 else{
					 cellPoints.get(key).incrementAtom();
				 }
			 }

		 }
		else{
		 // System.out.println("Creating new cell for "+x+","+y);
		 // System.out.println("Cell Type is "+ curGrid.cellType(x,y)+". " +curGrid.cellLimit(cellType(x,y)));
		 Cell cur = new Cell(person, colour, coordinate,curGrid.cellType(x,y),curGrid.cellLimit(cellType(x,y)));
		 cur.incrementAtom();
		 cellPoints.put(coordinate,cur);
		}
	}

	public boolean pointBelongsToPlayer(Player person, int x, int y){

		if(this.cellPoints.get(this.getCellIntArray(x,y))!=null && this.cellPoints.get(this.getCellIntArray(x,y)).getOwner()==person){
			return true;
		}


		return false;
	}

//Getters and setters!!
}
