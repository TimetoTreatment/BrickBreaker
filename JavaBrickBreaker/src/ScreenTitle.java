import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ScreenTitle extends JPanel implements Screen {

	private GameEntity mGameEntity;
	private boolean mExit;
	private int mLineXStart;
	private int mLineXEnd;
	private int mLineY;
	private int mLineHighlightPos;
	private JLabel mTitle;
	private JLabel mSubtitle;
	private JLabel mPrompt;

	ScreenTitle(GameEntity gameEntity) {

		mGameEntity = gameEntity;
		mExit = false;
		mLineXStart = 198;
		mLineXEnd = 1002;
		mLineY = 280;
		mLineHighlightPos = 375;

		setSize(Config.width, Config.height);
		setLayout(new GridLayout(3, 1));
		setFocusable(true);
		setBackground(Color.black);

		mTitle = new JLabel("Brick Breaker");
		mTitle.setHorizontalAlignment(SwingConstants.CENTER);
		mTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		mTitle.setSize(800, 100);
		mTitle.setLocation((Config.width - mTitle.getWidth()) / 2, 180);
		mTitle.setForeground(Color.white);
		mTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 108));
		add(mTitle);

		mSubtitle = new JLabel("with Java Swing");
		mSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		mSubtitle.setVerticalAlignment(SwingConstants.TOP);
		mSubtitle.setForeground(Color.white);
		mSubtitle.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		mSubtitle.setBorder(new EmptyBorder(20, 200, 220, 200));
		add(mSubtitle);

		mPrompt = new JLabel("Press SPACEBAR to Play");
		mPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		mPrompt.setForeground(Color.black);
		mPrompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 48));
		mPrompt.setBorder(new EmptyBorder(100, 100, 150, 100));
		add(mPrompt);

		Animation();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					mExit = true;
			}
		});
	}

	@Override
	public void Initialize() {
		mExit = false;
		mGameEntity.Initialize();
	}

	@Override
	public void Update() {
		requestFocus();
		repaint();
	}

	@Override
	public void paintComponent(Graphics _g) {
		super.paintComponent(_g);

		Graphics2D g = (Graphics2D) _g;

		g.setPaint(new GradientPaint(mLineXStart, mLineY, Color.black, mLineHighlightPos, mLineY, Color.white));
		g.drawLine(mLineXStart, mLineY, mLineHighlightPos + 1, mLineY);

		g.setPaint(new GradientPaint(mLineHighlightPos, mLineY, Color.white, mLineXEnd, mLineY, Color.black));
		g.drawLine(mLineHighlightPos - 1, mLineY, mLineXEnd, mLineY);
	}

	@Override
	public boolean IsFinished() {
		if (mExit) {
			mExit = false;
			return true;
		}

		return false;
	}

	private void Animation() {
		new Thread(() -> {
			try {
				Thread.sleep(3000);

				for (; !mExit;) {
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
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				for (; !mExit;) {
					for (int dt = 4; mLineHighlightPos <= mLineXEnd; mLineHighlightPos += 2) {

						if (mLineHighlightPos >= (mLineXEnd - mLineXStart) * 4 / 5 + mLineXStart)
							dt = 1;

						Thread.sleep(dt);
					}
					for (int dt = 4; mLineHighlightPos >= mLineXStart; mLineHighlightPos -= 2) {

						if (mLineHighlightPos <= (mLineXEnd - mLineXStart) * 1 / 5 + mLineXStart)
							dt = 1;

						Thread.sleep(dt);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}
}
