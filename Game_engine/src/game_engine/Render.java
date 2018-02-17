package game_engine;

import game_engine.gfx.Font;
import game_engine.gfx.Image;
import game_engine.gfx.ImageRequest;
import game_engine.gfx.ImageTile;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Render{
    
    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
    
    private int pW,pH;
    private int[] p;
    private int[]zBuffer;
    
    private int zDepth = 0;
    private boolean processing = false;
 
    public Render(GameLoop gc){
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
    }
    
    public void process(){
        
        processing = true;
        
        Collections.sort(imageRequest, new Comparator<ImageRequest>(){    
            @Override
            public int compare(ImageRequest i0, ImageRequest i1){
                if(i0.zDepth < i1.zDepth){
                    return -1;
                }
                if(i0.zDepth > i1.zDepth){
                    return 11;
                }
                return 0;
            } 
        });
        
        for(int i = 0; i < imageRequest.size(); i++){  
            ImageRequest ir = imageRequest.get(i);
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }
        
        imageRequest.clear();
        processing = false;
        
    }
    
    public void clear(){
        for(int i = 0; i < p.length; i++){
            p[i] = 0;
            zBuffer[i] = 0;
        }
    }
    
    //Render font
    public void drawText(String text, int offX, int offY, int color){

        text = text.toUpperCase();
        int offSet = 0;
        
        for(int i = 0; i < text.length(); i ++){
            int unicode  = text.codePointAt(i) - 32;
            
            for(int y = 0; y < font.getFontImage().getHeight(); y++){
            
                for(int x = 0; x < font.getWidths()[unicode]; x++){
                
                    if(font.getFontImage().getPixel()[(x + font.getOffSets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff){
                    
                        setPixel(x + offX + offSet, y + offY, color);
                        
                    }
                
                }
                
            }
            
            offSet += font.getWidths()[unicode];
            
        }
        
    }
    
    //Assigns the image pixels to an array and makes transparent for certain value
    public void setPixel(int x, int y, int value){
    
        int alpha = ((value >> 24) & 0xff);
        if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0){
           
            return;
            
        }
        
        int index = x + y + pW;
        
        if(zBuffer[index] > zDepth){
            
            return;
            
        }
        
        zBuffer[index] = zDepth;
        
        if(alpha == 255){
            
            p[index] = value;
            
        }else{
            
            int pixelColor = p[index];
            
            int newRed = ((pixelColor >> 16) & 0xff) - (int)((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha/255f));
            int newGreen = ((pixelColor >> 8) & 0xff) - (int)((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha/255f));
            int newBlue = (pixelColor & 0xff) - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha/255f));

            p[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue); 
            
        }
                
    }
    
    //Draws an image on screen
    public void drawImage(Image image, int offX, int offY){
        
        if(image.isAlpha() && !processing){
            imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }
    
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
    
        if(image.isAlpha() && !processing){
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }
        
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
    
    public void drawRect(int offX, int offY, int width, int height, int color){
        
        for(int y = 0; y <= height; y++){
        
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
            
        }
        
        for(int x = 0; x <= width; x++){
        
            setPixel(x + offX, offY, color);
            setPixel(x + offX, offY + height, color);
            
        }
        
    }
    
    public void drawFillRect(int offX, int offY, int width, int height, int color){
        
        //Don't render
        if(offX < -width){
            return;  
        }
        if(offY < -height){
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
        int newWidth = width;
        int newHeight = height;
        
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
        
        for(int y = newY; y < newHeight; y++){
                    
            for(int x = 0; x < newWidth; x++){
                
                setPixel(x + offX, y + offY, color);
                
            }
            
        }
        
    }
    
    public int getzDepth() {
        return zDepth;
    }
    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
    
}
