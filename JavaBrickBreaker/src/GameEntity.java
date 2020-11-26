import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.Vector;

public class GameEntity {

	private int mStage;
	private int mHighScore;
	private int mCurrentScore;
	private int mBonusBall;
	private int mBallQueue;
	private Vector<Prop> mProps;
	private Vector<Ball> mBalls;
	private Vector<Brick> mBricks;
	private Bar mBar;
	AudioPlayer mAudioBGM = new AudioPlayer("bgm.wav", 1);
	AudioPlayer mAudioBusted = new AudioPlayer("busted.wav", 1);
	AudioPlayer mAudioClear = new AudioPlayer("clear.wav", 1);
	AudioPlayer[] mAudioLaugh = new AudioPlayer[7];
	AudioPlayer[] mAudioSteamPack = new AudioPlayer[2];
	AudioPlayer[] mAudioDeath = new AudioPlayer[2];
	AudioPlayer mAudioLaunch = new AudioPlayer("launch.wav", 10);
	AudioPlayer mAudioHitBrick = new AudioPlayer("hitBrick.wav", 10);
	AudioPlayer mAudioHitBar = new AudioPlayer("hitBar.wav", 10);
	AudioPlayer mAudioItemLongBar = new AudioPlayer("itemLongBar.wav", 5);
	AudioPlayer mAudioItemShortBar = new AudioPlayer("itemShortBar.wav", 5);
	AudioPlayer mAudioItemBonusBall = new AudioPlayer("itemBonusBall.wav", 5);
	AudioPlayer mAudioItemUltimate = new AudioPlayer("itemUltimate.wav", 5);

	GameEntity() {
		mAudioBGM.Play();
		mAudioBGM.SetLoop(true);

		mProps = new Vector<>();
		mBalls = new Vector<>();
		mBricks = new Vector<>();
		mBar = null;
		mHighScore = 0;

		for (int i = 0; i < 7; i++)
			mAudioLaugh[i] = new AudioPlayer("laugh" + i + ".wav", 2);

		mAudioDeath[0] = new AudioPlayer("death0.wav", 10);
		mAudioDeath[1] = new AudioPlayer("death1.wav", 10);
		mAudioSteamPack[0] = new AudioPlayer("steamPack0.wav", 5);
		mAudioSteamPack[1] = new AudioPlayer("steamPack1.wav", 5);
	}

	void Initialize() {
		mCurrentScore = 0;
		mStage = 1;
		mBallQueue = 0;
		mBonusBall = 5;
	}

	void AddBall(Vec2f position, Vec2f velocity) {
		Ball ball = new Ball(position, velocity);
		mProps.add(ball);
		mBalls.add(ball);
	}

	void UseBall() {
		if (mBonusBall > 0) {
			mBonusBall--;
			mAudioLaunch.Play();
			mBallQueue++;
		}
	}

	void AddBar(Vec2f position, int width, int height, Color color) {
		mBar = new Bar(position, width, height, color);
		mProps.add(mBar);
	}

	void MoveBar(int direction) {
		mBar.Move(direction);
	}

	void AddBrick(Vec2f position, int width, int height, Color color) {
		Brick newBrick = new Brick(position, width, height, color);
		mProps.add(newBrick);
		mBricks.add(newBrick);
	}

	void NewStage() {
		mProps.clear();
		mBalls.clear();
		mBricks.clear();
		mBar = null;

		final int maxWidth = 1190;
		final int maxHeight = 400;
		final int maxCol = mStage + 2;
		final int maxRow = mStage + 1;
		int width = maxWidth / maxCol;
		int height = maxHeight / maxRow;

		for (int row = 0; row < maxRow; row++)
			for (int col = 0; col < maxCol; col++)
				AddBrick(new Vec2f(5 + col * width * 0.99, 5 + row * height * 0.99), (int) (width * 0.996), height,
						Color.white);

		AddBar(new Vec2f(390, 750), 400, 20, Color.white);
		AddBall(new Vec2f(600, 740), new Vec2f(250, -500));
	}

	void Update(Container container, double dt) {
		Ball multipleBallSource = null;
		boolean multipleBallEnable = false;
		boolean fastBallEnable = false;

		for (; mBallQueue > 0; mBallQueue--) {
			Ball newBall = new Ball(new Vec2f(mBar.GetPosition().x + mBar.GetWidth() / 2, mBar.GetPosition().y - 10),
					new Vec2f(mBar.GetVelocity().x / 2 + 600 * Math.random() - 350, -600 * Math.random() - 300));

			mProps.add(newBall);
			mBalls.add(newBall);
		}

		for (Ball ball : mBalls) {
			ball.Update(dt);
			Prop collisionProp = ball.Collision(container, mProps);

			if (collisionProp instanceof Ball) {
				ball.SetEnable(false);
				mAudioDeath[(int) Math.random() * 2].Play();
			}

			else if (collisionProp instanceof Bar) {
				mAudioHitBar.Play();
				if (ball.GetPosition().x <= mBar.GetPosition().x + mBar.GetWidth() / 5)
					ball.GetVelocity().x -= Math.abs(Ball.defaultVelocity.x);
				else if (ball.GetPosition().x <= mBar.GetPosition().x + mBar.GetWidth() * 2 / 5)
					ball.GetVelocity().x -= Math.abs(Ball.defaultVelocity.x * 0.5);
				else if (ball.GetPosition().x >= mBar.GetPosition().x + mBar.GetWidth() * 4 / 5)
					ball.GetVelocity().x += Math.abs(Ball.defaultVelocity.x);
				else if (ball.GetPosition().x >= mBar.GetPosition().x + mBar.GetWidth() * 3 / 5)
					ball.GetVelocity().x += Math.abs(Ball.defaultVelocity.x * 0.5);

				ball.GetVelocity().x += mBar.GetVelocity().x / 20;
				ball.GetVelocity().y = -ball.GetVelocity().y;
				ball.GetPosition().y -= ball.GetRadious();

			} else if (collisionProp instanceof Brick) {
				Brick brick = (Brick) collisionProp;

				if (brick.IsEnable()) {
					mAudioHitBrick.Play();
					brick.SetEnable(false);

					mCurrentScore += 100;
					if (mHighScore < mCurrentScore)
						mHighScore = mCurrentScore;

					switch (brick.GetType()) {

					case Brick.multipleBall:
						mAudioLaugh[(int) (Math.random() * 7)].Play();
						multipleBallSource = ball;
						multipleBallEnable = true;
						break;

					case Brick.fastBall:
						fastBallEnable = true;
						mAudioSteamPack[(int) (Math.random() * 2)].Play();
						break;

					case Brick.shortBar:
						mBar.SetWidth((int) (mBar.GetWidth() * 0.8));
						mAudioItemShortBar.Play();
						break;

					case Brick.longBar:
						mBar.SetWidth((int) (mBar.GetWidth() * 1.25));
						mAudioItemLongBar.Play();
						break;

					case Brick.addLife:
						mBonusBall++;
						mAudioItemBonusBall.Play();
						break;

					case Brick.ultimate:
						mAudioItemUltimate.Play();
						mBallQueue += 10;
						break;

					}
				}
			}
		}

		if (fastBallEnable) {
			for (var b : mBalls) {
				b.GetVelocity().x *= 1.25;
				b.GetVelocity().y *= 1.25;
			}
		}

		if (multipleBallEnable) {
			AddBall(new Vec2f(multipleBallSource.GetPosition().x, multipleBallSource.GetPosition().y),
					new Vec2f(multipleBallSource.GetVelocity().x * (Math.random() + 0.5),
							multipleBallSource.GetVelocity().y * (Math.random() + 0.5)));

			AddBall(new Vec2f(multipleBallSource.GetPosition().x, multipleBallSource.GetPosition().y),
					new Vec2f(multipleBallSource.GetVelocity().x * (Math.random() + 0.5),
							multipleBallSource.GetVelocity().y * (Math.random() + 0.5)));
		}

		for (Brick brick : mBricks)
			brick.Update(dt);

		mBar.Update(dt);
		mBar.Collision(container);

		for (int i = 0; i < mBalls.size(); i++)
			if (mBalls.get(i).IsEnable() == false)
				mBalls.remove(i);

		for (int i = 0; i < mBricks.size(); i++) {
			if (mBricks.get(i).IsRemovable())
				mBricks.remove(i);
		}
	}

	void Draw(Graphics2D g) {

		for (int i = 0; i < mBonusBall; i++) {
			if (mBar != null) {
				int x = (int) mBar.GetPosition().x + i * 10;
				int y = (int) mBar.GetPosition().y + 20;
				g.setPaint(new GradientPaint(x, y, new Color(200, 100, 100), x + 7, y + 7, Color.black));
				g.fillOval(x, y, 7, 7);
			}
		}

		for (int i = 0; i < mBalls.size(); i++)
			mBalls.get(i).Draw(g);

		for (int i = 0; i < mBricks.size(); i++)
			mBricks.get(i).Draw(g);

		if (mBar != null)
			mBar.Draw(g);
	}

	boolean IsGameOver() {
		if (mBalls.size() != 0)
			return false;

		mAudioBusted.Play();
		return true;
	}

	boolean IsClear() {
		if (mBricks.size() != 0)
			return false;

		mAudioClear.Play();
		mStage += 2;
		mBonusBall += 3;
		return true;
	}

	int GetHighScore() {
		return mHighScore;
	}

	int GetCurrentScore() {
		return mCurrentScore;
	}
}
