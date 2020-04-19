package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.image.BufferedImage;


public class Node {
	public ArrayList<String> determinator;
	
	BufferedImage buffer;
	boolean[] states = {false, false, false, false, false, false, false, false};
	
	DreamEngine engine;
	Random random = new Random ();
	
	public Node (DreamEngine en, String det, BufferedImage im) {
		engine = en;
		buffer = im;
		determinator = new ArrayList<String> (Arrays.asList(det.split(";")));
		Thread t = new Thread () {
			public void run () {
				while (engine.isOperating) {
					executeDeterminator ();
					if (random.nextInt(10) == 9) {
						mutateDeterminator ();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	private void mutateDeterminator() {
		// TODO Mutate node determinator
		
	}

	public void executeDeterminator () {
		// TODO: Execute node determinator
	}
}
