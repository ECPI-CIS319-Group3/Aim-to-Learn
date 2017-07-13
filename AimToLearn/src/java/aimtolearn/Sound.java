package aimtolearn;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public enum Sound {
	MENU_MOVE("menu_move.wav"),
	MENU_SELECT("menu_select.wav"),
	SHIELD_ACTIVE("shield.wav"),
	SHOOT("shoot.wav"),
	SHOT_CHARGE("shot_charge.wav"),
	ANSWER_EXPLOSION("answer_hit_explosion.wav"),
	SHIP_HIT("ship_hit_explosion.wav"),
	SHIELD_HIT("answer_hit_shield.wav");

	private static LineListener closeListener;
	private Clip clip;

	Sound(String fileName) {
		clip = Constants.getSound(fileName);
	//	clip.addLineListener(CloseListener.self);
	}

	public void play() {
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
		clip.start();
	}

	static class CloseListener implements LineListener {

		static LineListener self = new CloseListener();

		public void update(LineEvent e){
			if (e.getType() == LineEvent.Type.STOP) e.getLine().close();
		}
	}

}
