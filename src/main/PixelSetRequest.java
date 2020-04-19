package main;

import java.awt.Color;

public class PixelSetRequest {
	
	public int x;
	public int y;
	public Color col;

	public PixelSetRequest (int ix, int iy, Color icol) {
		x = ix;
		y = iy;
		col = icol;
	}
}
