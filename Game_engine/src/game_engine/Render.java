/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_engine;

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
}
