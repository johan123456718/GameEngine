/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_engine;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 *
 * @author elev
 */
public class audio{

    /**
     * @param args the command line arguments
     */
    
    public JFrame frame = new JFrame("Hej");
    public JTextField textField = new JTextField();

    
    public audio(){
        frame = new JFrame("Jump");
        frame.setVisible(true);
        frame.setSize(100, 100);
        frame.setLocation(500, 300);
        frame.setDefaultCloseOperation(3);
        frame.add(textField, BorderLayout.CENTER);
        textField.addKeyListener(listener);
    }
    
    public static void main(String[] args) {
        new audio();
        try {
            File file = new File("/Users/elev/NetBeansProjects/Gymnasiearbete/src/gymnasiearbete/island_music_x.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            Thread.sleep(clip.getMicrosecondLength());
            } catch (Exception e) {
        System.err.println(e.getMessage());
        }
    }
        
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if(keycode == 87){
            try {
            File file = new File("/Users/elev/Downloads/Jump-SoundBible.com-1007297584.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength());
            } catch (Exception ex) {
        System.err.println(ex.getMessage());
        }
        }
    }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
}
