package game_engine;

public abstract class AbstractGame {
    
    public abstract void update(GameLoop gc, float dt);
    public abstract void render(GameLoop gc, Render r);
    
}
