import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JavaBrickBreaker extends JFrame {

	GameEntity gameEntity;
	Container screens;
	CardLayout screenManager;
	ScreenTitle screenTitle;
	ScreenGameplay screenGameplay;
	ScreenGameover screenGameover;
	Screen currentScreen;

	JavaBrickBreaker() {
		setTitle("Brick Breaker");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Config.width, Config.height);
		setResizable(false);

		gameEntity = new GameEntity();

		screens = getContentPane();
		screenManager = new CardLayout();

		screenTitle = new ScreenTitle();
		screenGameplay = new ScreenGameplay(gameEntity);
		screenGameover = new ScreenGameover(gameEntity);

		screens.setLayout(screenManager);
		screens.add("title", screenTitle);
		screens.add("gameplay", screenGameplay);
		screens.add("gameover", screenGameover);

		MainLoop();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	void MainLoop() {
		new Thread(() -> {
			for (currentScreen = screenTitle;;) {
				try {
					currentScreen.Initialize();
					Thread.sleep((long) (Config.frameTime * 1000));

					for (;;) {

						if (currentScreen.IsFinished()) {
							screenManager.next(screens);

							if (currentScreen instanceof ScreenTitle)
								currentScreen = screenGameplay;
							else if (currentScreen instanceof ScreenGameplay)
								currentScreen = screenGameover;
							else if (currentScreen instanceof ScreenGameover)
								currentScreen = screenTitle;

							break;
						}

						currentScreen.Update();
						Thread.sleep((long) (Config.frameTime * 1000));
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JavaBrickBreaker();
		});
	}
}
