package main;

import java.awt.Color;
import java.awt.Image;
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
import javax.swing.JOptionPane;

public class DreamEngine {

	public static int dreamCycleLength = 5;
	public static int searchCycleLength = 10;
	int iterationNum = 0;
	
	public static void main(String[] args) {
		String s = (String)JOptionPane.showInputDialog("Enter a search prompt:");
		DreamEngine engine = new DreamEngine ();
		engine.start(s);
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
			BufferedImage[] imgs = searcher.images(links.get(random.nextInt(links.size())));
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
	
	public String makeInstructionSequence () {
		String is = "";
		int numInstrs = random.nextInt(100);
		for (int i = 0; i < numInstrs; i++) {
			is += random.nextInt(18);
			is += ";";
		}
		return is;
	}
	
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
			return (String[]) inp.toArray();
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
				activeDream.setRGB(req.x, req.y, req.col);
			}
		}
		viewer.repaint();
	}
	
	
	public void start (String prompt) {
		fetchImages (20, prompt);
		activeDream = new BufferedImage (1024, 1024, BufferedImage.TYPE_INT_RGB);
		viewer = new DreamViewer (this);
		
		String[] dets = readNodeDeterminators ();
		isOperating = true;
		for (int i = 0; i < 8; i++) {
			Node node = new Node (this, ((dets!=null && dets.length > i) ? dets[i] : makeInstructionSequence()), images.get(random.nextInt(images.size())));
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
