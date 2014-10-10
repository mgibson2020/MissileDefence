/*
 * Program: Sound.java
 * Project: MissileDefense
 * Author: J. Ethan Wallace and Michael Gibson
 * Date Written: 10/05/2014 - 10/08/2014
 * Abstract: This class is used to play sounds in the game.
 * 
 * This code was copied from the tutorial provided at http://noobtuts.com/java/play-sounds
 * I found it very helpful to run the audio on different threads
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	//public final static String d = MDMain.class.getResource("/snd/Ding.wav").toExternalForm();
	
    public static synchronized void play(final String fileName) 
    {    	    	
        // Note: use .wav files             
        new Thread(new Runnable() { 
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
                    clip.open(inputStream);
                    clip.start(); 
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
    }
}