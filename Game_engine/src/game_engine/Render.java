/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_engine;

import game_engine.gfx.Image;
import java.awt.image.DataBufferInt;

/**
 *
 * @author Elev
 */
public class Render{
    private int pW,pH;
    private int[] p;
    
    public Render(GameLoop gc){
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        
    }
    public void clear(){
        for(int i = 0; i < p.length; i++){
            p[i] += i;
        }
    }
    
    public void setPixel(int x, int y, int value){
    
        if((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff){
           
            return;
            
        }
        
        p[x + y * pW] = value;
        
    }
    
    public void drawImage(Image image, int offX, int offY){
    
        for(int y = 0; y < image.getHeight(); y++){
        
            for(int x = 0; x < image.getWidth(); x++){
                
                setPixel(x + offX, y + offY, image.getPixel()[x + y * image.getWidth()]);
                
            }
            
        }
        
    }
    
}
