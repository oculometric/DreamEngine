package main;

import java.awt.image.BufferedImage;

public class Data {
	public int type;
	
	public String textData;
	public BufferedImage image;
	
	public Data (String t) {
		textData = t;
		type = 0;
	}
	
	public Data (BufferedImage i) {
		image = i;
		type = 1;
	}
}
