package com.jgw.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Framework that controls game (Game.java) that created, updated, and drew it
 * on screen
 */

public class Framework extends Canvas {
	public static int frameWidth, frameHeight;

	// 1 second = 1,000,000,000 nanseconds
	public static final long secInNanosec = 1000000000L;

	// 1 millisecond = 1,000,000 nanoseconds
	public static final long millisecInNanosec = 1000000L;

	// Frames per second
	private final int GAME_FPS = 60;

	// Pause between updates (in nanosecs)
	private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

	// Game States
	public static enum GameState {
		STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED
	}

	// Current Game State
	public static GameState gameState;

	// Elapsed game time (in nanosecs)
	private long gameTime;
	private long lastTime;

	// Actual game
	private Game game;

	// Temporary String (placeholder)
	private String tmpStr;
	private Font font = new Font("monospaced", Font.BOLD, 28);

	
	// Menu Image
	private BufferedImage menuImg;

	public Framework() {
		super();

		gameState = GameState.VISUALIZING;

		// Start game in new thread
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				GameLoop();
			}
		};
		gameThread.start();
	}

	/**
	 * Sets variables and objects
	 * <p>
	 * This method sets variables and objects for this class. Variables and
	 * objects for actual game set in Game.java
	 */
	private void Initialize() {
		// TODO Auto-generated method stub

	}

	/**
	 * Load files (images, sounds, etc.)
	 * <p>
	 * This method loads files for this class. Files for actual game set in
	 * Game.java
	 */
	private void LoadContent() {
		try {
			URL menuImgUrl = this.getClass().getResource("data/menu.jpg");
			menuImg = ImageIO.read(menuImgUrl);
		} catch (IOException ex) {
			Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * In specific intervals of game time (GAME_UPDATE_PERIOD), the game/logic
	 * is updated and then drawn on screen
	 */
	private void GameLoop() {
		// These two vars are used in VISUALIZING game state. We used them to
		// wait some time so we can get the correct frame/window resolution
		long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

		// Vars used for calculating time that defines how long we should put
		// thread to seep to meet GAME_FPS
		long beginTime, timeTaken, timeLeft;

		while (true) {
			beginTime = System.nanoTime();

			switch (gameState) {
			case PLAYING:
				gameTime += System.nanoTime() - lastTime;
				game.updateGame(gameTime, mousePosition());
				lastTime = System.nanoTime();
				break;
			case GAMEOVER:

				break;
			case MAIN_MENU:

				break;
			case OPTIONS:

				break;
			case GAME_CONTENT_LOADING:

				break;
			case STARTING:
				// Sets variables and objects
				Initialize();
				// Load files (images, sounds, etc.)
				LoadContent();
				// Then, set game state to Main Menu
				gameState = GameState.MAIN_MENU;
				break;
			case VISUALIZING:
				// Kludge for Ubuntu OS, this.getWidth() needs to wait a sec
				if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
					frameWidth = this.getWidth();
					frameHeight = this.getHeight();
					// Then, set game state to Start
					gameState = GameState.STARTING;
				} else {
					visualizingTime += System.nanoTime() - lastVisualizingTime;
					lastVisualizingTime = System.nanoTime();
				}
				break;
			}
			
			// Repaint screen
			repaint();
			
			// Calc the time that defines how long to put thread to sleep to meet GAME_FPS
			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / millisecInNanosec;  // in nanosecs

			// If time is less than 10 ms, the put thread to sleep for another 10 ms so another thread can work
			if (timeLeft < 10) {
				timeLeft = 10;  // set a min value
			}
			
			try {
				// Provides the necessary delay and yields control so another thread can work
				Thread.sleep(timeLeft);
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
	}
	
	
	/**
	 * Draw game on screen.  It is called in the repaint() and GameLoop() methods
	 */
	@Override
	public void Draw(Graphics2D g2d) {
		g2d.setFont(font);

		switch (gameState) {
		case PLAYING:
			game.Draw(g2d, mousePosition());
			break;
		case GAMEOVER:
			game.DrawGameOver(g2d, mousePosition());
			break;
		case MAIN_MENU:
			g2d.drawImage(menuImg, 0, 0, frameWidth, frameHeight, null);
			tmpStr = "Use left mouse button to shoot the duck.";
			g2d.drawString(tmpStr, (frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2), (int) (frameHeight * 0.65));
			tmpStr = "Click with left mouse button to start the game.";
			g2d.drawString(tmpStr, (frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2), (int) (frameHeight * 0.70));
			tmpStr = "Press ESC at any time to exit the game.";
			g2d.drawString(tmpStr, (frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2), (int) (frameHeight * 0.75));
			tmpStr = "-jgw-";
			g2d.drawString(tmpStr, 7, frameHeight - 5);
			break;
		case OPTIONS:
			
			break;
		case GAME_CONTENT_LOADING:
			g2d.setColor(Color.WHITE);
			tmpStr = "GAME is LOADING";
			g2d.drawString(tmpStr, (frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2), frameHeight/2);
			break;
		}
	}
	
	
	/**
	 * Starts new game
	 */
	private void newGame() {
		// Set gameTime to zero and lastTime to current time for later calcs
		gameTime = 0;
		lastTime = System.nanoTime();
		
		game = new Game();
	}
	
	
	/**
	 * Restart game
	 * <p>Resets gameTime and calls RestartGame() method of game object so some vars are reset
	 */
	private void restartGame() {
		// Set gameTime to zero and lastTime to current time for later calcs
		gameTime = 0;
		lastTime = System.nanoTime();
		
		game.RestartGame();
		
		// Change game state so game can start
		gameState = GameState.PLAYING;
	}
	
	
	/**
	 * Returns position of mouse in frame/window.
	 * If mouse position is null, then return 0,0 coords
	 * 
	 * @return	Point of mouse coords
	 */
	private Point mousePosition() {
		try {
			Point mp = this.getMousePosition();
			
			if (mp != null) {
				return this.getMousePosition();
			} else {
				return new Point(0, 0);
			}
		} catch (Exception e) {
			return new Point(0, 0);
		}
	}
	
	/**
	 * Keyboard key released method
	 * 
	 * @param e	KeyEvent
	 */
	@Override
	public void keyReleasedFramework(KeyEvent e) {
		switch (gameState) {
		case GAMEOVER:
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} else {
				restartGame();
			}
			break;
		case PLAYING:
		case MAIN_MENU:
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}
			break;
		}
	}
	
	
	/**
	 * Mouse clicked method
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (gameState) {
		case MAIN_MENU:
			if (e.getButton() == MouseEvent.BUTTON1) {
				newGame();
			}
			break;

		default:
			break;
		}
	}
}
