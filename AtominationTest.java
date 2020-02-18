import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class AtominationTest {


	@Test
	public void testConstruction() {
		Atomination game = new Atomination();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		game.help();

		String help=
		"HELP	displays this help message\n"+
		"QUIT	quits the current game\n\n" +
		"DISPLAY	draws the game board in terminal\n"+
		"START	<number of players> <width> <height> starts the game\n" +
		"PLACE	<x> <y> places an atom in a grid space\n"+
		"UNDO	undoes the last move made\n"+
		"STAT	displays game statistics\n"+
		"SAVE	<filename> saves the state of the game\n"+
		"LOAD	<filename> loads a save file\n\n";


		assertEquals(help, outContent.toString());
		game.quit();
	}

	@Test
	public void testQuit(){

		Atomination game = new Atomination();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		game.quit();
		String expected="Bye!\n";
		assertEquals(expected, outContent.toString());
		assertFalse(game.getFlag());
		game.quit();
	}


	@Test
	public void testDefault(){
		Atomination game = new Atomination();
		assertTrue(game.getFlag());
		assertFalse(game.getHasStarted());
		assertFalse(game.getHasLoadedSuccessfully());
		assertNull(game.getGrid());
		assertNull(game.getCurrentPlayer());
		assertEquals(0,game.getOriginalPlayers().size());
		assertEquals(0,game.getP().size());
		assertEquals(0,game.getMoves().size());
		game.quit();
	}

	@Test
	public void testStartAndCreatePlayersMin(){
		Atomination game = new Atomination();
		game.start(2,2,2);
		assertEquals(2,game.getGrid().getGridWidth());
		assertEquals(2,game.getGrid().getGridHeight());
		assertEquals(2,game.getOriginalPlayers().size());
		assertEquals(2,game.getP().size());
		game.quit();
	}


	@Test
	public void testStartAndCreatePlayersMax(){
		Atomination game = new Atomination();
		game.start(4,255,255);
		assertEquals(255,game.getGrid().getGridWidth());
		assertEquals(255,game.getGrid().getGridHeight());
		assertEquals(4,game.getOriginalPlayers().size());
		assertEquals(4,game.getP().size());
		game.quit();
	}



	@Test
	public void testPlaceDisplay2Win(){
		Atomination game = new Atomination();
		game.start(2,3,3);
		assertTrue(game.place(0,0,false));
		assertEquals(1,game.getMoves().size());
		assertEquals(1,game.getGrid().getCellPoints().size());


		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		game.display();

		String actual=outContent.toString();
		String expected="\n"+"+--------+\n"+"|R1|  |  |\n"+"|  |  |  |\n"+"|  |  |  |\n"+"+--------+\n\n";
		assertEquals(expected, actual);


		assertFalse(game.place(0,0,false));
		assertTrue(game.place(0,1,false));
		//Test point belongs to player
		assertTrue(game.place(0,0,false));
		assertEquals(3,game.getMoves().size());
		assertEquals(2,game.getGrid().getCellPoints().size());

		//Player Red wins so set flag to false!!
		assertFalse(game.getFlag());
		game.quit();

	}



	@Test
	public void testPlaceOccupied3Win(){
		Atomination game = new Atomination();
		game.start(3,3,3);
		//Red,Green,Purple
		assertTrue(game.place(0,0,false));
		assertFalse(game.place(0,0,false));
		assertTrue(game.place(1,0,false));
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,0,false));
		//Green dies
		assertEquals(2,game.getP().size());
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,1,false));
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,1,false));
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,1,false));
		//Red wins!!
		assertFalse(game.getFlag());

		game.quit();
	}

	@Test
	public void testPlace4Win(){
		Atomination game = new Atomination();
		game.start(4,3,3);

		assertTrue(game.place(0,0,false));
		assertTrue(game.place(0,1,false));
		assertTrue(game.place(1,0,false));
		assertTrue(game.place(1,2,false));


		assertTrue(game.place(0,0,false));
		//two players left!
		assertEquals(2,game.getP().size());
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,2,false));
		assertTrue(game.place(1,1,false));

		assertTrue(game.place(0,2,false));

		//Red wins!!
		assertFalse(game.getFlag());
		game.quit();
	}

	@Test
	public void testWinnerRemoveLosers(){
		Atomination game = new Atomination();
		game.start(2,3,3);

		assertTrue(game.place(0,0,false));
		assertTrue(game.place(0,1,false));
		assertFalse(game.winner());

		assertTrue(game.place(0,0,false));
		assertEquals(1,game.getP().size());

		assertTrue(game.winner());
		game.quit();
	}

	@Test
	public void testSwitchPlayers(){
		Atomination game = new Atomination();
		game.start(4,3,3);
		PlayerColour cur=PlayerColour.Red;
		assertEquals(cur,game.getCurrentPlayer().getPlayerColour());
		game.place(0,0,false);
		cur=PlayerColour.Green;
		assertEquals(cur,game.getCurrentPlayer().getPlayerColour());

		game.place(0,1,false);
		cur=PlayerColour.Purple;
		assertEquals(cur,game.getCurrentPlayer().getPlayerColour());

		game.place(1,0,false);
		cur=PlayerColour.Blue;
		assertEquals(cur,game.getCurrentPlayer().getPlayerColour());


		game.place(1,1,false);
		cur=PlayerColour.Red;
		assertEquals(cur,game.getCurrentPlayer().getPlayerColour());
		game.quit();

	}

	@Test
	public void testRestartGrid(){

		Atomination game = new Atomination();
		game.start(4,3,3);
		game.restartGrid(10,10);
		assertEquals(10,game.getGrid().getGridWidth());
		assertEquals(10,game.getGrid().getGridHeight());
		game.quit();
	}


	@Test
	public void testReplay(){
		Atomination game = new Atomination();
		game.start(4,3,3);
		game.place(0,0,false);
		game.restartGrid(10,10);
		assertEquals(0,game.getGrid().getCellPoints().size());
		game.replay(game.getMoves().size());
		assertEquals(1,game.getGrid().getCellPoints().size());
		game.quit();
	}

	@Test
	public void testUndo(){
		Atomination game = new Atomination();
		game.start(4,3,3);
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		game.undo();

		assertEquals("Cannot Undo\n",outContent.toString());


		game.place(0,0,false);

		game.place(1,1,false);
		assertEquals(2,game.getMoves().size());
		game.undo();
		assertEquals(1,game.getMoves().size());
		game.quit();
	}


	@Test
	public void testStat(){
		Atomination game = new Atomination();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		game.stat();
		String e1="Game Not In Progress\n";
		assertEquals(e1,outContent.toString());

		game.start(2,3,3);

		game.stat();

		String e2=e1+"Game Ready\n"+"Red's Turn\n\n"+ "Player Red:\n"+"Grid Count: 0\n\n"+"Player Green:\n"+"Grid Count: 0\n\n";

		assertEquals(e2,outContent.toString());

		game.place(0,0,false);
		game.place(0,1,false);
		game.place(0,0,false);
		game.stat();

		//Check lost if statement
		String e3=e2+"Green's Turn\n\n"+"Red's Turn\n\n"+"Red Wins!\n"+ "Player Red:\n"+"Grid Count: 2\n\n"+"Player Green:\n"+"Lost\n\n";

		assertEquals(e3,outContent.toString());

		game.quit();
	}


	@Test
	public void testSave(){
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		File f = new File("./"+"test.dat");

		if(f.exists()){
			f.delete();
		}

		Atomination game = new Atomination();
		game.start(4,3,3);
		game.place(0,0,false);
		game.place(1,1,false);
		game.save("///");

		String e="Game Ready\n"+"Red's Turn\n\n"+"Green's Turn\n\n"+"Purple's Turn\n\n"+ "Failed to save Game\n";

		assertEquals(e,outContent.toString());

		game.save("test.dat");
		String e1=e+ "Game Saved\n\n";

		assertEquals(e1,outContent.toString());

		String e2=e1+"File Already Exists\n";
		game.save("test.dat");
		assertEquals(e2,outContent.toString());
		game.quit();

	}

	@Test
	public void testLoad(){
		Atomination game = new Atomination();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		game.load("Random");
		String e="Cannot Load Save\n\n";
		assertEquals(e,outContent.toString());

		game.load("dodgy.d");
		String k=e+"Unsucessfully load\n";
		assertEquals(k,outContent.toString());

		game.load("test.dat");
		String e1= k+"Game Loaded\n\n";
		assertEquals(e1,outContent.toString());
		assertEquals(2,game.getMoves().size());

		game.load("test.dat");
		String e2= e1+"Restart Application To Load Save\n\n";
		assertEquals(e2,outContent.toString());
		assertEquals(2,game.getMoves().size());
		game.quit();
	}

	@Test
	public void testPrintAndPrintInline(){
		Atomination game = new Atomination();
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		game.print("Cat");
		String e1= "Cat\n";
		assertEquals(e1,outContent.toString());

		game.printInline("Dogs");
		String e2=e1+"Dogs";
		assertEquals(e2,outContent.toString());

		game.quit();
	}

	@Test
	public void testCheckBad(){
		Atomination game = new Atomination();
		assertTrue(game.checkBad("1"));
		assertFalse(game.checkBad("2"));
		assertTrue(game.checkBad("-1"));

		game.quit();
	}

	@Test
	public void testCheckInt(){
		Atomination game = new Atomination();
		assertTrue(game.checkInt("1"));
		assertTrue(game.checkInt("2"));
		assertTrue(game.checkInt("-1"));
		assertFalse(game.checkInt("1.2"));
		assertFalse(game.checkInt("-1.3333"));
		game.quit();
	}


	@Test
	public void testCell(){
		Player tom = new Player(PlayerColour.Red,0);
		int[] i = new int[]{3,4};
		Cell cur = new Cell(tom,PlayerColour.Red,i,"Middle",4);

		assertEquals(i,cur.getCoordinate());
		assertTrue(cur.isCellEmpty());
		cur.incrementAtom();
		assertFalse(cur.isCellEmpty());
		assertEquals(PlayerColour.Red,cur.getPlayerColour());
		assertEquals(tom,cur.getOwner());
		Player james = new Player(PlayerColour.Blue,0);
		cur.changeOwner(james,PlayerColour.Blue);
		assertEquals(james,cur.getOwner());
		assertEquals(1,cur.getAtomNumber());
		assertEquals("Middle",cur.getCellType());
		assertEquals(4,cur.getLimit());
	}

	@Test
	public void testPlayer(){
		Player p1 = new Player(PlayerColour.Red,0);
		Grid cur = new Grid(10,10);
		assertEquals(0,p1.getGridsOwned(cur));
		assertEquals(PlayerColour.Red,p1.getPlayerColour());
		int[] i = new int[]{2,2};
		cur.addPoint(p1,p1.getPlayerColour(),i,cur);
		assertEquals(1,p1.getGridsOwned(cur));
	}

	@Test
	public void testGrid(){
		Player p1 = new Player(PlayerColour.Red,0);
		Grid cur = new Grid(4,6);
		cur.emptyGrid();
		assertEquals(0,cur.getCellPoints().size());
		assertEquals(4,cur.getGridWidth());
		assertEquals(6,cur.getGridHeight());
		assertFalse(cur.checkOccupied(2,2));
		int[] i = new int[]{2,2};
		cur.addPoint(p1,p1.getPlayerColour(),i,cur);
		assertTrue(cur.checkOccupied(2,2));

		cur.removeCell(p1,2,2);
		assertFalse(cur.checkOccupied(2,2));

		cur.addPoint(p1,p1.getPlayerColour(),i,cur);
		Cell b = cur.getCell(2,2);
		assertEquals( b,cur.getCell(2,2));
		assertEquals( i,cur.getCellIntArray(2,2));

		assertTrue(cur.pointBelongsToPlayer(p1,2,2));

		int[] c = new int[]{3,3};

		assertFalse(cur.pointBelongsToPlayer(p1,3,3));

		//CellType Test
		assertEquals( "Top-Left-Corner",cur.cellType(0,0));
		assertEquals( "Top-Right-Corner",cur.cellType(3,0));
		assertEquals( "Bottom-Left-Corner",cur.cellType(0,5));
		assertEquals( "Bottom-Right-Corner",cur.cellType(3,5));
		assertEquals( "Up-Side",cur.cellType(2,0));
		assertEquals( "Down-Side",cur.cellType(2,5));
		assertEquals( "Left-Side",cur.cellType(0,2));
		assertEquals( "Right-Side",cur.cellType(3,2));
		assertEquals( "Middle",cur.cellType(1,1));

		assertEquals( 2,cur.cellLimit("Top-Left-Corner"));
		assertEquals( 3,cur.cellLimit("Left-Side"));
		assertEquals( 4,cur.cellLimit("Middle"));

		cur.emptyGrid();

		int[] a = new int[]{0,0};
		Player p2 = new Player(PlayerColour.Green,0);
		assertEquals( 0,cur.getCellPoints().size());
		cur.addPoint(p1,p1.getPlayerColour(),a,cur);
		int[] d =new int[]{0,1};
		cur.addPoint(p2,p2.getPlayerColour(),d,cur);
		assertEquals( 2,cur.getCellPoints().size());

		cur.addPoint(p1,p1.getPlayerColour(),a,cur);

		assertEquals( 2,cur.getCellPoints().size());
		//Red owns (0,1) now!
		assertEquals( PlayerColour.Red,cur.getCell(0,1).getPlayerColour());

	}
	
	@Test
	public void testRecursionStopper(){
		Player p1 = new Player(PlayerColour.Red,0);
		Grid cur = new Grid(4,6);
		cur.emptyGrid();
		assertEquals(0,cur.getCellPoints().size());
		assertEquals(0,cur.getStopper());
		cur.expand(p1,p1.getPlayerColour(),0,0);
		assertEquals(1,cur.getStopper());
		cur.resetStopper();
		assertEquals(0,cur.getStopper());
	}
	
	@Test
	public void testInfiniteRecursion2By2(){
		Atomination game = new Atomination();
		game.start(2,2,2);
		game.place(0,0,false);
		assertEquals(1,game.getGrid().getCellPoints().size());
		game.place(0,1,false);
		game.place(1,0,false);
		game.place(1,1,false);
		assertEquals(4,game.getGrid().getCellPoints().size());
		assertTrue(game.place(0,0,false));
	}

// 	@Test
// 	public void testReplay(){
// 		Atomination game = new Atomination();
// 		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
// 		System.setOut(new PrintStream(outContent));

// 		String e1= k+"Game Loaded\n\n";
// 		assertEquals(e1,outContent.toString());


// 		game.quit();
// 	}

}
