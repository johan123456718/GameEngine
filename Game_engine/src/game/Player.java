package game;

import game_engine.GameLoop;
import game_engine.Render;
import java.awt.event.KeyEvent;

public class Player extends GameObject {
    
    private float speed = 100;
    
    public Player(int posX, int posY){
        this.tag = "player";
        this.posX = posX * 16;
        this.posY = posY * 16;
        this.width = 16;
        this.height = 16;
    }

    @Override
    public void update(GameLoop gc, float dt) {
        
        if(gc.getInput().isKey(KeyEvent.VK_W)){
            posY -= dt * speed;
        }
        
        if(gc.getInput().isKey(KeyEvent.VK_S)){
            posY += dt * speed;
        }
        
        if(gc.getInput().isKey(KeyEvent.VK_A)){
            posX -= dt * speed;
        }
        
        if(gc.getInput().isKey(KeyEvent.VK_D)){
            posX += dt * speed;
        }
        
    }

    @Override
    public void render(GameLoop gc, Render r) {
        
        r.drawFillRect((int)posX, (int)posY, width, height, 0xff00ff00);
        
    }
    
}
