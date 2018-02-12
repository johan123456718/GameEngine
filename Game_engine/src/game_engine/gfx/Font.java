package game_engine.gfx;

public class Font {
    
    public static final Font STANDARD = new Font("/standardfont.png");
            
    private Image fontImage;
    private int[] offSets;
    private int[] widths;
    private int unicode;
    
    public Font(String path){
    
        fontImage = new Image(path);
        
        offSets = new int[59];
        widths = new int[59];
        unicode = 0;
        
        for(int i = 0; i < fontImage.getWidth(); i++){
        
            if(fontImage.getPixel()[i] == 0xff0000ff){
                offSets[unicode] = i;
            }
            if(fontImage.getPixel()[i] == 0xffffff00){
                widths[unicode] = i - offSets[unicode];
                unicode++;
            }
            
        }
        
    }

    public Image getFontImage() {
        return fontImage;
    }
    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }
    public int[] getOffSets() {
        return offSets;
    }
    public void setOffSets(int[] offSets) {
        this.offSets = offSets;
    }
    public int[] getWidths() {
        return widths;
    }
    public void setWidths(int[] widths) {
        this.widths = widths;
    }
    
}
