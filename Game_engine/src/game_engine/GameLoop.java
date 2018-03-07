/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elev
 */
public class GameLoop implements Runnable {
    
    private Thread thread;
    private Render renderer;
    private Window window;
    private Input input;
    private AbstractGame game;
	
    private boolean running = false;
    private final double UPDATE_CAP = 1.0/60.0;

    private int width = 300, height = 200;
    private float scale = 3f;
    private String title = "GameEngine";
    
    public GameLoop (AbstractGame game){
        
        this.game = game;
        
    }
    
    public void start(){
        window = new Window(this); 
        renderer = new Render(this);
        input = new Input(this);
        thread = new Thread(this);
        thread.run();
    }
    
    public void stop(){
        
    }
    
    public void run(){
        
        running = true;
        
        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unproccedTime = 0;
        
        double frameTime = 0;
        int frames = 0;
        int fps = 0;
        
        game.init(this);
        
        while(running){
            render = true;
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime  = firstTime - lastTime;
            lastTime = firstTime;
            
            unproccedTime += passedTime;
            frameTime += passedTime;
            
            while  (unproccedTime >= UPDATE_CAP){
                
                unproccedTime -= UPDATE_CAP;
                render = true;
                
                game.update(this, (float)UPDATE_CAP);
                
                input.update();
                
                if(frameTime >= 1.0){
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
                
            }
            
            if(render){
                renderer.clear();
                game.render(this, renderer);
                renderer.process();
                renderer.drawText("FPS: " + fps, 0, 0, 0xff00ffff);          
                window.update();
                frames ++;
            }else{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        dispose(); 
        
    }
    
    private void dispose(){
        
    }

    public Render getRenderer() {
        return renderer;
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
    public float getScale() {
        return scale;
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    public String getTitle() {
        return title;
    }
    public Window getWindow() {
        return window;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Input getInput() {
        return input;
    }

}