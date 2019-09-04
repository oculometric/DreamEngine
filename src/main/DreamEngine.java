package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DreamEngine {

	public static void main(String[] args) {
		Searcher s = new Searcher ();
		for (String ss : s.links("Boris Johnson")) {
			System.out.println (ss);
		}
	}
	
	private ArrayList<String> texts = new ArrayList<String> ();
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage> ();
	private Thread operatorThread;
	
	public void initiate () {
		operatorThread = new Thread () {
			public void run () {
				
			}
		};
		// TODO: Time-based operation splitting
	}
	
	public void startDreamCycle () {
		
	}
	
	public void startSearchCycle () {
		
	}

}
