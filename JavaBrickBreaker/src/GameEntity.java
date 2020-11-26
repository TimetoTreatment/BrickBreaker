import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Vector;

public class GameEntity {

	private int mBonusBall;
	int highScore;
	int currentScore;
	private int mStage;
	private int mBallQueue;
	private ArrayList<Prop> mProps;
	private ArrayList<Ball> mBalls;
	private ArrayList<Brick> mBricks;
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

	GameEntity() {

		mAudioBGM.Play();
		mAudioBGM.SetLoop(true);

		mProps = new ArrayList<>();
		mBalls = new ArrayList<>();
		mBricks = new ArrayList<>();
		mBar = null;
		highScore = 0;

		for (int i = 0; i < 7; i++)
			mAudioLaugh[i] = new AudioPlayer("laugh" + i + ".wav", 1);

		mAudioDeath[0] = new AudioPlayer("death0.wav", 10);
		mAudioDeath[1] = new AudioPlayer("death1.wav", 10);
		mAudioSteamPack[0] = new AudioPlayer("steamPack0.wav", 5);
		mAudioSteamPack[1] = new AudioPlayer("steamPack1.wav", 5);
	}

	void Initialize() {

		currentScore = 0;
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
		if (mBonusBall == 0)
			return;

		mBonusBall--;
		mAudioLaunch.Play();
		mBallQueue++;
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
		final int maxRow = mStage+1;
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

		for (Brick brick : mBricks) {
			brick.Update(dt);
		}

		for (; mBallQueue > 0; mBallQueue--) {
			Ball newBall = new Ball(new Vec2f(mBar.mPosition.x + mBar.mWidth / 2, mBar.mPosition.y - 10),
					new Vec2f(600 * Math.random() - 350, -600 * Math.random() - 300));

			mProps.add(newBall);
			mBalls.add(newBall);
		}

		for (Ball ball : mBalls) {
			ball.Update(dt);
			Prop collisionProp = ball.Collision(container, mProps);

			if (collisionProp instanceof Ball) {
				ball.mEnable = false;
				mAudioDeath[(int) Math.random() * 2].Play();
			}

			else if (collisionProp instanceof Bar) {
				mAudioHitBar.Play();
				if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth / 5)
					ball.mVelocity.x -= Math.abs(ball.mDefaultVelocity.x);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 2 / 5)
					ball.mVelocity.x -= Math.abs(ball.mDefaultVelocity.x * 0.5);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 3 / 5)
					;
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 4 / 5)
					ball.mVelocity.x += Math.abs(ball.mDefaultVelocity.x * 0.5);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth)
					ball.mVelocity.x += Math.abs(ball.mDefaultVelocity.x);

				ball.mVelocity.x += mBar.mVelocity.x / 20;
				ball.mVelocity.y = -ball.mVelocity.y;
				ball.mPosition.y = mBar.mPosition.y - ball.mRadious * 2;

			} else if (collisionProp instanceof Brick) {
				Brick brick = (Brick) collisionProp;

				if (brick.mEnable) {
					mAudioHitBrick.Play();
					brick.mEnable = false;

					if (ball.mPosition.x < brick.mPosition.x + ball.mRadious
							|| ball.mPosition.x >= brick.mPosition.x + brick.mWidth - 2 * ball.mRadious)
						ball.mVelocity.x = -ball.mVelocity.x;
					else
						ball.mVelocity.y = -ball.mVelocity.y;

					currentScore += 100;
					if (highScore < currentScore)
						highScore = currentScore;

					if (brick.mType != Brick.normal) {
						switch (brick.mType) {

						case Brick.multipleBall:
							mAudioLaugh[(int) (Math.random() * 7)].Play();
							multipleBallSource = ball;
							multipleBallEnable = true;
							break;

						case Brick.fastBall:

							for (var b : mBalls) {
								b.mVelocity.x *= 1.25;
								b.mVelocity.y *= 1.25;
							}

							mAudioSteamPack[(int) (Math.random() * 2)].Play();

							break;

						case Brick.shortBar:
							mBar.mWidth *= 0.8;
							mAudioItemShortBar.Play();
							break;

						case Brick.longBar:
							mBar.mWidth *= 1.25;
							mAudioItemLongBar.Play();
							break;

						case Brick.addLife:
							if (mBonusBall < 10) {
								mBonusBall++;
								mAudioItemBonusBall.Play();
							}
							break;
						}
					}
				}
			}
		}

		if (multipleBallEnable) {
			AddBall(new Vec2f(multipleBallSource.mPosition.x, multipleBallSource.mPosition.y),
					new Vec2f(multipleBallSource.mVelocity.x * (Math.random() + 0.5),
							multipleBallSource.mVelocity.y * (Math.random() + 0.5)));

			AddBall(new Vec2f(multipleBallSource.mPosition.x, multipleBallSource.mPosition.y),
					new Vec2f(multipleBallSource.mVelocity.x * (Math.random() + 0.5),
							multipleBallSource.mVelocity.y * (Math.random() + 0.5)));

			multipleBallEnable = false;
		}

		mBar.Update(dt);
		mBar.Collision(container);
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
		mStage++;
		mBonusBall += 3;
		return true;
	}

	void Draw(Graphics2D g) {

		for (int i = 0; i < mBalls.size(); i++)
			if (mBalls.get(i).mEnable == false)
				mBalls.remove(i);

		for (int i = 0; i < mBricks.size(); i++) {
			if (mBricks.get(i).mRemovable)
				mBricks.remove(i);
		}

		for (int i = 0; i < mBonusBall; i++) {

			if (mBar == null)
				return;

			int x = (int) mBar.mPosition.x + i * 10;
			int y = (int) mBar.mPosition.y + 20;
			g.setPaint(new GradientPaint(x, y, new Color(200, 100, 100), x + 7, y + 7, Color.black));
			g.fillOval(x, y, 7, 7);
		}

		for (var ball : mBalls)
			ball.Draw(g);

		for (var brick : mBricks)
			brick.Draw(g);

		mBar.Draw(g);
	}
}
