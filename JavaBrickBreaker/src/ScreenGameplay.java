import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class ScreenGameplay extends JPanel implements Screen {

	
	GameEntity mGameEntity;
	boolean mExit = false;
	MatteBorder mBorder;
	boolean isInitialized = true;

	ScreenGameplay(GameEntity gameEntity) {
		mGameEntity = gameEntity;
		mGameEntity.AddBar(new Vec2f(390, 750), 400, 20, Color.white);
		mGameEntity.AddBall(new Vec2f(600, 740), new Vec2f(250, -500));

		int row = 5;
		int col = 5;

		for (row = 5; row <= 500; row += 100) {
			for (col = 5; col <= 1000; col += 197)
				mGameEntity.AddBrick(new Vec2f(col, row), 200, 100, Color.white);
		}

		setBorder(new MatteBorder(5, 5, 0, 5, Color.black));
		setBackground(Color.black);

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

		if(mGameEntity.IsClear())
		{
			
			
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
		// TODO Auto-generated method stub

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

		mGameEntity.AddBar(new Vec2f(390, 750), 400, 20, Color.white);
		mGameEntity.AddBall(new Vec2f(600, 740), new Vec2f(250, -500));

	}

}
