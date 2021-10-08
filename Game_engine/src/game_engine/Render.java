package game_engine;

import game_engine.gfx.Font;
import game_engine.gfx.Image;
import game_engine.gfx.ImageRequest;
import game_engine.gfx.ImageTile;
import game_engine.gfx.Light;
import game_engine.gfx.LightRequest;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Handles all type of rendering on the screen.
 */

public class Render{ 
    
    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
    private ArrayList<LightRequest> lightRequest = new ArrayList<LightRequest>();
    
    private int pW,pH;
    private int[] p;
    private int[]zBuffer;
    private int[] lightMap;
    private int[] lightBlock;
    
    private int ambientColor = 0xff232323;
    private int zDepth = 0;
    private boolean processing = false;
    /**
     * The render constructor, initializes all arrays and variables.
     */
    public Render(GameLoop gc){
        pW = gc.getWidth();
        pH = gc.getHeight();
        p = ((DataBufferInt)gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[p.length];
        lightMap = new int[p.length];
        lightBlock = new int[p.length];
    }
    /**
     * Handles image and lightprocessing to render correctly and in the right order.
     */
    public void process(){
        
        processing = true;
        
        Collections.sort(imageRequest, new Comparator<ImageRequest>(){    
            @Override
            public int compare(ImageRequest i0, ImageRequest i1){
                if(i0.zDepth < i1.zDepth){
                    return -1;
                }
                if(i0.zDepth > i1.zDepth){
                    return 1;
                }
                return 0;
            } 
        });
        
        for(int i = 0; i < imageRequest.size(); i++){  
            ImageRequest ir = imageRequest.get(i);
            setzDepth(ir.zDepth);
            drawImage(ir.image, ir.offX, ir.offY);
        }
        
        //Draw lighting
        for(int i = 0; i < lightRequest.size(); i++){
            LightRequest l = lightRequest.get(i);
            drawLightRequest(l.light, l.locX, l.locY);
        }
        
        for(int i = 0; i < p.length; i++){
            float r = ((lightMap[i] >> 16) & 0xff) / 255f;
            float g = ((lightMap[i] >> 8) & 0xff) / 255f;
            float b = (lightMap[i] & 0xff) / 255f;
            
            p[i] = ((int)(((p[i] >> 16) & 0xff) * r) << 16 | (int)(((p[i] >> 8) & 0xff) * g) << 8 | (int)((p[i] & 0xff) * b)); 
        }
        
        imageRequest.clear();
        lightRequest.clear();
        processing = false;
        
    }
    /**
     * Makes sure the screen is clean.
     */
    public void clear(){
        for(int i = 0; i < p.length; i++){
            p[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }
    
    /**
     * Draws text on the screen.
     * @param text
     * @param offX
     * @param offY
     * @param color 
     */
    public void drawText(String text, int offX, int offY, int color){

        int offSet = 0;
        
        for(int i = 0; i < text.length(); i ++){
            int unicode  = text.codePointAt(i);
            
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
    /**
     * Handles the lightmap with all the lights in.
     * @param x
     * @param y
     * @param value 
     */
    public void setLightMap(int x, int y, int value){
        
        if(x < 0 || x >= pW || y < 0 || y >= pH){
           
            return;
            
        }
        
        int baseColor = lightMap[x + y * pW];
        
        int maxRed = Math.max((baseColor >> 16) & 0xff, (value >> 16) & 0xff);
        int maxGreen = Math.max((baseColor >> 8) & 0xff, (value >> 8) & 0xff);
        int maxBlue = Math.max(baseColor & 0xff, value & 0xff);
        
        lightMap[x + y * pW] = (maxRed << 16 | maxGreen << 8 | maxBlue);
        
    }
    /**
     * Handles which images block the light depending on zDepth.
     * @param x
     * @param y
     * @param value 
     */
    public void setLightBlock(int x, int y, int value){
        
        if(x < 0 || x >= pW || y < 0 || y >= pH){
           
            return;
            
        }
        
        if(zBuffer[x + y * pW] > zDepth){
            
            return;
            
        }

        lightBlock[x + y * pW] = value;
        
    }
    
    /**
     * Assigns the image pixels to an array and makes transparent for certain value
     */
    public void setPixel(int x, int y, int value){
    
        int alpha = ((value >> 24) & 0xff);
        if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0){
           
            return;
            
        }
        
        int index = x + y * pW;
        
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

            p[index] = (newRed << 16 | newGreen << 8 | newBlue); 
            
        }
                
    }
    
    /**
     * Draws an image on screen
     */
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
                setLightBlock(x + offX, y + offY, image.getLightBlock());
                
            }
            
        }
        
    }
    
    /**
     * Draws an imagetile on screen.
     */
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
                setLightBlock(x + offX, y + offY, image.getLightBlock());
                
            }
            
        }
        
    }
    /**
     * Draws a non filled rectangle on the screen, specify a color with 0xff followed by any RGB color in hexadecimal.
     * @param offX
     * @param offY
     * @param width
     * @param height
     * @param color 
     */
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
    
    /**
     * Draws a filled rectangle on the screen, specify a color with 0xff followed by any hexadecimal color in RGB.
     */
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
    
    public void drawLight(Light l, int offX, int offY){
        
        lightRequest.add(new LightRequest(l, offX, offY));
        
    }
    
    private void drawLightRequest(Light l, int offX, int offY){ 
        
        for(int i = 0; i <= l.getDiameter(); i++){
            
            drawLightLine(l,l.getRadius(), l.getRadius(), i, 0, offX, offY);
            drawLightLine(l,l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY);
            drawLightLine(l,l.getRadius(), l.getRadius(), 0, i, offX, offY);
            drawLightLine(l,l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY);   
            
        }
        
    }
    
    private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY){
        
        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);
        
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        
        int err = dx - dy;
        int err2;
        
        while(true){
            
            int screenX = x0 - l.getRadius() + offX;
            int screenY = y0 - l.getRadius() + offY;
            
            if(screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH){
                return;
            }
            
            int lightColor = l.getLightValue(x0, y0);
            if(lightColor == 0){
                return;
            }
            
            if(lightBlock[screenX + screenY * pW] == Light.FULL){
                return;
            }
            
            setLightMap(screenX, screenY, lightColor);
            
            if(x0 == x1 && y0 == y1){
                break;
            }
            
            err2 = 2 * err;
            
            if(err2 > -1 * dy){
                err -= dy;
                x0 += sx;
            }
            
            if(err2 < dx){
                err += dx;
                y0 += sy;
            }
            
        }
        
    }
    
    public int getzDepth() {
        return zDepth;
    }
    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
    public int getAmbientColor() {
        return ambientColor;
    }
    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }
    
}
