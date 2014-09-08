package com.jgw.framework;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties
 */

public class Window extends JFrame {
	public Window() {
		// Title
		this.setTitle("My Duck Hunt");

		// true = full screen
		if (true) {
			// Disables decorations
			this.setUndecorated(true);
			// Fullscreen
			this.setExtendedState(this.MAXIMIZED_BOTH);
		} else {
			// Screen size
			this.setSize(1024, 768);
			// Center window
			this.setLocationRelativeTo(null);
			this.setResizable(false);
		}

		// Exit app when window is closed
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creates instance of Framework.java that extends Canvas.java and puts
		// it on the frame
		this.setContentPane(new Framework());

		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Use event dispatch thread to build UI for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Window();
			}
		});
	}
}