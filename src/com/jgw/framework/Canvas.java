package com.jgw.framework;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Create JPanel so we can draw and listen for keyboard and mouse events
 */

public abstract class Canvas extends JPanel implements KeyListener,
		MouseListener {
	// Keyboard states (whether keyboard keys are down or not)
	private static boolean[] keyboardState = new boolean[525];

	// Mouse states (whether mouse buttons are down or not)
	private static boolean[] mouseState = new boolean[3];

	public Canvas() {
		// Double Buffer draws on screen
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setBackground(Color.BLACK);

		// If you draw your own mouse cursor or just don't need it, set to true
		if (true) {
			BufferedImage blankCursorImg = new BufferedImage(16, 16,
					BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit()
					.createCustomCursor(blankCursorImg, new Point(0, 0), null);
			this.setCursor(blankCursor);
		}

		// Adds keyboard listener to JPanel
		this.addKeyListener(this);

		// Adds mouse listener to JPanel
		this.addMouseListener(this);
	}
	
	// This method will be overridden in Framework.java and is used to draw on screen
	public abstract void Draw(Graphics2D g2d);
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g2d);
		Draw(g2d);
	}
	
	
	/**
	 * Keyboard
	 * <p>Is key down?
	 * 
	 * @param key	Number of key to check state
	 * @return		true if key is down, false if up
	 */
	public static boolean keyboardKeyState(int key) {
		return keyboardState[key];
	}

	// Keyboard methods
	@Override
	public void keyPressed(KeyEvent e) {
		keyboardState[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyboardState[e.getKeyCode()] = false;
		keyReleasedFramework(e);
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public abstract void keyReleasedFramework(KeyEvent e);
	
	
	/**
	 * Mouse
	 * <p>Is mouse button clicked?
	 * <p>Parameter "button" can be "MouseEvent.BUTTON1" for mouse button #1, etc.
	 * 
	 * @param button	Number of mouse button to check state
	 * @return			true if button is down, false if up
	 */
	public static boolean mouseButtonState(int button) {
		return mouseState[button - 1];
	}
	
	// Sets mouse button status
	private void mouseKeyStatus(MouseEvent e, boolean status) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseState[0] = status;
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			mouseState[1] = status;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			mouseState[2] = status;
		}
	}
	
	// Mouse methods
	@Override
	public void mousePressed(MouseEvent e) {
		mouseKeyStatus(e, true);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseKeyStatus(e, false);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
