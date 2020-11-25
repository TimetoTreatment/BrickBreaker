import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioPlayer {

	private Clip clip;

	AudioPlayer(String fileName) {
		try {
			clip = AudioSystem.getClip();
			URL url = getClass().getClassLoader().getResource(fileName);

			if (url != null)
				clip.open(AudioSystem.getAudioInputStream(url));
			else
				clip.open(AudioSystem.getAudioInputStream(new File("Assets/Audio/" + fileName)));

		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}

	void Play() {
		clip.setFramePosition(0);
		clip.start();
	}

	void Stop() {
		clip.stop();
	}
}
