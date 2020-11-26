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
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ScreenGameplay extends JPanel implements Screen {

	GameEntity mGameEntity;
	boolean mExit = false;
	MatteBorder mBorder;
	boolean isInitialized = true;
	JLabel tutorial;
	JPanel mHelp;
	int mHighlightY;

	ScreenGameplay(GameEntity gameEntity) {
		mGameEntity = gameEntity;

		setBorder(new MatteBorder(5, 5, 0, 5, Color.black));
		setBackground(Color.black);
		setLayout(null);

		tutorial = new JLabel();
		tutorial.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		tutorial.setHorizontalAlignment(SwingConstants.CENTER);
		tutorial.setForeground(Color.white);
		tutorial.setBounds(680, 790, 500, 50);
		add(tutorial);

		mHelp = new JPanel();
		mHelp.setLayout(new GridLayout(5, 1));
		mHelp.setOpaque(false);
		mHelp.setBounds(20, 500, 500, 200);
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

		JLabel orange = new JLabel("Bonus++ (SPACEBAR)");
		orange.setForeground(new Color(200, 100, 0));
		orange.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		orange.setBorder(new EmptyBorder(0, 20, 0, 0));
		mHelp.add(orange);

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
					mGameEntity.MoveBar(Bar.left);
					break;

				case KeyEvent.VK_RIGHT:
					mGameEntity.MoveBar(Bar.right);
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

		setFocusable(true);
		setVisible(true);
	}

	@Override
	public boolean IsFinished() {

		if (mExit) {
			mExit = false;
			return true;
		}

		return mExit;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub

		mGameEntity.Update(this, Config.frameTime);
		requestFocus();
		repaint();

		if (mGameEntity.IsClear()) {
			mGameEntity.NewStage();
			Tutorial("Press 'H' to see Block Detail");
		}

		if (mGameEntity.IsGameOver()) {
			mBorder = new MatteBorder(5, 5, 0, 5, Color.black);
			mExit = true;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		mGameEntity.Draw((Graphics2D) g);
	}

	void Tutorial(String text) {
		new Thread(() -> {
			try {
				tutorial.setText(text);
				tutorial.setForeground(Color.white);

				for (int i = 0; i <= 255; i++) {
					Thread.sleep(1);
					tutorial.setForeground(new Color(i, i, i));
				}
				
				Thread.sleep(3000);

				for (int i = 255; i >= 0; i--) {
					Thread.sleep(1);
					tutorial.setForeground(new Color(i, i, i));
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}).start();
	}

	@Override
	public void Initialize() {

		mGameEntity.Initialize();
		mGameEntity.NewStage();
		Tutorial("Press 'SPACEBAR' to Launch BALL");

		setBorder(new MatteBorder(5, 5, 0, 5, Color.black));

		new Thread(() -> {

			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setBorder(new MatteBorder(5, 5, 0, 5, new Color(i, i, i)));
				repaint();
			}
		}).start();

		if (isInitialized) {
			isInitialized = false;
			return;
		}
		mExit = false;
	}
}
