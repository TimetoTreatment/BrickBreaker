import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class ScreenGameplay extends JPanel implements Screen {

	GameEntity mGameEntity;
	boolean mExit = false;
	MatteBorder mBorder;
	boolean isInitialized = true;
	

	ScreenGameplay(GameEntity gameEntity) {
		mGameEntity = gameEntity;

		setBorder(new MatteBorder(5, 5, 0, 5, Color.black));
		setBackground(Color.black);

		setLayout(null);

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

	@Override
	public void Initialize() {
		
		JLabel tutorial = new JLabel("Press SPACEBAR to launch spare balls");
		tutorial.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
		tutorial.setHorizontalAlignment(SwingConstants.CENTER);
		tutorial.setForeground(Color.white);
		tutorial.setBounds(680, 790, 500   , 50);
		add(tutorial);

		new Thread(() -> {
			try {

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

		mGameEntity.Initialize();
		mGameEntity.NewStage();

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
