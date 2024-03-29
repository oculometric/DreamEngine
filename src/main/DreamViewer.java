package main;

import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class DreamViewer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DreamViewer (DreamEngine de) {
		super ("DreamEngine Viewer");
		engine = de;
		this.setSize(engine.windowSize,engine.windowSize);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowListener () {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				engine.isOperating = false;
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
		});
		this.setVisible(true);
	}
	
	DreamEngine engine;
	
	@Override
	public void paint (Graphics g) {
		g.drawImage (engine.activeDream, 0, 0, engine.windowSize, engine.windowSize, 0, 0, engine.imageSize, engine.imageSize, null);
	}
}
