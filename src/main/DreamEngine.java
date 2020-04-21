package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class DreamEngine {

	public static int dreamCycleLength = 5;
	public static int searchCycleLength = 10;
	int iterationNum = 0;
	
	public boolean rebuildNodes;
	public int numNodes;
	public int numIts;
	public int imageSize;
	public int windowSize;
	public String searchPrompt;
	
	public static void main(String[] args) {
		Dialog d = new Dialog ();
		Object[] vals = d.run();		
		DreamEngine engine = new DreamEngine ();
		engine.rebuildNodes = (boolean)vals[0];
		engine.numNodes = (int)vals[1];
		engine.numIts = (int)vals[2];
		engine.imageSize = (int)vals[3];
		engine.windowSize = (int)vals[4];
		engine.searchPrompt = (String)vals[5];
		engine.start();
	}
	
	private ArrayList<String> texts = new ArrayList<String> ();
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage> ();
	private Searcher searcher = new Searcher ();
	private Random random = new Random ();
	private DreamViewer viewer;
	
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
	
	public void fetchImages (int num, String prompt) {
		ArrayList<String> links = new ArrayList<String> (Arrays.asList(searcher.links(prompt)));
		int imagesFound = 0;
		while (imagesFound < num) {
			if (links.size() < 1) break;
			BufferedImage[] imgs = searcher.images(links.get(random.nextInt(links.size())), 2);
			for (BufferedImage im : imgs) {
				imagesFound++;
				images.add(im);
				if (imagesFound >= num) break;
			}
		}
	}
	
	public BufferedImage activeDream;
	public boolean isOperating;
	private ArrayList<Node> activeNodes = new ArrayList<Node> ();
	
	public void setPixel (int x, int y, int col) {
		synchronized (setRequests) {
			setRequests.add(new PixelSetRequest (x, y, col));
		}
	}
	
	public int getPixel (int x, int y) {
		synchronized (activeDream) {
			return activeDream.getRGB(x, y);
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
		File file = new File("nodes.txt");
		ArrayList<String> inp = new ArrayList<String> ();
		Scanner sc;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) inp.add(sc.nextLine());
			sc.close();
			return inp.toArray(new String[inp.size()]);
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public void exportDream () {
		File f = new File ("dream.png");
		int num = 0;
		while (f.exists()) {
			num++;
			f = new File ("dream" + num + ".png");
		}
		try {
			
			ImageIO.write (activeDream, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDream () {
		ArrayList<PixelSetRequest> reqs;
		synchronized (setRequests) {
			reqs = (ArrayList<PixelSetRequest>)setRequests.clone();
			setRequests.clear();
		}
		synchronized (activeDream) {
			for (PixelSetRequest req : reqs) {
				if (req.x >= activeDream.getWidth() || req.y >= activeDream.getHeight() || req.x < 0 || req.y < 0) continue;
				activeDream.setRGB(req.x, req.y, req.col);
			}
		}
		viewer.repaint();
	}
	
	public void start () {
		fetchImages (numNodes*3, searchPrompt);
		activeDream = new BufferedImage (imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		viewer = new DreamViewer (this);
		
		String[] dets = readNodeDeterminators ();
		isOperating = true;
		for (int i = 0; i < numNodes; i++) {
			Node node = new Node (this, ((dets!=null && dets.length > i && !rebuildNodes) ? dets[i] : NodeGenerator.makeInstructionSequence()), images.get(random.nextInt(images.size())));
			activeNodes.add(node);
		}
		while (iterationNum < numIts && isOperating) {
			updateDream();
			System.out.println ("Iteration " + iterationNum + " completed.");
			iterationNum++;
			
			if (random.nextBoolean()) {
				Node n = activeNodes.get(random.nextInt(activeNodes.size()));
				synchronized (n.buffer) {
					n.buffer = images.get(random.nextInt(images.size()));
					n.bufferCursor = new Cursor (0,0);
				}
			}
			
			try {Thread.sleep(50);} catch (InterruptedException e) {}
		}
		isOperating = false;
		writeNodeDeterminators ();
		updateDream ();
		exportDream ();
		viewer.setVisible(false);
		viewer.dispose();
	}

}
