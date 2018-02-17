package game_engine.gfx;

public class ImageTile extends Image {
    
    private int tileWidth, tileHeight;
    
    public ImageTile(String path, int tileWidth, int tileHeight){
    
        super(path);
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
  
    }
    
    public Image getTileImage(int tileX, int tileY){
        
        int pixel[] = new int [tileWidth * tileHeight];
        
        for(int y = 0; y < tileHeight; y++){
            for(int x = 0; x < tileWidth; x++){
            
               pixel[x + y * tileWidth] =  this.getPixel()[(x + tileX * tileWidth) + (y + tileY * tileHeight) * this.getWidth()];
            
            } 
        }
         
        return new Image(pixel, tileWidth, tileHeight);
        
    }

    public int getTileWidth() {
        return tileWidth;
    }
    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }
    public int getTileHeight() {
        return tileHeight;
    }
    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }
    
}
