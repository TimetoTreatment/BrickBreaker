import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScreenGameover extends JPanel implements Screen {

	private boolean exit = false;
	private GameEntity mGameEntity;
	private JLabel mHighScore;
	private JLabel mScore;
	private JLabel prompt;
	private boolean animation = false;
	private int mPrevHighScore = 0;

	ScreenGameover(GameEntity gameEntity) {

		mGameEntity = gameEntity;

		setLayout(null);
		setBackground(Color.black);

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

		prompt = new JLabel("Press Spacebar to Play");
		prompt.setBounds(0, 600, 1200, 100);
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		prompt.setForeground(Color.black);
		prompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 48));
		add(prompt);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					exit = true;
				}
			}
		});

		Animation();
		setFocusable(true);
		setVisible(true);
	}

	@Override
	public boolean IsFinished() {

		if (exit) {
			exit = false;
			mPrevHighScore = mGameEntity.GetHighScore();
			return true;
		}

		return false;
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
		if (animation)
			return;

		animation = true;
		new Thread(() -> {
			try {

				Thread.sleep(4000);

				for (;;) {
					if (exit == true)
						return;

					for (int i = 0; i < 256; i++) {
						prompt.setForeground(new Color(i, i, i));
						Thread.sleep(1);
					}

					for (int i = 255; i >= 0; i--) {
						prompt.setForeground(new Color(i, i, i));
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
		animation = false;
	}

}
