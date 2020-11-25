import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class ScreenGameover extends JPanel implements Screen {

	boolean exit = false;

	ScreenGameover(GameEntity gameEntity) {
		setBackground(Color.blue);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					exit = true;
					System.out.println("exit");
				}
			}
		});



		setFocusable(true);
		setVisible(true);
	}

	@Override
	public boolean IsFinished() {

		if (exit) {
			exit = false;
			return true;
		}

		return false;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		requestFocus();
	}

	@Override
	public void Initialize() {
		// TODO Auto-generated method stub

	}

}
