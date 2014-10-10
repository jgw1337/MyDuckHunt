package com.jgw.framework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Target {
	// How much time passes before creating a new target?
	public static long timeBetweenTargets = Framework.secInNanosec / 2;
	
	// Last time a target was created
	public static long lastTargetTime = 0;
	
	/**
	 * Target info
	 * <li>Starting location
	 * <li>Speed
	 * <li>Points for shooting the target
	 */
	public static int[][] targetLines = {
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.25), -2, 20},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.40), -3, 30},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.55), -5, 40},
		{Framework.frameWidth, (int) (Framework.frameHeight * 0.70), -7, 50},
	};
	
	// Which is the next target
	public static int nextTargetLines = 0;
	
	// Target coords
	public int x, y;
	
	// Target speed and direction
	public int speed;
	
	// Points for shooting target
	public int score;
	private String tmpStr;
	
	// Target image
	private BufferedImage targetImg;
	
	/**
	 * Create new target
	 * 
	 * @param x			Starting x-coord
	 * @param y			Starting y-coord
	 * @param speed		Target speed
	 * @param score		How many points
	 * @param targetImg	Target image
	 */
	public Target(int x, int y, int speed, int score, BufferedImage targetImg) {
		this.x = x;
		this.y = y;
		
		this.speed = speed;
		
		this.score = score;
		
		this.targetImg = targetImg;
	}
	
	/**
	 * Move target
	 */
	public void Update() {
		x += speed;
	}
	
	/**
	 * Draw target
	 * 
	 * @param g2d	Graphics2D
	 */
	public void Draw(Graphics2D g2d) {
		g2d.drawImage(targetImg, x, y, null);
	}

	/**
	 * Draw hit score
	 * 
	 * @param g2d	Graphics2D
	 */
	public void DrawHitScore(Graphics2D g2d) {
		tmpStr = Integer.toString(score);
		g2d.setColor(Color.BLACK);
		g2d.drawString(tmpStr, x + 5, y + targetImg.getHeight() - 5 + 2);
		g2d.setColor(Color.WHITE);
		g2d.drawString(tmpStr, x + 5 - 2, y + targetImg.getHeight() - 5);
	}
}
