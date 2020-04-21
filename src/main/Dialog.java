package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Dialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JCheckBox nodesToggle;
	JFormattedTextField numNodes;
	JFormattedTextField numIts;
	JFormattedTextField imageSize;
	JFormattedTextField windowSize;
	JTextField searchPrompt;
	
	public Dialog () {
		super ();
		nodesToggle = new JCheckBox ("Rebuild nodes", false);
		numNodes = new JFormattedTextField (NumberFormat.getIntegerInstance());
		numNodes.setText("8");
		numIts = new JFormattedTextField (NumberFormat.getIntegerInstance());
		numIts.setText("500");
		imageSize = new JFormattedTextField (NumberFormat.getIntegerInstance());
		imageSize.setText("4096");
		windowSize = new JFormattedTextField (NumberFormat.getIntegerInstance());
		windowSize.setText("900");
		searchPrompt = new JTextField ();
		
		setLayout(new GridLayout(13, 1));
		
		add (new JLabel("Set parameters for calculation"));
		add (nodesToggle);
		add (new JLabel("Number of nodes:"));
		add (numNodes);
		add (new JLabel("Number of iterations:"));
		add (numIts);
		add (new JLabel("Canvas size:"));
		add (imageSize);
		add (new JLabel("Viewer window size:"));
		add (windowSize);
		add (new JLabel("Search prompt:"));
		add (searchPrompt);
		
		JButton ok = new JButton ("Start calculation");
		ok.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				output[0] = nodesToggle.isSelected();
				output[1] = Integer.parseInt(numNodes.getText());
				output[2] = Integer.parseInt(numIts.getText());
				output[3] = Integer.parseInt(imageSize.getText());
				output[4] = Integer.parseInt(windowSize.getText());
				output[5] = searchPrompt.getText();
				synchronized (isDone) {
					isDone = true;
				}
				dispose();
			}
		});
		
		add (ok);
		
		setSize (200, 350);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		
		setVisible (true);
	}
	
	Object[] output = new Object[6];
	Boolean isDone = false;
	
	public Object[] run () {
		while (!isDone) { try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}};
		return output;
	}
}
