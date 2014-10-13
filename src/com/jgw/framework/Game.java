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

	// Array of targets
	private ArrayList<Target> targets;

	// How many targets flew away?
	private int runawayTargets;

	// How many targets killed?
	private int killedTargets;

	// For each target killed, the player gets points
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
	private BufferedImage[] targetImg = new BufferedImage[15];

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

		targets = new ArrayList<Target>();

		runawayTargets = 0;
		killedTargets = 0;
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

			URL targetImg0Url = this.getClass().getResource("data/profile_e_2.jpg");
			targetImg[0] = ImageIO.read(targetImg0Url);

			URL targetImg1Url = this.getClass().getResource("data/profile_e.jpg");
			targetImg[1] = ImageIO.read(targetImg1Url);

			URL targetImg2Url = this.getClass().getResource("data/profile_f.jpg");
			targetImg[2] = ImageIO.read(targetImg2Url);

			URL targetImg3Url = this.getClass().getResource("data/profile_g.jpg");
			targetImg[3] = ImageIO.read(targetImg3Url);

			URL targetImg4Url = this.getClass().getResource("data/profile_j.jpg");
			targetImg[4] = ImageIO.read(targetImg4Url);

			URL targetImg5Url = this.getClass().getResource("data/profile_m.jpg");
			targetImg[5] = ImageIO.read(targetImg5Url);

			URL targetImg6Url = this.getClass().getResource("data/profile_w.jpg");
			targetImg[6] = ImageIO.read(targetImg6Url);

			URL targetImg7Url = this.getClass().getResource("data/char_link.png");
			targetImg[7] = ImageIO.read(targetImg7Url);
			
			URL targetImg8Url = this.getClass().getResource("data/char_mario.png");
			targetImg[8] = ImageIO.read(targetImg8Url);
			
			URL targetImg9Url = this.getClass().getResource("data/char_megaman.png");
			targetImg[9] = ImageIO.read(targetImg9Url);
			
			URL targetImg10Url = this.getClass().getResource("data/char_sackboy.png");
			targetImg[10] = ImageIO.read(targetImg10Url);
			
			URL targetImg11Url = this.getClass().getResource("data/char_samus.png");
			targetImg[11] = ImageIO.read(targetImg11Url);
			
			URL targetImg12Url = this.getClass().getResource("data/char_simon.png");
			targetImg[12] = ImageIO.read(targetImg12Url);
			
			URL targetImg13Url = this.getClass().getResource("data/profile_i.jpg");
			targetImg[13] = ImageIO.read(targetImg13Url);
			
			URL targetImg14Url = this.getClass().getResource("data/profile_trinity.jpg");
			targetImg[14] = ImageIO.read(targetImg14Url);
			
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
		// Removes all targets
		targets.clear();

		// Set lastTargetTime to zero
		Target.lastTargetTime = 0;

		runawayTargets = 0;
		killedTargets = 0;
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
		// Create new target, if it's time, and add to array
		if (System.nanoTime() - Target.lastTargetTime > Target.timeBetweenTargets) {
			// Random target

			targetImgCurrent = targetImg[rand.nextInt(targetImg.length)];
			// Create new target and add to array
			targets.add(new Target(Target.targetLines[Target.nextTargetLines][0]
					+ rand.nextInt(200), Target.targetLines[Target.nextTargetLines][1],
					Target.targetLines[Target.nextTargetLines][2],
					Target.targetLines[Target.nextTargetLines][3], targetImgCurrent));

			// Here we increase nextTargetLines so the next target will be created
			// on the next line
			Target.nextTargetLines++;
			if (Target.nextTargetLines >= Target.targetLines.length) {
				Target.nextTargetLines = 0;
			}

			Target.lastTargetTime = System.nanoTime();
		}

		// Update all targets
		for (int i = 0; i < targets.size(); i++) {
			// Move targets
			targets.get(i).Update();

			// Checks if target leaves screen and then removes
			if (targets.get(i).x < 0 - targetImgCurrent.getWidth()) {
				targets.remove(i);
				runawayTargets++;
			}
		}

		// Does player shoot?
		if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
			// Check if player can shoot again
			if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
				shoots++;

				// Check if any targets were hit
				for (int i = 0; i < targets.size(); i++) {
					// Check if mouse was over target's head or body when player
					// shot
/*
					if (new Rectangle(targets.get(i).x + 18, targets.get(i).y, 27,
							30).contains(mousePosition)
							|| new Rectangle(targets.get(i).x + 30,
									targets.get(i).y + 30, 88, 25)
									.contains(mousePosition)) {
*/
					if (new Rectangle(targets.get(i).x, targets.get(i).y, 100, 150).contains(mousePosition)) {
						killedTargets++;
						score += targets.get(i).score;

						// Remove targets if shot
						targets.remove(i);

						// A target was shot (one kill per bullet) so leave the
						// FOR loop
						break;
					}
				}

				lastTimeShoot = System.nanoTime();
			}
		}

		// When X targets runaway, the game ends
		if (runawayTargets > 20) {
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
		
		// Draw all the targets
		for (int i = 0; i < targets.size(); i++) {
			targets.get(i).Draw(g2d);
			targets.get(i).DrawHitScore(g2d);
		}
		
		g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
		
		g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
		
		font = new Font("monospaced", Font.BOLD, 28);
		g2d.setFont(font);
		g2d.setColor(Color.darkGray);
		g2d.drawString("RUNAWAY: " + runawayTargets, 10, 25 + 2);
		g2d.drawString("KILLS: " + killedTargets, 250, 25 + 2);
		g2d.drawString("SHOTS: " + shoots, 450, 25 + 2);
		g2d.drawString("SCORE: " + score, 650, 25 + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString("RUNAWAY: " + runawayTargets, 10 + 2, 25);
		g2d.drawString("KILLS: " + killedTargets, 250 + 2, 25);
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
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.45) + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 2, (int) (Framework.frameHeight * 0.45));

		tmpStr = "Press space or enter to restart";
		g2d.setColor(Color.BLACK);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2, (int) (Framework.frameHeight * 0.55) + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, Framework.frameWidth/2 - g2d.getFontMetrics().stringWidth(tmpStr)/2 + 2, (int) (Framework.frameHeight * 0.55));
}
}
