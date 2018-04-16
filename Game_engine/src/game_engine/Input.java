package game_engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Handles all input mouse and keyboard.
 */

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    
    private GameLoop gc;
    
    private final int NUM_KEYS = 256;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];
    
    private final int NUM_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];
    
    private int mouseX, mouseY;
    private int scroll;
    
    /**
     * Input constructor, adds all the listeners
     * @param gc
     */
    public Input(GameLoop gc){
    
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;
        
        gc.getWindow().getCanvas().addKeyListener(this);
        gc.getWindow().getCanvas().addMouseMotionListener(this);
        gc.getWindow().getCanvas().addMouseListener(this);
        gc.getWindow().getCanvas().addMouseWheelListener(this);
        
    }
    
    /**
     * Checks to see if any key has been pressed
     */
    public void update(){
    
        scroll = 0;
        
        for(int i = 0; i < NUM_KEYS; i++){
        
            keysLast[i] = keys[i];
            
        }
        
        for(int i = 0; i < NUM_BUTTONS; i++){
        
            buttonsLast[i] = buttons[i];
            
        }
        
    }
    /**
     * Checks to see if a specific key is being pressed, continues sending true if the key is held down.
     * @param KeyCode
     */
    public boolean isKey(int keyCode){
    
        return keys[keyCode];
        
    }
    /**
     * Checks to see if a specific key is being released, only sends true once.
     * @param KeyCode
     */
    public boolean isKeyUp(int keyCode){
    
        return !keys[keyCode] && keysLast[keyCode];
        
    }
    /**
     * Checks to see if a specific key is pressed down, only sends true once.
     * @param KeyCode
     */
    public boolean isKeyDown(int keyCode){
    
        return keys[keyCode] && !keysLast[keyCode];
        
    }
    /**
     * Checks to see if a specific button(on the mosue) is being pressed, continues sending true if the key is held down.
     * @param KeyCode
     */
    public boolean isButton(int button){
        return buttons[button];
        
    }
    /**
     * Checks to see if a specific button(on the mouse) is being released, only sends true once.
     * @param KeyCode
     */
    public boolean isButtonUp(int button){
    
        return !buttons[button] && buttonsLast[button];
        
    }
    /**
     * Checks to see if a specific button(on the mouse) is pressed down, only sends true once.
     * @param KeyCode
     */
    public boolean isButtonDown(int button){
    
        return buttons[button] && !buttonsLast[button];
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    /**
     * Checks to see if any key at all is being pressed
     * @param e 
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }
    /**
     * Chekcs to see if any key at all got released
     * @param e 
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    /**
     * Checks to see if any button(on the mouse) at al is being pressed.
     * @param e 
     */
    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }
    /**
     * Checks to see if any button(on the mouse) at all got released.
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }
    /**
     * empty
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    /**
     * empty
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
    /**
     * Checks to see how far the mouse has been dragged in X and Y
     * @param e 
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }
    /**
     * Checks to see how far the mouse has been moved in X and Y
     * @param e 
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int)(e.getX() / gc.getScale());
        mouseY = (int)(e.getY() / gc.getScale());
    }
    /**
     * Checks to see if the mousewheel has been move, differentiates betweem up and down scroll
     * @param e 
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {
        return mouseX;
    }
    public int getMouseY() {
        return mouseY;
    }
    public int getScroll() {
        return scroll;
    }
    
}
