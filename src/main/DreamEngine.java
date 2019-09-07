package main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DreamEngine {

	public static int dreamCycleLength = 5;
	public static int searchCycleLength = 10;
	
	public static void main(String[] args) {
		DreamEngine engine = new DreamEngine ();
	}
	
	private ArrayList<String> texts = new ArrayList<String> ();
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage> ();
	private Searcher searcher = new Searcher ();
	private Random random = new Random (0);

	public void gatherData (String prompt, int pages, int wordsToAcquire) {
		ArrayList<String> links = new ArrayList<String> (Arrays.asList(searcher.links(prompt)));
		for (int i = 0; i < pages; i++) {
			if (links.size() < 1) break;
			String text = searcher.text(links.get(random.nextInt(links.size())));
			ArrayList<String> words = new ArrayList<String> (Arrays.asList(text.split(" ")));
			for (int j = 0; j < wordsToAcquire; j++) {
				if (words.size() < 1) break;
				String word = words.get(random.nextInt(words.size()));
				texts.add(word);
				while (true) if (!words.remove(word)) break;
			}
		}
	}
	
	public BufferedImage activeDream;
	
	public void addTextToDream (String s, Seed seed) {
		// TODO: Add text
	}
	
	public void addImageToDream (BufferedImage s, Seed seed) {
		// TODO: Add image
	}
	
	public void exportDream (String prefixPath) {
		// TODO: Export dream
	}
	
	public void start () {
		Thread t = new Thread () {
			// TODO: Mainlooping
		};
	}

}
