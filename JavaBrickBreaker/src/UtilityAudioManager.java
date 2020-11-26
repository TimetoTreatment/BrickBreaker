import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioPlayer {

	private Clip[] mClips;
	private int mChannel;
	private int mIndex;

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
		if (mIndex == mChannel)
			mIndex = 0;

		mClips[mIndex].setFramePosition(0);
		mClips[mIndex].start();
		
		mIndex++;
	}

	void SetLoop(boolean b) {
		if (b == true)
			mClips[0].loop(-1);
		else
			mClips[0].loop(0);
	}

	void Stop() {
		mClips[mIndex].stop();
	}
}
