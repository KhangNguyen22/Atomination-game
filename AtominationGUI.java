import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import java.util.*;

public class AtominationGUI extends PApplet {
    private PImage img;
    private boolean flag=true;
    private Atomination game=new Atomination();
    private HashMap<int[],Cell> map;
    private int width=80;
    private int height=80;

    public AtominationGUI() {
      System.out.println("Game Starting!");
      game.start(2,10,6);

    }

    public void mouseClicked(MouseEvent event) {
      int x = (int) Math.round(Math.floor(event.getX()/width));
      int y = (int) Math.round(Math.floor(event.getY()/height));
      // int y = mouseY;
      // System.out.println(Integer.toString(x)+","+Integer.toString(y));
      if(flag){
          game.stat();
          game.place(x,y,false);
      }
      map = game.getGrid().getCellPoints();
      // System.out.println(Math.round(Math.floor(event.getX()/64)) +","+Math.round(Math.floor(event.getY()/64)));
    }

    public void setup() {
        frameRate(60);
        img = loadImage("./assets/tile.png");
        img.resize(width,height);
    }

    public void settings() {
        /// DO NOT MODIFY SETTINGS
        size(width*10, height*6);
    }

    public void draw() {
      int x=0;
      int y=0;
      for (int i=0; i< 7;i++){
        y=i*height;
        for(int j=0;j<11;j++){
          image(img,x,y);
          x=j*width;
        }
      }
      if(map!= null){
        for (int[] key:map.keySet()){
          Cell cur = map.get(key);
          int[] coord = cur.getCoordinate();
          PlayerColour pColour=cur.getPlayerColour();
          String dirtyColour = pColour.toString();
          String colour = dirtyColour.toLowerCase();

          int num= cur.getAtomNumber();
          if(num==0){
              PImage t=loadImage("./assets/"+colour+"1.png");
              t.resize(width,height);
              image(t,width*coord[0],height*coord[1]);
            }
            else{
              PImage t=loadImage("./assets/"+colour +Integer.toString(num)+".png");
              t.resize(width,height);
              image(t,width*coord[0],height*coord[1]);
            }
        }
      }

      if(game.getP().size()==1 && flag){
        // System.out.println("We have a Winner!");
        game.quit();
        flag=false;
      }

    }

    public static void main(String args[]) {
        PApplet.main("AtominationGUI");
    }



}
