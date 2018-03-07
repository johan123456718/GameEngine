package game;

import game_engine.GameLoop;
import game_engine.Render;
import java.awt.event.KeyEvent;

public class Player extends GameObject {
    
    private int tileX, tileY;
    private float offX, offY;
    
    private float speed = 100;
    private float fallSpeed = 10;
    private float jump = -3.4f;
    private boolean ground = false;
    
    private float fallDist = 0;
    
    public Player(int posX, int posY){
        this.tag = "player";
        this.tileX = posX;
        this.tileY = posY;  
        this.offX = 0;
        this.offY = 0;
        this.posX = posX * GameManager.TS;
        this.posY = posY * GameManager.TS;
        this.width = GameManager.TS;
        this.height = GameManager.TS;
    }

    @Override
    public void update(GameLoop gc, GameManager gm, float dt) {
        
        //Left and Right
        if(gc.getInput().isKey(KeyEvent.VK_D)){
            offX += dt * speed;
        }
        
        if(gc.getInput().isKey(KeyEvent.VK_A)){
            offX -= dt * speed;
        }
        //End of Left and Right
        
        //Beginning of Jump and Gravity
        fallDist += dt * fallSpeed;
        
        if(gc.getInput().isKeyDown(KeyEvent.VK_W) && ground ){
            fallDist = jump;
            ground = false;
        }
        
        offY += fallDist;
        
        if(gm.getCollision(tileX, tileY + 1) && offY >= 0){
            fallDist = 0;
            offY = 0;
            ground = true;
        }   
        //End of Jump and Gravity
        
        //Final position
        if(offY > GameManager.TS / 2){
            tileY ++;
            offY -= GameManager.TS;
        }
        
        if(offY < -GameManager.TS / 2){
            tileY--;
            offY += GameManager.TS;
        }
        
        if(offX > GameManager.TS / 2){
            tileX ++;
            offX -= GameManager.TS;
        }
        
        if(offX < -GameManager.TS / 2){
            tileX--;
            offX += GameManager.TS;
        }
        
        posX = tileX * GameManager.TS + offX;
        posY = tileY * GameManager.TS + offY;
        
    }

    @Override
    public void render(GameLoop gc, Render r) {
        
        r.drawFillRect((int)posX, (int)posY, width, height, 0xff00ff00);
        
    }
    
}
