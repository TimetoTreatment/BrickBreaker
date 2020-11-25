import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Vector;

public class GameEntity {

	private int mLife = 4;
	private int highScore;
	private int currentScore;
	private ArrayList<Prop> mProps;
	private ArrayList<Ball> mBalls;
	private ArrayList<Brick> mBricks;
	private Bar mBar;
	private boolean mClear;
	private int mStage;
	AudioPlayer mAudioBGM = new AudioPlayer("bgm.wav");
	AudioPlayer mAudioBusted = new AudioPlayer("busted.wav");
	AudioPlayer[] mAudioLaugh = new AudioPlayer[7];

	GameEntity() {

		mAudioBGM.Play();

		highScore = 0;
		currentScore = 0;

		mProps = new ArrayList<>();
		mBalls = new ArrayList<>();
		mBricks = new ArrayList<>();
		mBar = null;

		for (int i = 0; i < 7; i++)
			mAudioLaugh[i] = new AudioPlayer("laugh" + i + ".wav");
	}

	void Initialize() {
		highScore = 0;
		currentScore = 0;

		mProps.clear();
		mBalls.clear();
		mBar = null;
		mLife = 5;
		mStage = 1;
	}

	void AddBall(Vec2f position, Vec2f velocity) {

		Ball ball = new Ball(position, velocity);

		mProps.add(ball);
		mBalls.add(ball);
	}

	void UseBall() {
		if (mLife == 0)
			return;

		mLife--;

		Ball ball = new Ball(new Vec2f(mBar.mPosition.x + mBar.mWidth / 2, mBar.mPosition.y - 10),
				new Vec2f(600 * Math.random() - 350, -600 * Math.random() - 300));

		mProps.add(ball);
		mBalls.add(ball);
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

	void Update(Container container, double dt) {

		mClear = true;

		Ball multipleBallSource = null;
		boolean multipleBallEnable = false;

		for (Brick brick : mBricks) {
			brick.Update(dt);
		}

		for (Ball ball : mBalls) {
			ball.Update(dt);
			Prop collisionProp = ball.Collision(container, mProps);

			if (collisionProp instanceof Ball)
				ball.mEnable = false;

			else if (collisionProp instanceof Bar) {
				if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth / 5)
					ball.mVelocity.x -= Math.abs(ball.mVelocity.x * 0.1);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 2 / 5)
					ball.mVelocity.x -= Math.abs(ball.mVelocity.x * 0.2);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 3 / 5)
					;
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth * 4 / 5)
					ball.mVelocity.x += Math.abs(ball.mVelocity.x * 0.2);
				else if (ball.mPosition.x <= mBar.mPosition.x + mBar.mWidth)
					ball.mVelocity.x += Math.abs(ball.mVelocity.x * 0.1);

				ball.mVelocity.x += mBar.mVelocity.x / 20;
				ball.mVelocity.y = -ball.mVelocity.y;
				ball.mPosition.y = mBar.mPosition.y - ball.mRadious * 2;

			} else if (collisionProp instanceof Brick) {
				Brick brick = (Brick) collisionProp;

				if (brick.mEnable) {
					mClear = false;
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
							multipleBallSource = ball;
							multipleBallEnable = true;
							break;

						case Brick.slowBall:

							for (var b : mBalls) {
								b.mVelocity.x *= 0.8;
								b.mVelocity.y *= 0.8;
							}
							break;

						case Brick.fastBall:

							for (var b : mBalls) {
								b.mVelocity.x *= 1.25;
								b.mVelocity.y *= 1.25;
							}

							break;

						case Brick.shortBar:
							mBar.mWidth *= 0.8;
							break;

						case Brick.longBar:
							mBar.mWidth *= 1.25;
							break;

						case Brick.addLife:
							mLife++;
							break;
						}

						mAudioLaugh[(int) (Math.random() * 7)].Play();
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
		System.out.println(mBricks.size());
		for (var brick : mBricks) {
			if (brick.mEnable == true)
				return false;
		}


		mAudioLaugh[(int) (Math.random() * 7)].Play();
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

		for (var ball : mBalls)
			ball.Draw(g);

		for (var brick : mBricks)
			brick.Draw(g);

		mBar.Draw(g);
	}
}
