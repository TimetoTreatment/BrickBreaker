import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioPlayer {

	boolean mLoop = false;
	private Clip[] mClips;
	int mChannel;
	int mIndex;

	AudioPlayer(String fileName, int channel) {

		mChannel = channel;
		mClips = new Clip[mChannel];
		mIndex = 0;

		try {
			for (int i = 0; i < mChannel; i++) {
				mClips[i] = AudioSystem.getClip();
				URL url = getClass().getClassLoader().getResource(fileName);

				if (url != null)
					mClips[i].open(AudioSystem.getAudioInputStream(url));
				else
					mClips[i].open(AudioSystem.getAudioInputStream(new File("Assets/Audio/" + fileName)));
			}
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}

	void Play() {

		mIndex++;

		if (mIndex == mChannel)
			mIndex = 0;

		mClips[mIndex].setFramePosition(0);
		mClips[mIndex].start();
	}

	void SetLoop(boolean b) {
		if (b == true)
			mClips[0].loop(100);
	}

	void Stop() {
		mClips[mIndex].stop();
	}
}
