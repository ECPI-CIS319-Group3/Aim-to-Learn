package aimtolearn;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class AnimatedSprite {

	private Image[] frames;

	public AnimatedSprite(BufferedImage spriteSheet, int frameCount) {
		int frameHeight = spriteSheet.getHeight(null);
		int frameWidth = spriteSheet.getWidth(null) / frameCount;

		this.frames = new Image[frameCount];

		for (int i=0; i<frameCount; i++) {
			this.frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
		}
	}

}
