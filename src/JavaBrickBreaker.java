import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JavaBrickBreaker extends JFrame {

	private GameEntity gameEntity;
	private ScreenManager screenManager;

	JavaBrickBreaker() {

		gameEntity = new GameEntity();
		screenManager = new ScreenManager(gameEntity);

		setTitle("Brick Breaker");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Config.width, Config.height);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		add(screenManager);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JavaBrickBreaker();
		});
	}
}
