package game;

import game_engine.AbstractGame;
import game_engine.GameLoop;
import game_engine.Render;
import game_engine.gfx.Image;
import java.util.ArrayList;

public class GameManager extends AbstractGame {
    
    public static final int TS = 16;
    
    private boolean[] collision;
    private int levelWidth, levelHeight;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    public GameManager(){   
        objects.add(new Player(6, 4));   
        loadLevel("/level.png");
    }
    
    @Override
    public void init(GameLoop gc) {     
       gc.getRenderer().setAmbientColor(-1);
    }

    @Override
    public void update(GameLoop gc, float dt) {  
        for(int i = 0; i < objects.size(); i++){  
            objects.get(i).update(gc, this, dt);
            if(objects.get(i).isDead()){
                objects.remove(i);
                i--;
            }  
        }  
    }

    @Override
    public void render(GameLoop gc, Render r) { 
        
        for(int y = 0; y < levelHeight; y++){
            for(int x = 0; x < levelWidth; x++){
                
                if(collision[x + y * levelWidth]){
                    r.drawFillRect(x * TS, y * TS, TS, TS, 0xff0f0f0f);
                    
                }
                else{
                    r.drawFillRect(x * TS, y * TS, TS, TS, 0xfff9f9f9);                    
                }
                
            }
        }
        
        for(GameObject obj : objects){
            obj.render(gc, r);
        }    
    }
    
    public void loadLevel(String path){
        Image levelImage = new Image(path);
        
        levelWidth = levelImage.getWidth();
        levelHeight = levelImage.getHeight();
        collision = new boolean[levelWidth * levelHeight];
        
        for(int y = 0; y < levelImage.getHeight(); y++){
            for(int x = 0; x < levelImage.getWidth(); x++){
                if(levelImage.getPixel()[x + y * levelImage.getWidth()] == 0xff000000){
                    collision[x + y * levelImage.getWidth()] = true;
                }else{
                    collision[x + y * levelImage.getWidth()] = false;
                }
            }
        }
    }
    
    public boolean getCollision(int x, int y){
        if(x < 0 || x >= levelWidth || y < 0 || y >= levelHeight){
            return true;
        }
        return collision[x + y * levelWidth];
    }
    
    public static void main(String args[]){
        GameLoop gc = new GameLoop(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(2f);
        gc.start();
    }
    
}
