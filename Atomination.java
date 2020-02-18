import java.util.*;
import java.io.*;

public class Atomination {
	private static final String HELP =
		"HELP	displays this help message\n"+
		"QUIT	quits the current game\n\n" +
		"DISPLAY	draws the game board in terminal\n"+
		"START	<number of players> <width> <height> starts the game\n" +
		"PLACE	<x> <y> places an atom in a grid space\n"+
		"UNDO	undoes the last move made\n"+
		"STAT	displays game statistics\n"+
		"SAVE	<filename> saves the state of the game\n"+
		"LOAD	<filename> loads a save file\n";

	private boolean flag = true;
	private boolean hasStarted=false;
	private boolean hasLoadedSuccessfully=false;
	private Grid curGrid;
	private Player currentPlayer;
	private ArrayList<Player> originalPlayers = new ArrayList<Player>();
	private ArrayList<Player> p = new ArrayList<Player>();
	private ArrayList<int[]> moves = new ArrayList<int[]>();

	public void help(){ print(HELP); }

	public void quit(){ print("Bye!"); flag=false;}

	public void display(){
		int width = curGrid.getGridWidth();
		int height = curGrid.getGridHeight();
		System.out.println();
		printInline("+");
		for(int i =0; i< width*3-1;i++){
			printInline("-");
		}
		printInline("+\n");

		for (int i =0; i< height;i++){
			for (int j = 0; j<width; ++j){
				// print(j);
				if(j==0){
				printInline("|");
				}
				int cJ=j;
				int cI=i;
				int[] newPoint = new int[]{cJ,cI};

				if(curGrid.checkOccupied(cJ,cI)){

					Cell myCell = curGrid.getCell(cJ,cI);
					String cellColour = myCell.getPlayerColour().toString();
					char c =cellColour.charAt(0);
					String s=String.valueOf(c);
				    System.out.print(s+ Integer.toString(myCell.getAtomNumber())+"|");
				}
				else{
					System.out.print("  |");
				}
			}
			printInline("\n");
		}
		printInline("+");
		for(int i =0; i< width*3-1;i++){
			printInline("-");
		}
		printInline("+\n\n");
		}

	public void start(int numPlayers, int width, int height){
		this.hasStarted=true;
		curGrid = new Grid(width,height);
		this.getGrid().emptyGrid();
		//Do something with numPlayers
		this.createPlayers(numPlayers);
		print("Game Ready\nRed's Turn\n");
	}

	public void createPlayers(int numPlayers){
		p.clear();
		originalPlayers.clear();
		PlayerColour[] arr = PlayerColour.values();
		this.currentPlayer = new Player(PlayerColour.Red,0);
		originalPlayers.add(currentPlayer);
		p.add(currentPlayer);
		for(int x=1; x<numPlayers;x++){
			Player b = new Player(arr[x],0);
			p.add(b);
			originalPlayers.add(b);
		}
	}

	public boolean place(int x, int y, boolean HaveUndone){
	//Get current player, check if
		int[] curPoint = new int[]{x,y};
		//When checkPoint(int x, int y), do this instead!! Don't instance check!!
		if(curGrid.checkOccupied(x,y)){
			//Yes it is occupied.
			//Does it belong to the current player
			if(curGrid.pointBelongsToPlayer(currentPlayer,x,y)){
				// print("Cell "+x+","+y+" belong to "+ currentPlayer);
				curGrid.addPoint(currentPlayer,currentPlayer.getPlayerColour(),curPoint, curGrid);

				if (!HaveUndone){
					this.moves.add(curPoint);
				}

			}
			else{
				print("Cannot Place Atom Here");
				return false;
			}
		}
		else{
			curGrid.addPoint(currentPlayer,currentPlayer.getPlayerColour(), curPoint, curGrid);

			if(!HaveUndone){
				this.moves.add(curPoint);
			}
		}
		this.curGrid.resetStopper();


		if (p.size()==2){
			if(moves.size()>p.size()){
				if(this.winner()){
					this.removeLosers();
					print(currentPlayer.getPlayerColour() + " Wins!");
					this.flag=false;
					return true;
				}
			}
			currentPlayer= this.switchPlayer();
			if(!HaveUndone){
				print(currentPlayer.getPlayerColour()+"'s Turn\n");
			}
			return true;
		}
		if (p.size()==3){
			if(moves.size()>p.size()){
				this.removeLosers();
				if(this.winner()){
					print(currentPlayer.getPlayerColour() + " Wins!");
					this.flag=false;
					return true;
				}
			}
			currentPlayer= this.switchPlayer();
			if(!HaveUndone){print(currentPlayer.getPlayerColour()+"'s Turn\n");}
			return true;
		}

		if(moves.size()>p.size()){
				this.removeLosers();
				if(this.winner()){
					print(currentPlayer.getPlayerColour() + " Wins!");
					this.flag=false;
					return true;
				}
		}
		currentPlayer= this.switchPlayer();
		if(!HaveUndone){
			print(currentPlayer.getPlayerColour()+"'s Turn\n");
		}
		//Once you place, you must switch the player to next player.
		return true;
		}

	public boolean winner(){
	 	int x=0;
		for(int i=0;i<originalPlayers.size();i++){
			if(originalPlayers.get(i)!=currentPlayer){
				if(originalPlayers.get(i).getGridsOwned(curGrid)==0){
					x+=1;
				}
			}
		}
		if(x==originalPlayers.size()-1){
			return true;
		}
		return false;
	}

	public void removeLosers(){
		ArrayList<Integer> killList = new ArrayList<Integer>();
		for(int i=0;i<p.size();i++){
			if(p.get(i)!=currentPlayer){
				if(p.get(i).getGridsOwned(curGrid)==0){
					killList.add(i);
				}
			}
		}
		Collections.reverse(killList);

		for (int i: killList){
			p.remove(i);
		}
		killList.clear();
	}

	public Player switchPlayer(){
		int x=-99;
		for(int i=0;i<p.size();i++){
			if (p.get(i)==currentPlayer){
				if(i+1 == p.size()){
					x=0;
				}
				else{
					x=i+1;
				}
			}
		}
		return p.get(x);
	}

	public void restartGrid(int width,int height){
			curGrid.emptyGrid();
			curGrid = new Grid(width,height);
	}

	public void replay(int num){
		this.currentPlayer=p.get(0);
		for (int i=0; i<num; i++ ){
			int[] coord = moves.get(i);
			this.place(coord[0],coord[1],true);
		}

	}

	public void undo(){
		if(moves.size()>0){
			this.restartGrid(curGrid.getGridWidth(),curGrid.getGridHeight());
			this.replay(moves.size()-1);
			moves.remove(moves.size()-1);
			print(currentPlayer.getPlayerColour()+"'s Turn\n");
		}
		else{
			print("Cannot Undo");
		}
	}

	public void stat(){
		if(this.hasStarted == false){
			print("Game Not In Progress");
		}
		else{
			PlayerColour[] arr = PlayerColour.values();
			for (int i=0; i<originalPlayers.size();i++){
				print("Player " + arr[i]+":" );
				if(originalPlayers.get(i).getGridsOwned(curGrid)==0 && moves.size()>originalPlayers.size()){
					print("Lost\n");
				}else{
				print("Grid Count: "+ originalPlayers.get(i).getGridsOwned(curGrid)+"\n");
				}
			}
		}

		}

	public void save(String filename){
		File file = new File("./"+ filename);
		if(!file.isFile()){
		try{
			DataOutputStream stream = new DataOutputStream(new FileOutputStream("./"+filename));
			stream.writeByte(curGrid.getGridWidth());
			stream.writeByte(curGrid.getGridHeight());
			stream.writeByte(p.size());
			for (int x=0;x<moves.size();x++){
				stream.writeByte(moves.get(x)[0]);
				stream.writeByte(moves.get(x)[1]);
				stream.writeByte(0);
				stream.writeByte(0);
			}
			stream.close();
			print("Game Saved\n");
		}

		catch(IOException e){
			print("Failed to save Game");
		}
		}
		else{
			print("File Already Exists");
		}

	}

	public void load(String filename){
		File f = new File("./"+filename);
		if (!f.isFile()){
			print("Cannot Load Save\n");
			return;
		}
		else if(this.hasStarted || this.hasLoadedSuccessfully){
			print("Restart Application To Load Save\n");
			return;
		}
		else{
			try{
				FileInputStream stream = new FileInputStream("./"+filename);
				int width = stream.read();
				int height= stream.read();
				curGrid=new Grid(width,height);

				int no_players= stream.read();
				this.createPlayers(no_players);
				while (stream.available() > 0) {
					int x = stream.read();
					int y = stream.read();
					stream.read();
					stream.read();
					int[] cur = new int[]{x,y};
					this.moves.add(cur);
				}
				this.replay(moves.size());
			this.hasLoadedSuccessfully=true;
			this.hasStarted=true;
			print("Game Loaded\n");
			}
			catch(Exception e) {
				print("Unsucessfully load");
			}
		}
	}

	public static void print(Object item){
		System.out.println(item);
	}
	public static void printInline(Object item){
		System.out.print(item);
	}

	public boolean checkBad(String str){
		if(str.matches("[0-9]+") && str.length()>0){
			if (Integer.parseInt(str)<2){
				return true;
			}else{
				return false;
			}

		}
		return true;
	}

	public boolean checkInt(String str){
		if(str.matches("-?[0-9]+") && str.length()>0){
			return true;
		}
		return false;
	}

	public boolean getFlag(){
		return this.flag;
	}

	public boolean getHasStarted(){
		return this.hasStarted;
	}

	public boolean getHasLoadedSuccessfully(){
		return this.hasLoadedSuccessfully;
	}

	public Grid getGrid(){
		return this.curGrid;
	}
	public Player getCurrentPlayer(){
		return this.currentPlayer;
	}

	public ArrayList<Player> getOriginalPlayers(){
		return this.originalPlayers;
	}

	public ArrayList<Player> getP(){
		return this.p;
	}

	public ArrayList<int[]> getMoves(){
		return this.moves;
	}

	public static void main(String[] args) {
		//Your game starts here!
		//HashMap for get keys
		Atomination game = new Atomination();
		HashMap<String, ExcecuteCommands> cmdNoArgs = new HashMap<String, ExcecuteCommands>();
		cmdNoArgs.put("help", ()-> game.help());
		cmdNoArgs.put("quit",()->game.quit());
		cmdNoArgs.put("display",()->game.display());
		cmdNoArgs.put("undo",()->game.undo());
		cmdNoArgs.put("stat",()->game.stat());

		HashMap<String, SaveLoad > cmd = new HashMap<String, SaveLoad>();
		cmd.put("save",(String str)->game.save(str));
		cmd.put("load",(String str)->game.load(str));

		Scanner keyboard = new Scanner(System.in);

		while (game.flag){
				String str = keyboard.nextLine();
				String clean =str.toLowerCase().trim();
				// print(clean);
				if(clean.contains(" ")){

					String[] items = clean.split("\\s+");

					for (int i = 0; i < items.length; i++){
                     items[i] = items[i].trim();
					}
					if (items[0].equals("start")){
						if (game.hasStarted==true){
							print("Invalid Command");
							continue;
						}
						if(items.length>4){
							print("Too many Arguments");
							continue;
						}
						if(items.length<4){
							print("Missing Argument");
							continue;
						}
						if(game.checkBad(items[1]) || game.checkBad(items[2]) || game.checkBad(items[3])){
							print("Invalid command arguments");
							continue;
						}

						if(Integer.parseInt(items[1])<=4 && Integer.parseInt(items[2])<=255 && Integer.parseInt(items[3])<=255){
							game.start(Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
						}
						else{
							print("Put in a valid number!!");
						}
						//Need else in case they put some huge width or height


					}
					else if(items[0].equals("place") && game.hasStarted){
						//Check if integers
						if(items.length!=3){
							print("Invalid Coordinates");
						}

						else if(game.checkInt(items[1]) && game.checkInt(items[2])){

								int x = Integer.parseInt(items[1]);
								int y = Integer.parseInt(items[2]);
								if(x<0 || x>= game.curGrid.getGridWidth() || y <0 || y >= game.curGrid.getGridHeight()){
								print("Invalid Coordinates");

																}
								else{game.place(x,y,false);}
						}
					}
					else if(items[0].equals("save") && game.hasStarted){
						if(items.length==2){
							cmd.get("save").apply(items[1]);
						}
					}
					else if(items[0].equals("load")){
						if(items.length==2){
							cmd.get("load").apply(items[1]);
						}
					}
				}
				else if(cmdNoArgs.containsKey(clean)){
						cmdNoArgs.get(clean).apply();
				}

		}
	}
}

interface ExcecuteCommands{
		void apply();
	}
// interface Moves{
// 	void replay(int x, int y);
// }

interface SaveLoad{
		void apply(String filename);
	}
