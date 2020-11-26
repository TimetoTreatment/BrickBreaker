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

	private boolean exit;
	private boolean isInitialized;
	private int mLineXStart;
	private int mLineXEnd;
	private int mLineY;
	private int mLineHighlightPos;
	private JLabel title;
	private JLabel subtitle;
	private JLabel prompt;

	ScreenTitle() {

		exit = false;
		isInitialized = false;
		mLineXStart = 200;
		mLineXEnd = 1000;
		mLineY = 280;
		mLineHighlightPos = 350;

		setSize(Config.width, Config.height);
		setLayout(new GridLayout(3, 1));
		setFocusable(true);
		setVisible(true);
		setBackground(Color.black);

		title = new JLabel("Brick Breaker");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.BOTTOM);
		title.setSize(800, 100);
		title.setLocation((Config.width - title.getWidth()) / 2, 180);
		title.setForeground(Color.white);
		title.setFont(new Font(Font.DIALOG, Font.BOLD, 108));
		add(title);

		subtitle = new JLabel("with Java Swing");
		subtitle.setHorizontalAlignment(SwingConstants.CENTER);
		subtitle.setVerticalAlignment(SwingConstants.TOP);
		subtitle.setForeground(Color.white);
		subtitle.setFont(new Font(Font.DIALOG, Font.PLAIN, 30));
		subtitle.setBorder(new EmptyBorder(20, 200, 220, 200));
		add(subtitle);

		prompt = new JLabel("Press SPACEBAR to Play");
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		prompt.setForeground(Color.black);
		prompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 48));
		prompt.setBorder(new EmptyBorder(100, 100, 150, 100));
		add(prompt);

		Animation();

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					exit = true;
			}
		});
	}

	@Override
	public void Initialize() {
		if (isInitialized)
			return;

		isInitialized = true;
		exit = false;
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
		if (exit) {
			exit = false;
			return true;
		}

		return false;
	}

	private void Animation() {
		new Thread(() -> {
			try {
				Thread.sleep(4000);

				for (; !exit;) {
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
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				for (; !exit;) {
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
