package aimtolearn;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
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
	SHIELD_HIT("answer_hit_shield.wav"),
	BG_MUSIC_V1("bg_music_v1.wav", true),
	BG_MUSIC_V2("bg_music_v2.wav", true);

	private static int masterVolume = 100;
	private static int fxVolume = 100;
	private static int musicVolume = 100;

	private Clip clip;
	private FloatControl gainControl;
	private boolean isMusic;

	public static int STEP_SIZE = 5;

	// called to preload sounds
	public static void init() { values(); }

	Sound(String fileName) {
		this(fileName, false);
	}

	Sound(String fileName, boolean isMusic) {
		this.clip = Constants.getSound(fileName);
		this.gainControl = ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN));
		this.isMusic = isMusic;
	//	clip.addLineListener(CloseListener.self);
	}

	public void play() {
		clip.stop();
		clip.flush();
		clip.setFramePosition(0);
		clip.start();
	}

	public void loop() {
		clip.setLoopPoints(0, -1);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void setMasterVolume(int percentVolume) {
		masterVolume = clampVolume(percentVolume);
		float gain = toGain(masterVolume);
		for (Sound sound : values())
			sound.gainControl.setValue(gain);
	}

	public static void setFxVolume(int percentVolume) {
		fxVolume = clampVolume(percentVolume);
		float gain = toGain(fxVolume);
		for (Sound sound : values()) {
			if (!sound.isMusic)
				sound.gainControl.setValue(gain);
		}
	}

	public static void setMusicVolume(int percentVolume) {
		musicVolume = clampVolume(percentVolume);
		float gain = toGain(musicVolume);
		for (Sound sound : values()) {
			if (sound.isMusic)
				sound.gainControl.setValue(gain);
		}
	}

	private static int clampVolume(int percentVol) {
		return percentVol < 0 ? 0 : percentVol > 150 ? 150 : percentVol;
	}

	public static int getMasterVolume() { return masterVolume;	}
	public static int getFxVolume() { return fxVolume; }
	public static int getMusicVolume() { return musicVolume; }

	private static float toGain(int percentVolume) {
		double percentVol = percentVolume / 100.0;
		return (float) (20 * Math.log10(percentVol));
	}

	static class CloseListener implements LineListener {

		static LineListener self = new CloseListener();

		public void update(LineEvent e){
			if (e.getType() == LineEvent.Type.STOP) e.getLine().close();
		}
	}

}
