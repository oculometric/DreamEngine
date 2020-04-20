package main;

import java.awt.Frame;
import java.awt.Graphics;

public class DreamViewer extends Frame {
	public DreamViewer (DreamEngine de) {
		super ("DreamEngine Viewer");
		engine = de;
		this.setSize(1024,1024);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	DreamEngine engine;
	
	@Override
	public void paint (Graphics g) {
		g.drawImage (engine.activeDream, 0, 0, 1024, 1024, 0, 0, 1024, 1024, null);
	}
}
