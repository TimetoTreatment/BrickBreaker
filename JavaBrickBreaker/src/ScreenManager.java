import java.awt.CardLayout;
import javax.swing.JPanel;

class ScreenManager extends JPanel {

	private GameEntity mGameEntity;
	private CardLayout mScreens;
	private ScreenTitle screenTitle;
	private ScreenGamePlay screenGamePlay;
	private ScreenGameOver screenGameOver;
	private Screen currentScreen;

	ScreenManager(GameEntity gameEntity) {

		mGameEntity = gameEntity;
		mScreens = new CardLayout();
		screenTitle = new ScreenTitle(mGameEntity);
		screenGamePlay = new ScreenGamePlay(mGameEntity);
		screenGameOver = new ScreenGameOver(mGameEntity);
		currentScreen = screenTitle;
		setLayout(mScreens);

		add("title", screenTitle);
		add("gameplay", screenGamePlay);
		add("gameover", screenGameOver);

		MainLoop();
	}

	void MainLoop() {
		new Thread(() -> {
			for (;;) {
				try {
					currentScreen.Initialize();
					Thread.sleep((long) (Config.frameTime * 1000));

					for (; !currentScreen.IsFinished();) {
						Thread.sleep((long) (Config.frameTime * 1000));
						currentScreen.Update();
					}

					mScreens.next(this);

					if (currentScreen instanceof ScreenTitle)
						currentScreen = screenGamePlay;
					else if (currentScreen instanceof ScreenGamePlay)
						currentScreen = screenGameOver;
					else if (currentScreen instanceof ScreenGameOver)
						currentScreen = screenTitle;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
