/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_engine;

import game_engine.gfx.Image;
import game_engine.gfx.ImageTile;
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
    
    //Assigns the image pixels to an array and makes transparent for certain value
    public void setPixel(int x, int y, int value){
    
        if((x < 0 || x >= pW || y < 0 || y >= pH) || value == 0xffff00ff){
           
            return;
            
        }
        
        p[x + y * pW] = value;
        
    }
    
    //Draws an image on screen
    public void drawImage(Image image, int offX, int offY){
    
        //Don't render
        if(offX < -image.getWidth()){
            return;  
        }
        if(offY < -image.getHeight()){
            return;  
        }
        if(offX >= pW){
            return;  
        }
        if(offY >= pH){
            return;  
        }
        
        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();
        
        //Clipping
        if(offX < 0){
            newX -= offX; 
        }
        if(offY < 0){
            newY -= offY;
        }
        if(newWidth + offX >= pW){
            newWidth -= newWidth + offX - pW;
        }
        if(newHeight + offY >= pH){
            newHeight -= newHeight + offY - pH;
        }
        
        //Render code
        for(int y = newY; y < newHeight; y++){
        
            for(int x = newX; x < newWidth; x++){
                
                setPixel(x + offX, y + offY, image.getPixel()[x + y * image.getWidth()]);
                
            }
            
        }
        
    }
    
    //Draws an imagetile on screen
    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY){
    
        //Don't render
        if(offX < -image.getTileWidth()){
            return;  
        }
        if(offY < -image.getTileHeight()){
            return;  
        }
        if(offX >= pW){
            return;  
        }
        if(offY >= pH){
            return;  
        }
        
        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileWidth();
        int newHeight = image.getTileHeight();
        
        //Clipping
        if(offX < 0){
            newX -= offX; 
        }
        if(offY < 0){
            newY -= offY;
        }
        if(newWidth + offX >= pW){
            newWidth -= newWidth + offX - pW;
        }
        if(newHeight + offY >= pH){
            newHeight -= newHeight + offY - pH;
        }
        
        //Render code
        for(int y = newY; y < newHeight; y++){
        
            for(int x = newX; x < newWidth; x++){
                
                setPixel(x + offX, y + offY, image.getPixel()[(x + tileX * image.getTileWidth()) + (y + tileY * image.getTileHeight()) * image.getWidth()]);
                
            }
            
        }
        
    }
    
}
