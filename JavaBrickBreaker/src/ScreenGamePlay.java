import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class ScreenGamePlay extends JPanel implements Screen {

	private JLabel mTutorial;
	private JPanel mHelp;
	private GameEntity mGameEntity;
	private boolean mExit;

	private int mBarDirection = Bar.stop;
	private boolean mKeyLock = false;
	private boolean mWestKey = false;
	private boolean mEastKey = false;
	private boolean mUseBallKey = false;

	ScreenGamePlay(GameEntity gameEntity) {

		mGameEntity = gameEntity;
		mBarDirection = Bar.stop;
		mExit = false;

		setBorder(new MatteBorder(5, 5, 0, 5, Color.black));
		setBackground(Color.black);
		setFocusable(true);
		setLayout(null);

		mTutorial = new JLabel();
		mTutorial.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		mTutorial.setHorizontalAlignment(SwingConstants.CENTER);
		mTutorial.setForeground(Color.white);
		mTutorial.setBounds(680, 790, 500, 50);
		add(mTutorial);

		mHelp = new JPanel();
		mHelp.setLayout(new GridLayout(5, 1));
		mHelp.setOpaque(false);
		mHelp.setBounds(20, 550, 500, 200);
		mHelp.setVisible(false);

		JLabel yellow = new JLabel("Ball Fission");
		yellow.setForeground(new Color(200, 200, 0));
		yellow.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		yellow.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(yellow);

		JLabel green = new JLabel("Bar Length Up");
		green.setForeground(new Color(0, 200, 0));
		green.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		green.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(green);

		JLabel red = new JLabel("Ball Speed Up");
		red.setForeground(new Color(200, 0, 0));
		red.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		red.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(red);

		JLabel blue = new JLabel("Bar Length Down");
		blue.setForeground(new Color(0, 0, 200));
		blue.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		blue.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(blue);

		JLabel orange = new JLabel("Ball Bonus");
		orange.setForeground(new Color(200, 100, 0));
		orange.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		orange.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(orange);

		JLabel gray = new JLabel("???");
		gray.setForeground(new Color(100, 100, 100));
		gray.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		gray.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(gray);

		add(mHelp);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					mWestKey = false;
					break;

				case KeyEvent.VK_RIGHT:
					mEastKey = false;
					break;

				case KeyEvent.VK_H:
					mHelp.setVisible(false);
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					mWestKey = true;
					break;

				case KeyEvent.VK_RIGHT:
					mEastKey = true;
					break;

				case KeyEvent.VK_SPACE:
					mUseBallKey = true;
					break;

				case KeyEvent.VK_H:
					mHelp.setVisible(true);
					break;
				}
			}
		});
	}

	@Override
	public void Initialize() {
		mBarDirection = Bar.stop;
		mUseBallKey = false;

		mGameEntity.NewStage();
		mHelp.setVisible(false);
		Tutorial("Press 'SPACEBAR' to Launch BALL");

		new Thread(() -> {
			try {
				for (int i = 0; i < 100; i++) {
					setBorder(new MatteBorder(5, 5, 0, 5, new Color(i, i, i)));
					repaint();
					Thread.sleep(10);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}).start();

		mExit = false;
	}

	@Override
	public void Update() {
		MoveBar();
		UseBall();
		mGameEntity.Update(this, Config.frameTime);

		requestFocus();
		repaint();

		if (mGameEntity.IsClear()) {
			mGameEntity.NewStage();
			Tutorial("Press 'H' key to see Block Detail");
		}

		if (mGameEntity.IsGameOver()) {
			mExit = true;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		mGameEntity.Draw((Graphics2D) g);
	}

	@Override
	public boolean IsFinished() {
		if (mExit) {
			mExit = false;
			return true;
		}

		return mExit;
	}

	private void Tutorial(String text) {
		new Thread(() -> {
			try {
				mTutorial.setText(text);
				mTutorial.setForeground(Color.white);

				for (int i = 0; i <= 255; i++) {
					Thread.sleep(1);
					mTutorial.setForeground(new Color(i, i, i));
				}

				Thread.sleep(3000);

				for (int i = 255; i >= 0; i--) {
					Thread.sleep(1);
					mTutorial.setForeground(new Color(i, i, i));
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}).start();
	}

	private void MoveBar() {
		if (!mWestKey && !mEastKey)
			mBarDirection = Bar.stop;

		if (!mKeyLock) {
			if (mBarDirection == Bar.stop) {
				if (mWestKey)
					mBarDirection = Bar.west;
				if (mEastKey)
					mBarDirection = Bar.east;
			} else if (mWestKey && mBarDirection == Bar.east)
				mBarDirection = Bar.west;
			else if (mEastKey && mBarDirection == Bar.west)
				mBarDirection = Bar.east;
		}

		if (!mWestKey || !mEastKey)
			mKeyLock = false;
		else
			mKeyLock = true;

		mGameEntity.MoveBar(mBarDirection);
	}

	private void UseBall() {
		if (mUseBallKey == true) {
			mGameEntity.UseBall();
			mUseBallKey = false;
		}
	}
}
