import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ScreenTitle extends JPanel implements Screen {

	boolean exit;
	boolean isInitialized;
	JLabel title;
	JLabel subtitle;
	JLabel prompt;

	ScreenTitle() {

		exit = false;
		isInitialized = false;

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
		subtitle.setBorder(
				new CompoundBorder(new EmptyBorder(0, 200, 220, 200), new MatteBorder(0, 0, 1, 0, Color.white)));
		add(subtitle);

		prompt = new JLabel("Press Spacebar to Play");
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		prompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
		prompt.setBorder(new CompoundBorder(new EmptyBorder(170, 390, 65, 390), new LineBorder(Color.white, 2)));
		add(prompt);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					exit = true;
			}
		});
		
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("x " + e.getX());
				System.out.println("y " + e.getY());
			}
		});
	}

	@Override
	public void Initialize() {
		if (isInitialized)
			return;

		isInitialized = true;
		exit = false;
		Animation();
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
	}

	private void Animation() {
		new Thread(() -> {
			try {

				for (;;) {

					if (exit == true)
						return;

					prompt.setForeground(Color.black);
					Thread.sleep(249);
					
					double random = Math.random();
					if(random <= 0.33)
						prompt.setForeground(Color.red);
					else if(random <= 0.66)
						prompt.setForeground(Color.green);
					else
						prompt.setForeground(Color.cyan);

					Thread.sleep(249);

				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}).start();
	}

	@Override
	public boolean IsFinished() {
		if (exit) {
			exit = false;
			return true;
		}

		return false;
	}

}
