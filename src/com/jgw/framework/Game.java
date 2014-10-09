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
	private ArrayList<Target> ducks;

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

	// Target
	private BufferedImage targetImgCurrent;
	private BufferedImage targetImg1, targetImg2, targetImg3, targetImg4, targetImg5;
	private BufferedImage targetImg6, targetImg7, targetImg8, targetImg9, targetImg10;
	private BufferedImage targetImg11, targetImg12, targetImg13;

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
		font = new Font("monospaced", Font.BOLD, 28);

		ducks = new ArrayList<Target>();

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

			URL duckImg1Url = this.getClass().getResource("data/profile_e.jpg");
			targetImg1 = ImageIO.read(duckImg1Url);

			URL duckImg2Url = this.getClass().getResource("data/profile_f.jpg");
			targetImg2 = ImageIO.read(duckImg2Url);

			URL duckImg3Url = this.getClass().getResource("data/profile_g.jpg");
			targetImg3 = ImageIO.read(duckImg3Url);

			URL duckImg4Url = this.getClass().getResource("data/profile_j.jpg");
			targetImg4 = ImageIO.read(duckImg4Url);

			URL duckImg5Url = this.getClass().getResource("data/profile_m.jpg");
			targetImg5 = ImageIO.read(duckImg5Url);

			URL duckImg6Url = this.getClass().getResource("data/profile_w.jpg");
			targetImg6 = ImageIO.read(duckImg6Url);

			URL duckImg7Url = this.getClass().getResource("data/char_link.png");
			targetImg7 = ImageIO.read(duckImg7Url);
			
			URL duckImg8Url = this.getClass().getResource("data/char_mario.png");
			targetImg8 = ImageIO.read(duckImg8Url);
			
			URL duckImg9Url = this.getClass().getResource("data/char_megaman.png");
			targetImg9 = ImageIO.read(duckImg9Url);
			
			URL duckImg10Url = this.getClass().getResource("data/char_sackboy.png");
			targetImg10 = ImageIO.read(duckImg10Url);
			
			URL duckImg11Url = this.getClass().getResource("data/char_samus.png");
			targetImg11 = ImageIO.read(duckImg11Url);
			
			URL duckImg12Url = this.getClass().getResource("data/char_simon.png");
			targetImg12 = ImageIO.read(duckImg12Url);
			
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
		Target.lastDuckTime = 0;

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
		if (System.nanoTime() - Target.lastDuckTime > Target.timeBetweenDucks) {
			// Random target
			switch (rand.nextInt(13)+1) {
			case 1:
				targetImgCurrent = targetImg1;
				break;
			case 2:
				targetImgCurrent = targetImg2;
				break;
			case 3:
				targetImgCurrent = targetImg3;
				break;
			case 4:
				targetImgCurrent = targetImg4;
				break;
			case 5:
				targetImgCurrent = targetImg5;
				break;
			case 6:
				targetImgCurrent = targetImg6;
				break;
			case 7:
				targetImgCurrent = targetImg7;
				break;
			case 8:
				targetImgCurrent = targetImg8;
				break;
			case 9:
				targetImgCurrent = targetImg9;
				break;
			case 10:
				targetImgCurrent = targetImg10;
				break;
			case 11:
				targetImgCurrent = targetImg11;
				break;
			case 12:
				targetImgCurrent = targetImg12;
				break;
			default:
				targetImgCurrent = targetImg1;
				break;
			}
			// Create new duck and add to array
			ducks.add(new Target(Target.duckLines[Target.nextDuckLines][0]
					+ rand.nextInt(200), Target.duckLines[Target.nextDuckLines][1],
					Target.duckLines[Target.nextDuckLines][2],
					Target.duckLines[Target.nextDuckLines][3], targetImgCurrent));

			// Here we increase nextDuckLines so the next duck will be created
			// on the next line
			Target.nextDuckLines++;
			if (Target.nextDuckLines >= Target.duckLines.length) {
				Target.nextDuckLines = 0;
			}

			Target.lastDuckTime = System.nanoTime();
		}

		// Update all ducks
		for (int i = 0; i < ducks.size(); i++) {
			// Move ducks
			ducks.get(i).Update();

			// Checks if duck leaves screen and then removes
			if (ducks.get(i).x < 0 - targetImgCurrent.getWidth()) {
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
/*
					if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27,
							30).contains(mousePosition)
							|| new Rectangle(ducks.get(i).x + 30,
									ducks.get(i).y + 30, 88, 25)
									.contains(mousePosition)) {
*/
					if (new Rectangle(ducks.get(i).x, ducks.get(i).y, 100, 150).contains(mousePosition)) {
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

		// When X ducks runaway, the game ends
		if (runawayDucks > 50) {
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
		
		font = new Font("monospaced", Font.BOLD, 28);
		g2d.setFont(font);
		g2d.setColor(Color.darkGray);
		g2d.drawString("RUNAWAY: " + runawayDucks, 10, 25 + 2);
		g2d.drawString("KILLS: " + killedDucks, 250, 25 + 2);
		g2d.drawString("SHOTS: " + shoots, 450, 25 + 2);
		g2d.drawString("SCORE: " + score, 650, 25 + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString("RUNAWAY: " + runawayDucks, 10 + 2, 25);
		g2d.drawString("KILLS: " + killedDucks, 250 + 2, 25);
		g2d.drawString("SHOTS: " + shoots, 450 + 2, 25);
		g2d.drawString("SCORE: " + score, 650 + 2, 25);
	}

	/**
	 * Draw Game Over screen
	 * 
	 * @param g2d			Graphics2D
	 * @param mousePosition	Current mouse position
	 */
	public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
		Draw(g2d, mousePosition);
		
		font = new Font("monospaced", Font.BOLD, 72);
		g2d.setFont(font);

		// The first text is used for shade
		tmpStr = "Game Over";
		g2d.setColor(Color.BLACK);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.40) + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 2, (int) (Framework.frameHeight * 0.40));

		tmpStr = "Press space or enter to restart";
		g2d.setColor(Color.BLACK);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.60) + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 2, (int) (Framework.frameHeight * 0.60));
}
}
