package game;

import game_engine.AbstractGame;
import game_engine.GameLoop;
import game_engine.Render;
import java.util.ArrayList;

public class GameManager extends AbstractGame {
    
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();

    public GameManager(){   
        objects.add(new Player(2, 2));    
    }
    
    @Override
    public void init(GameLoop gc) {     
       gc.getRenderer().setAmbientColor(-1);
    }

    @Override
    public void update(GameLoop gc, float dt) {  
        for(int i = 0; i < objects.size(); i++){  
            objects.get(i).update(gc, dt);
            if(objects.get(i).isDead()){
                objects.remove(i);
                i--;
            }  
        }  
    }

    @Override
    public void render(GameLoop gc, Render r) { 
        for(GameObject obj : objects){
            obj.render(gc, r);
        }    
    }
    
    public static void main(String args[]){
        GameLoop gc = new GameLoop(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(3f);
        gc.start();
    }
    
}
