package game_engine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {
    
    private int width, height;
    private int[] pixel;
    
    //Loads in an image then flushes it from path
    public Image(String Path){
    
        BufferedImage image = null;
        
        try{ 
            image = ImageIO.read(Image.class.getResourceAsStream(Path));    
        }catch(IOException e){   
            e.printStackTrace();   
        }
        
        width = image.getWidth();
        height = image.getHeight();
        pixel = image.getRGB(0, 0, width, height, null, 0, width);
        
        image.flush(); 
        
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
    public int[] getPixel() {
        return pixel;
    }
    public void setPixel(int[] pixel) {
        this.pixel = pixel;
    }
    
}
