package game;

import game_engine.GameLoop;
import game_engine.Render;

/**
 * An abstract class handling all game objects in the game.
 */

public abstract class GameObject {
    
    protected String tag;
    protected float posX, posY;
    protected int width, height;
    protected boolean dead = false;
    
    public abstract void update(GameLoop gc, GameManager gm, float dt);
    public abstract void render(GameLoop gc, Render r);

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public float getPosX() {
        return posX;
    }
    public void setPosX(float posX) {
        this.posX = posX;
    }
    public float getPosY() {
        return posY;
    }
    public void setPosY(float posY) {
        this.posY = posY;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public boolean isDead() {
        return dead;
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }
    
}
