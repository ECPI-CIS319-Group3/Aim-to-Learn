package aimtolearn;

import javax.sound.sampled.*;
import java.applet.AudioClip;

public enum Sound {
	MENU_MOVE("menu_move.wav"),
	MENU_SELECT("menu_select.wav"),
	SHIELD_ACTIVE("shield.wav"),
	SHOOT("shoot.wav"),
	SHOT_CHARGE("shot_charge.wav"),
	ANSWER_EXPLOSION("answer_hit_explosion.wav"),
	SHIP_HIT("ship_hit_explosion.wav"),
	SHIELD_HIT("answer_hit_shield.wav");

	private static int volume = 100;

	private static LineListener closeListener;
	private Clip clip;
	private FloatControl gainControl;
	private AudioClip appletClip;

	Sound(String fileName) {
		clip = Constants.getSound(fileName);
		gainControl = ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN));
		//	appletClip = Applet.newAudioClip(Constants.class.getResource("wav/"+fileName));
	//	clip.addLineListener(CloseListener.self);
	}

	public void play() {
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
		clip.start();
	//	appletClip.play();
	}

	public static void setVolume(int percentVolume) {
		volume = percentVolume;
		float gain = toGain(percentVolume);

		for (Sound sound : values())
			sound.gainControl.setValue(gain);
	}

	public static int getVolume() {
		return volume;
	}

	private static float toGain(int percentVolume) {
		double percentVol = percentVolume / 100.0;
		return (float) (Math.log(percentVol) / Math.log(10.0) * 20.0);
	}

	static class CloseListener implements LineListener {

		static LineListener self = new CloseListener();

		public void update(LineEvent e){
			if (e.getType() == LineEvent.Type.STOP) e.getLine().close();
		}
	}

}
