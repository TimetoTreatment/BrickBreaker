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

public class ScreenGameplay extends JPanel implements Screen {

	private GameEntity mGameEntity;
	private boolean mExit;
	private boolean mIsInitialized;
	private JLabel mTutorial;
	private JPanel mHelp;

	ScreenGameplay(GameEntity gameEntity) {
		mGameEntity = gameEntity;
		mExit = false;
		mIsInitialized = true;

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

		JLabel yellow = new JLabel("Ball Fission X 3");
		yellow.setForeground(new Color(200, 200, 0));
		yellow.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		yellow.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(yellow);

		JLabel red = new JLabel("Ball Speed X 1.25");
		red.setForeground(new Color(200, 0, 0));
		red.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		red.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(red);

		JLabel green = new JLabel("Bar Length X 1.25");
		green.setForeground(new Color(0, 200, 0));
		green.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		green.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(green);

		JLabel blue = new JLabel("Bar Length X 0.8");
		blue.setForeground(new Color(0, 0, 200));
		blue.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		blue.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(blue);

		JLabel orange = new JLabel("Bonus Ball + 1");
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
					mGameEntity.MoveBar(Bar.stop);
					break;

				case KeyEvent.VK_RIGHT:
					mGameEntity.MoveBar(Bar.stop);
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
					mGameEntity.MoveBar(Bar.west);
					break;

				case KeyEvent.VK_RIGHT:
					mGameEntity.MoveBar(Bar.east);
					break;

				case KeyEvent.VK_SPACE:
					mGameEntity.UseBall();
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

		mGameEntity.Initialize();
		mGameEntity.NewStage();
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

		if (mIsInitialized) {
			mIsInitialized = false;
			return;
		}
		mExit = false;
	}

	@Override
	public void Update() {

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

	void Tutorial(String text) {
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
}
