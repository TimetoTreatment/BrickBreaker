import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class AudioPlayer {

	private int mChannel;
	private int mMaxChannel;
	private Clip[] mClips;

	AudioPlayer(String fileName, int channel) {
		mChannel = 0;
		mMaxChannel = channel;
		mClips = new Clip[mMaxChannel];

		try {
			for (int i = 0; i < mMaxChannel; i++) {
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
		if (mChannel == mMaxChannel)
			mChannel = 0;

		mClips[mChannel].setFramePosition(0);
		mClips[mChannel].start();

		mChannel++;
	}

	void SetLoop(boolean b) {
		if (b == true)
			mClips[0].loop(-1);
		else
			mClips[0].loop(0);
	}

	void Stop() {
		mClips[mChannel].stop();
	}
}
