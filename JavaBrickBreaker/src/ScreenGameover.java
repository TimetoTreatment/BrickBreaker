import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScreenGameover extends JPanel implements Screen {

	private GameEntity mGameEntity;
	private JLabel mHighScore;
	private JLabel mScore;
	private JLabel mPrompt;
	private int mPrevHighScore;
	private boolean mExit;
	private boolean mAnimation;

	ScreenGameover(GameEntity gameEntity) {

		mGameEntity = gameEntity;
		mPrevHighScore = 0;
		mExit = false;
		mAnimation = false;

		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);

		mScore = new JLabel();
		mScore.setBounds(100, 100, 1000, 100);
		mScore.setHorizontalAlignment(SwingConstants.CENTER);
		mScore.setForeground(Color.white);
		mScore.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		add(mScore);

		mHighScore = new JLabel();
		mHighScore.setBounds(100, 200, 1000, 100);
		mHighScore.setHorizontalAlignment(SwingConstants.CENTER);
		mHighScore.setForeground(Color.white);
		mHighScore.setFont(new Font(Font.DIALOG, Font.PLAIN, 60));
		add(mHighScore);

		mPrompt = new JLabel("Press Spacebar to Play");
		mPrompt.setBounds(0, 600, 1200, 100);
		mPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		mPrompt.setForeground(Color.black);
		mPrompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 48));
		add(mPrompt);

		Animation();
		
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

		if (mPrevHighScore < mGameEntity.GetCurrentScore())
			mScore.setForeground(Color.green);
		else
			mScore.setForeground(Color.white);

		mHighScore.setText("HighScore : " + Integer.toString(mGameEntity.GetHighScore()));
		mScore.setText("Score : " + Integer.toString(mGameEntity.GetCurrentScore()));

		requestFocus();
	}

	private void Animation() {
		if (mAnimation)
			return;

		mAnimation = true;
		new Thread(() -> {
			try {

				Thread.sleep(4000);

				for (;;) {
					if (mExit == true)
						return;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}).start();
	}

	@Override
	public void Initialize() {
		// TODO Auto-generated method stub
		mAnimation = false;
	}

	@Override
	public boolean IsFinished() {

		if (mExit) {
			mExit = false;
			mPrevHighScore = mGameEntity.GetHighScore();
			return true;
		}

		return false;
	}
}
