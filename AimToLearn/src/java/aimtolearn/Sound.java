package aimtolearn;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * An enum of possible sound files
 */
public enum Sound {
	MENU_MOVE("menu_move.wav"),
	MENU_SELECT("menu_select.wav"),
	SHIELD_ACTIVE("shield.wav"),
	SHOOT("shoot.wav"),
	SHOT_CHARGE("shot_charge.wav"),
	ANSWER_EXPLOSION("answer_hit_explosion.wav"),
	SHIP_HIT("ship_hit_explosion.wav"),
	SHIELD_HIT("answer_hit_shield.wav"),
	SHIP_EXPLOSION("ship_explosion.wav"),
	BG_MUSIC_V1("bg_music_v1.wav", true),
	BG_MUSIC_V2("bg_music_v2.wav", true);

	private static double masterVolume = 1.0;
	private static int fxVolume = 100;
	private static int musicVolume = 100;
	private static Sound activeMusic = null;

	private Clip clip;
	private FloatControl gainControl;
	private boolean isMusic;

	public static int STEP_SIZE = 5;

	/** Called to preload sounds */
	public static void init() { values(); }

	/**
	 * Constructor for sound effects. Delegates to the 2-arg constructor with isMusic = false
	 */
	Sound(String fileName) {
		this(fileName, false);
	}

	/**
	 * Constructor for sound effects or music
	 * @param fileName The filename to load
	 * @param isMusic true if this sound is music
	 */
	Sound(String fileName, boolean isMusic) {
		this.clip = Constants.getSound(fileName);
		this.gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		this.isMusic = isMusic;
	}

	/**
	 * Play this sound once, cutting off the previous instance of this sound
	 */
	public void play() {
		if (isMusic) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * Loop this sound indefinitely
	 */
	public void loop() {
		if (isMusic && activeMusic != this) {
			if(activeMusic != null) activeMusic.stop();
			clip.setFramePosition(0);
			clip.setLoopPoints(0, -1);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			activeMusic = this;
		}
	}

	public void stop() {
		if (isMusic) activeMusic = null;
		clip.stop();
		clip.flush();
	}

	/**
	 * Set the master volume, which scales both other volumes accordingly
	 * @param percentVolume an integer representation of a percent for this volume
	 */
	public static void setMasterVolume(int percentVolume) {
		masterVolume = clampVolume(percentVolume) / 100.0;

		// update other volumes with this new master volume scale
		setFxVolume(fxVolume);
		setMusicVolume(musicVolume);
	}

	/**
	 * Sets the volume of all sound effects
	 * @param percentVolume an integer representation of a percent for this volume
	 */
	public static void setFxVolume(int percentVolume) {
		fxVolume = clampVolume(percentVolume);
		float gain = toGain((int) (fxVolume * masterVolume));
		for (Sound sound : values()) {
			if (!sound.isMusic)
				sound.gainControl.setValue(gain);
		}
	}

	/**
	 * Sets the volume of all music
	 * @param percentVolume an integer representation of a percent for this volume
	 */
	public static void setMusicVolume(int percentVolume) {
		musicVolume = clampVolume(percentVolume);
		float gain = toGain((int) (musicVolume * masterVolume));
		for (Sound sound : values()) {
			if (sound.isMusic)
				sound.gainControl.setValue(gain);
		}
	}

	/**
	 * Used to clamp the given volume to the range 0 - 150
	 */
	private static int clampVolume(int percentVol) {
		return percentVol < 0 ? 0 : percentVol > 150 ? 150 : percentVol;
	}

	// === getters for the 3 volumes ===

	public static int getMasterVolume() { return (int) (100 * masterVolume); }
	public static int getFxVolume() { return fxVolume; }
	public static int getMusicVolume() { return musicVolume; }

	/**
	 * Turn a percent volume >0 into a gain value
	 */
	private static float toGain(int percentVolume) {
		double percentVol = percentVolume / 100.0;
		return (float) (20 * Math.log10(percentVol));
	}

}
