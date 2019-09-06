package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DreamEngine {

	public static int dreamCycleLength = 5;
	public static int searchCycleLength = 10;
	
	public static void main(String[] args) {
		Searcher s = new Searcher ();
		for (String ss : s.links("Boris Johnson")) {
			System.out.println (ss);
			s.text(ss);
		}
		
		//DreamEngine de = new DreamEngine ();
		//de.initiate();
	}
	
	private ArrayList<String> texts = new ArrayList<String> ();
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage> ();
	private Thread operatorThread;
	private boolean operationState;
	
	public void initiate () {	
		operatorThread = new Thread () {
			public void run () {
				while (true) {
					startSearchCycle ();
					startDreamCycle ();
				}
			}
		};
		operatorThread.start();
		
		while (true) {
			operationState = false;
			try {
				Thread.sleep(1000 * 60 * searchCycleLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			operationState = true;
			try {
				Thread.sleep(1000 * 60 * dreamCycleLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startDreamCycle () {
		System.out.println ("Starting dream cycle.");
		while (operationState) {
			System.out.println ("Dream!");
		}
	}
	
	public void startSearchCycle () {
		System.out.println ("Starting search cycle.");
		while (!operationState) {
			System.out.println ("Search!");
		}
	}

}
