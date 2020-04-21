package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.Color;
import java.awt.image.BufferedImage;

/*
 * Instruction Spec:
 * 
 * 00 - Write from buffer to dream
 * 01 - Read from dream to buffer
 * 
 * 02 - Toggle state value
 * 03 - Advance state cursor
 * 04 - Regress state cursor
 * 05 - Reset state cursor
 * 06 - Reset state under cursor
 * 07 - Skip next instructions if state under cursor is true
 * 08 - End skip
 * 
 * 09 - Read buffer pixel to states
 * 10 - Write states to buffer pixel
 * 
 * 11 - Increment buffer x cursor
 * 12 - Decrement buffer x cursor
 * 13 - Increment buffer y cursor
 * 14 - Decrement buffer y cursor
 * 15 - Increment dream x cursor
 * 16 - Decrement dream x cursor
 * 17 - Increment dream y cursor
 * 18 - Decrement dream y cursor
 * 
 * 19 - Begin area copy operation
 * 20 - End area copy operation
 * 
 * 21 - Repeat next instructions based on value of states
 * 22 - End repeat
 * 
 * 23 - Copy pixels in a radius around the cursor
 * 
 * 24 - Jump by an offset determined by the states TODO
 */

public class Node {
	public ArrayList<String> determinator;
	
	public BufferedImage buffer;
	private boolean[] states = {false, false, false, false, false, false, false, false};
	
	private DreamEngine engine;
	private Random random = new Random ();
	
	public Cursor bufferCursor;
	private Cursor dreamCursor;
	
	private Cursor bufferCursorFreeze = null;
	private Cursor dreamCursorFreeze = null;
	private int stateCursor = 0;
	
	public Node (DreamEngine en, String det, BufferedImage im) {
		
		bufferCursor = new Cursor (0,0);
		dreamCursor = new Cursor (random.nextInt(4096), random.nextInt(4096));
		
		engine = en;
		buffer = im;
		determinator = new ArrayList<String> (Arrays.asList(det.split(";")));
		for (int i = 0; i < determinator.size(); i++) if (determinator.get(i).length() < 1) determinator.remove(i);
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

	private void executeDeterminator () {
		boolean shouldSkipNext = false;
		int loc = 0;
		int loopbackLoc = -1;
		int loopItsToDo = -1;
		while (loc < determinator.size()) {
			if (shouldSkipNext) continue;
			String si = determinator.get(loc);
			int instr;
			try {
				instr = Integer.parseInt(si);
			} catch (NumberFormatException e) {
				System.out.println ("Error on instruction " + si + " during execution.");
				continue;
			}
			switch (instr) {
			case 0:
				engine.setPixel(dreamCursor.x, dreamCursor.y, buffer.getRGB(bufferCursor.x, bufferCursor.y));
				break;
			case 1:
				buffer.setRGB(bufferCursor.x, bufferCursor.y, engine.getPixel(dreamCursor.x, dreamCursor.y));
				break;
			case 2:
				states[stateCursor] = !states[stateCursor];
				break;
			case 3:
				stateCursor++;
				if (stateCursor > 7) stateCursor = 0;
				break;
			case 4:
				stateCursor--;
				if (stateCursor < 0) stateCursor = 7;
				break;
			case 5:
				stateCursor = 0;
				break;
			case 6:
				states[stateCursor] = false;
				break;
			case 7:
				shouldSkipNext = states[stateCursor];
				break;
			case 8:
				shouldSkipNext = false;
			case 9:
				Color pixel = new Color (buffer.getRGB(bufferCursor.x, bufferCursor.y));
				if (pixel.getRed() > 0) {
					states[0] = true;
					if (pixel.getRed() > 127) {
						states[1] = true;
						if (pixel.getRed() < 255) states[0] = false;
					}
				}
				if (pixel.getGreen() > 0) {
					states[2] = true;
					if (pixel.getGreen() > 127) {
						states[3] = true;
						if (pixel.getGreen() < 255) states[2] = false;
					}
				}
				if (pixel.getBlue() > 0) {
					states[4] = true;
					if (pixel.getBlue() > 127) {
						states[5] = true;
						if (pixel.getBlue() < 255) states[4] = false;
					}
				}
				break;
				// FIXME: Two remaining values?
			case 10:
				int r = 0;
				int g = 0;
				int b = 0;
				if (states[1]) {
					r = 128;
					if (states[0]) r = 255;
				} else if (states[0]) {
					r = 64;
				}
				if (states[3]) {
					g = 128;
					if (states[2]) g = 255;
				} else if (states[2]) {
					g = 64;
				}
				if (states[5]) {
					b = 128;
					if (states[4]) b = 255;
				} else if (states[4]) {
					b = 64;
				}
				Color pix = new Color (r,g,b);
				buffer.setRGB(bufferCursor.x, bufferCursor.y, pix.getRGB());
				break;
			case 15:
				dreamCursor.x++;
				if (dreamCursor.x >= engine.activeDream.getWidth()) dreamCursor.x = 0;
			case 11:
				bufferCursor.x++;
				if (bufferCursor.x >= buffer.getWidth()) bufferCursor.x = 0;
				break;
			case 16:
				dreamCursor.x--;
				if (dreamCursor.x < 0) dreamCursor.x = engine.activeDream.getWidth()-1;
			case 12:
				bufferCursor.x--;
				if (bufferCursor.x < 0) bufferCursor.x = buffer.getWidth()-1;
				break;
			case 17:
				dreamCursor.y++;
				if (dreamCursor.y >= engine.activeDream.getHeight()) dreamCursor.y = 0;
			case 13:
				bufferCursor.y++;
				if (bufferCursor.y >= buffer.getHeight()) bufferCursor.y = 0;
				break;
			case 18:
				dreamCursor.y--;
				if (dreamCursor.y < 0) dreamCursor.y = engine.activeDream.getHeight()-1;
			case 14:
				bufferCursor.y--;
				if (bufferCursor.y < 0) bufferCursor.y = buffer.getHeight()-1;
				break;
			case 19:
				bufferCursorFreeze = bufferCursor;
				dreamCursorFreeze = dreamCursor;
				break;
			case 20:
				if (bufferCursorFreeze == null) break;
				copyArea (bufferCursor, bufferCursorFreeze, dreamCursorFreeze);
				bufferCursorFreeze = null;
				dreamCursorFreeze = null;
				break;
			case 21:
				if (loopbackLoc == -1) {
					int i = statesToInt ();
					loopbackLoc = loc+1;
					loopItsToDo = i;
				}
				break;
			case 22:
				if (loopbackLoc == -1) break;
				loopItsToDo--;
				if (loopItsToDo > 0) loc = loopbackLoc;
				else {
					loopItsToDo = -1;
					loopbackLoc = -1;
				}
				break;
			case 23:
				int radius = statesToInt();
				copyArea (new Cursor (bufferCursor.x-radius, bufferCursor.y-radius), new Cursor (bufferCursor.x+radius, bufferCursor.y+radius), new Cursor (dreamCursor.x-radius, dreamCursor.y-radius));
			}
			loc++;
		}
	}

	private int statesToInt() {
		int r = 0;
		int a = 1;
		for (boolean state : states) {
			if (state) r += a;
			a *= 2;
		}
		return r;
	}

	private void copyArea(Cursor c1, Cursor c2, Cursor ps) {
		Cursor start = new Cursor (Math.min (c1.x, c2.x), Math.min (c1.y, c2.y));
		int xex = Math.abs(c1.x - c2.x);
		int yex = Math.abs(c1.y - c2.y);
		for (int i = 0; i < xex; i++) {
			for (int j = 0; j < yex; j++) {
				int ox = start.x + i;
				int oy = start.y + j;
				int tx = ps.x + i;
				int ty = ps.y + j;
				if (ox < 0 || oy < 0 || tx < 0 || ty < 0) continue;
				if (ox >= buffer.getWidth() || oy >= buffer.getHeight() || tx >= engine.activeDream.getWidth() || ty >= engine.activeDream.getHeight()) continue;
				engine.setPixel(tx, ty, buffer.getRGB(ox, oy));
			}
		}
	}
}
