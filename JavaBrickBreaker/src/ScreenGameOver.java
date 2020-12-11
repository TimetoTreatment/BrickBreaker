import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScreenGameOver extends JPanel implements Screen {

	private GameEntity mGameEntity;
	private JLabel mHighScore;
	private JLabel mScore;
	private JLabel mPrompt;
	private int mPrevHighScore;
	private Thread mAnimation;
	private boolean mAnimationEnd;
	private boolean mExit;

	ScreenGameOver(GameEntity gameEntity) {
		mGameEntity = gameEntity;
		mPrevHighScore = 0;
		mExit = false;
		mAnimation = null;
		mAnimationEnd = false;

		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);

		mScore = new JLabel();
		mScore.setBounds(100, 180, 1000, 100);
		mScore.setHorizontalAlignment(SwingConstants.CENTER);
		mScore.setForeground(Color.black);
		mScore.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		add(mScore);

		mHighScore = new JLabel();
		mHighScore.setBounds(100, 300, 1000, 100);
		mHighScore.setHorizontalAlignment(SwingConstants.CENTER);
		mHighScore.setForeground(Color.black);
		mHighScore.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		add(mHighScore);

		mPrompt = new JLabel("Press SPACEBAR to Restart");
		mPrompt.setBounds(0, 640, 1200, 100);
		mPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		mPrompt.setForeground(Color.black);
		mPrompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 48));
		add(mPrompt);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					mExit = true;
				}
			}
		});
	}

	@Override
	public void Update() {
		requestFocus();
	}

	@Override
	public void Initialize() {
		mExit = false;
		mAnimationEnd = false;
		mScore.setForeground(Color.black);
		mHighScore.setForeground(Color.black);
		mHighScore.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		mPrompt.setForeground(Color.black);

		mAnimation = new Thread(() -> {
			try {
				mScore.setText("Score : " + Integer.toString(mGameEntity.GetCurrentScore()));
				mHighScore.setText("HighScore : " + Integer.toString(mPrevHighScore));
				mScore.setForeground(Color.white);
				Thread.sleep(1000);

				mHighScore.setForeground(Color.white);
				Thread.sleep(1000);

				if (mPrevHighScore < mGameEntity.GetCurrentScore()) {
					for (int score = mPrevHighScore; score <= mGameEntity.GetCurrentScore();) {
						mHighScore.setText("HighScore : " + Integer.toString(score));
						Thread.sleep(1);

						if (mGameEntity.GetCurrentScore() < 10000)
							score += 10;
						else
							score += 100;
					}

					Thread.sleep(1000);
					mGameEntity.PlayHighScore();

					mHighScore.setFont(new Font(Font.DIALOG, Font.BOLD, 88));
					mHighScore.setForeground(new Color(0, 200, 0));
					
					Thread.sleep(500);
				}

				mAnimationEnd = true;

				for (;;) {
					for (int i = 0; i < 256; i++) {
						mPrompt.setForeground(new Color(i, i, i));
						Thread.sleep(1);
					}

					for (int i = 255; i >= 0; i--) {
						mPrompt.setForeground(new Color(i, i, i));
						Thread.sleep(1);
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		});

		mAnimation.start();
	}

	@Override
	public boolean IsFinished() {
		if (mExit && mAnimationEnd) {
			mPrevHighScore = mGameEntity.GetHighScore();
			mAnimation.interrupt();
			return true;
		}

		return false;
	}
}
