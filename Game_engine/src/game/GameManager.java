package game;

import game_engine.AbstractGame;
import game_engine.GameLoop;
import game_engine.Render;
import game_engine.audio.SoundClip;
import game_engine.gfx.Image;
import java.awt.event.KeyEvent;

public class GameManager extends AbstractGame {

    public GameManager(){

    }
    
    public void reset(){
        
    }
    
    @Override
    public void update(GameLoop gc, float dt) {
  
    }

    @Override
    public void render(GameLoop gc, Render r) {
        r.drawFillRect(gc.getInput().getMouseX() - 16, gc.getInput().getMouseY() - 16, 32, 32, 0xffffffcc);
    }
    
    public static void main(String args[]){
    
        GameLoop gc = new GameLoop(new GameManager());
        gc.setWidth(320);
        gc.setHeight(240);
        gc.setScale(3f);
        gc.start();
        
    }
    
}
