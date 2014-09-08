package com.jgw.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Actual Game
 */

public class Game {
	// Random number
	private Random rand;

	// Font
	private Font font;

	// Array of ducks
	private ArrayList<Duck> ducks;

	// How many ducks flew away?
	private int runawayDucks;

	// How many ducks killed?
	private int killedDucks;

	// For each ducked killed, the player gets points
	private int score;

	// How many bullets player shot
	private int shoots;

	// Last time player shot
	private long lastTimeShoot;

	// Time that must elapsed between shots
	private long timeBetweenShots;

	// Temporary string (placeholder)
	private String tmpStr;

	// Background
	private BufferedImage backgroundImg;

	// Bottom grass
	private BufferedImage grassImg;

	// Duck
	private BufferedImage duckImg;

	// Gun sight
	private BufferedImage sightImg;

	// Middle width of sight image
	private int sightImgMiddleWidth;

	// Middle height of sight image
	private int sightImgMiddleHeight;

	public Game() {
		Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

		Thread threadForInitGame = new Thread() {
			@Override
			public void run() {
				// Sets variables and objects for game
				Initialize();
				// Load game files (images, sounds, etc.)
				LoadContent();

				Framework.gameState = Framework.GameState.PLAYING;
			}
		};
		threadForInitGame.start();
	}

	// Sets variables and objects for game
	private void Initialize() {
		rand = new Random();
		font = new Font("monospaced", Font.BOLD, 18);

		ducks = new ArrayList<Duck>();

		runawayDucks = 0;
		killedDucks = 0;
		score = 0;
		shoots = 0;

		lastTimeShoot = 0;
		timeBetweenShots = Framework.secInNanosec / 3;
	}

	// Load game files (images, sounds, etc.)
	private void LoadContent() {
		try {
			URL backgroundImgUrl = this.getClass().getResource(
					"data/background.jpg");
			backgroundImg = ImageIO.read(backgroundImgUrl);

			URL grassImgUrl = this.getClass().getResource("data/grass.png");
			grassImg = ImageIO.read(grassImgUrl);

			URL duckImgUrl = this.getClass().getResource("data/duck.png");
			duckImg = ImageIO.read(duckImgUrl);

			URL sightImgUrl = this.getClass().getResource("data/sight.png");
			sightImg = ImageIO.read(sightImgUrl);
			sightImgMiddleWidth = sightImg.getWidth() / 2;
			sightImgMiddleHeight = sightImg.getHeight() / 2;
		} catch (IOException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// Restart (resets some variables)
	public void RestartGame() {
		// Removes all ducks
		ducks.clear();

		// Set lastDuckTime to zero
		Duck.lastDuckTime = 0;

		runawayDucks = 0;
		killedDucks = 0;
		score = 0;
		shoots = 0;

		lastTimeShoot = 0;
	}

	/**
	 * <p>
	 * Update game logic
	 * 
	 * @param gameTime
	 *            gameTime of the game
	 * @param mousePosition
	 *            current mouse position
	 */
	public void updateGame(long gameTime, Point mousePosition) {
		// Create new duck, if it's time, and add to array
		if (System.nanoTime() - Duck.lastDuckTime > Duck.timeBetweenDucks) {
			// Create new duck and add to array
			ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0]
					+ rand.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1],
					Duck.duckLines[Duck.nextDuckLines][2],
					Duck.duckLines[Duck.nextDuckLines][3], duckImg));

			// Here we increase nextDuckLines so the next duck will be created
			// on the next line
			Duck.nextDuckLines++;
			if (Duck.nextDuckLines >= Duck.duckLines.length) {
				Duck.nextDuckLines = 0;
			}

			Duck.lastDuckTime = System.nanoTime();
		}

		// Update all ducks
		for (int i = 0; i < ducks.size(); i++) {
			// Move ducks
			ducks.get(i).Update();

			// Checks if duck leaves screen and then removes
			if (ducks.get(i).x < 0 - duckImg.getWidth()) {
				ducks.remove(i);
				runawayDucks++;
			}
		}

		// Does player shoot?
		if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
			// Check if player can shoot again
			if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
				shoots++;

				// Check if any ducks were hit
				for (int i = 0; i < ducks.size(); i++) {
					// Check if mouse was over duck's head or body when player
					// shot
					if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27,
							30).contains(mousePosition)
							|| new Rectangle(ducks.get(i).x + 30,
									ducks.get(i).y + 30, 88, 25)
									.contains(mousePosition)) {
						killedDucks++;
						score += ducks.get(i).score;

						// Remove ducks if shot
						ducks.remove(i);

						// A duck was shot (one kill per bullet) so leave the
						// FOR loop
						break;
					}
				}

				lastTimeShoot = System.nanoTime();
			}
		}

		// When 200 ducks runaway, the game ends
		if (runawayDucks > 200) {
			Framework.gameState = Framework.GameState.GAMEOVER;

		}
	}

	/**
	 * <p>
	 * Draw game on screen
	 * 
	 * @param g2d
	 *            Graphics2D
	 * @param mousePosition
	 *            current mouse position
	 */
	public void Draw(Graphics2D g2d, Point mousePosition) {
		g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
		
		// Draw all the ducks
		for (int i = 0; i < ducks.size(); i++) {
			ducks.get(i).Draw(g2d);
		}
		
		g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
		
		g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
		
		g2d.setFont(font);
		g2d.setColor(Color.darkGray);
		
		g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
		g2d.drawString("KILLS: " + killedDucks, 160, 21);
		g2d.drawString("SHOOTS: " + shoots, 299, 21);
		g2d.drawString("SCORE: " + score, 440, 21);
	}

	/**
	 * Draw Game Over screen
	 * 
	 * @param g2d			Graphics2D
	 * @param mousePosition	Current mouse position
	 */
	public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
		Draw(g2d, mousePosition);
		
		// The first text is used for shade
		g2d.setColor(Color.BLACK);
		tmpStr = "Game Over";
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.65) + 1);
		tmpStr = "Press space or enter to restart";
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.70) + 1);

		g2d.setColor(Color.RED);
		tmpStr = "Game Over";
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 1, (int) (Framework.frameHeight * 0.65));
		tmpStr = "Press space or enter to restart";
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 1, (int) (Framework.frameHeight * 0.65));
}
}
