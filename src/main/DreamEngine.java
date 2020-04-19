package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DreamEngine {

	public static int dreamCycleLength = 5;
	public static int searchCycleLength = 10;
	int iterationNum = 0;
	
	public static void main(String[] args) {
		DreamEngine engine = new DreamEngine ();
	}
	
	private ArrayList<String> texts = new ArrayList<String> ();
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage> ();
	private Searcher searcher = new Searcher ();
	private Random random = new Random ();

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
	public boolean isOperating;
	private ArrayList<Node> activeNodes = new ArrayList<Node> ();
	
	public String makeInstructionSequence () {
		// TODO: Make Instruction Seqeuence
	}
	
	public void setPixel (int x, int y, Color col) {
		synchronized (setRequests) {
			setRequests.add(new PixelSetRequest (x, y, col));
		}
	}
	
	private ArrayList<PixelSetRequest> setRequests = new ArrayList<PixelSetRequest> ();
	
	public void writeNodeDeterminators () {
		String data = "";
		for (Node n : activeNodes) {
			String det = "";
			for (String s : n.determinator) {
				det += s + ";";
			}
			data += det + "\n";
		}
		try {
            Files.write(Paths.get("nodes.txt"), data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String[] readNodeDeterminators () {
		// TODO: READ IN
	}
	
	public void exportDream () {
		// TODO: Export dream
	}
	
	public void updateDream () {
		ArrayList<PixelSetRequest> reqs;
		synchronized (setRequests) {
			reqs = (ArrayList<PixelSetRequest>)setRequests.clone();
			setRequests.clear();
		}
		for (PixelSetRequest req : reqs) {
			activeDream.setRGB(req.x, req.y, req.col.getRGB());
		}
	}
	
	// TODO: GRAPHICS
	
	public void start () {
		// TODO: Fetch images
		isOperating = true;
		for (int i = 0; i < 8; i++) {
			Node node = new Node (this, makeInstructionSequence(), images.get(random.nextInt(images.size())));
			activeNodes.add(node);
		}
		while (iterationNum < 1000) {
			updateDream();
		}
		isOperating = false;
		writeNodeDeterminators ();
		updateDream ();
		exportDream ();
	}

}
