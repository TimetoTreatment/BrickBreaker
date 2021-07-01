import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameEntity {

    private int mStage;
    private int mHighScore;
    private int mCurrentScore;
    private int mBonusBall;
    private int mBallQueue;
    private final ArrayList<Prop> mProps;
    private final ArrayList<Ball> mBalls;
    private final ArrayList<Brick> mBricks;
    private Bar mBar;
    private final AudioPlayer mAudioBGM = new AudioPlayer("bgm.wav", 1);
    private final AudioPlayer mAudioBusted = new AudioPlayer("busted.wav", 1);
    private final AudioPlayer mAudioClear = new AudioPlayer("clear.wav", 1);
    private final AudioPlayer mAudioHighScore = new AudioPlayer("highScore.wav", 1);
    private final AudioPlayer[] mAudioLaugh = new AudioPlayer[7];
    private final AudioPlayer[] mAudioSteamPack = new AudioPlayer[2];
    private final AudioPlayer[] mAudioDeath = new AudioPlayer[2];
    private final AudioPlayer mAudioLaunch = new AudioPlayer("launch.wav", 10);
    private final AudioPlayer mAudioHitBrick = new AudioPlayer("hitBrick.wav", 10);
    private final AudioPlayer mAudioHitBar = new AudioPlayer("hitBar.wav", 10);
    private final AudioPlayer mAudioItemLongBar = new AudioPlayer("itemLongBar.wav", 5);
    private final AudioPlayer mAudioItemShortBar = new AudioPlayer("itemShortBar.wav", 5);
    private final AudioPlayer mAudioItemBonusBall = new AudioPlayer("itemBonusBall.wav", 5);
    private final AudioPlayer mAudioItemUltimate = new AudioPlayer("itemUltimate.wav", 5);

    GameEntity() {
        mProps = new ArrayList<>();
        mBalls = new ArrayList<>();
        mBricks = new ArrayList<>();
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
        mStage = Config.defaultStage;
        mBallQueue = 0;
        mBonusBall = 5;
    }

    void Update(Container container, double dt) {
        if(mBar == null)
            return;

        Ball multipleBallSource = null;
        boolean multipleBallEnable = false;
        boolean fastBallEnable = false;

        for (Ball ball : mBalls) {
            ball.Update(dt);
            Prop collisionProp = ball.Collision(container, mProps);

            if (collisionProp instanceof Ball) {
                ball.SetEnable(false);
                mAudioDeath[(int) (Math.random() * 2)].Play();
            } else if (collisionProp instanceof Bar) {
                mAudioHitBar.Play();
                if (ball.GetPosition().x <= mBar.GetPosition().x + mBar.GetWidth() / 5)
                    ball.GetVelocity().x -= Math.abs(Ball.defaultVelocity.x);
                else if (ball.GetPosition().x <= mBar.GetPosition().x + mBar.GetWidth() * 2 / 5)
                    ball.GetVelocity().x -= Math.abs(Ball.defaultVelocity.x * 0.5);
                else if (ball.GetPosition().x >= mBar.GetPosition().x + mBar.GetWidth() * 4 / 5)
                    ball.GetVelocity().x += Math.abs(Ball.defaultVelocity.x);
                else if (ball.GetPosition().x >= mBar.GetPosition().x + mBar.GetWidth() * 3 / 5)
                    ball.GetVelocity().x += Math.abs(Ball.defaultVelocity.x * 0.5);

                ball.GetVelocity().x += mBar.GetVelocity().x / 10;
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
                        case Brick.multipleBall -> {
                            mAudioLaugh[(int) (Math.random() * 7)].Play();
                            multipleBallSource = ball;
                            multipleBallEnable = true;
                        }
                        case Brick.fastBall -> {
                            fastBallEnable = true;
                            mAudioSteamPack[(int) (Math.random() * 2)].Play();
                        }
                        case Brick.shortBar -> {
                            mBar.SetWidth((int) (mBar.GetWidth() * 0.8));
                            mAudioItemShortBar.Play();
                        }
                        case Brick.longBar -> {
                            mBar.SetWidth((int) (mBar.GetWidth() * 1.25));
                            mAudioItemLongBar.Play();
                        }
                        case Brick.addLife -> {
                            mBonusBall++;
                            mAudioItemBonusBall.Play();
                        }
                        case Brick.ultimate -> {
                            mAudioItemUltimate.Play();
                            mBallQueue += 10;
                        }
                    }
                }
            }
        }

        for (; mBallQueue > 0; mBallQueue--) {
            AddBall(new Vec2f(mBar.GetPosition().x + mBar.GetWidth() / 2, mBar.GetPosition().y - 10),
                    new Vec2f(
                            mBar.GetVelocity().x / 2 + Ball.defaultVelocity.x * Math.random()
                                    - Ball.defaultVelocity.x / 2,
                            -Ball.defaultVelocity.y * Math.random() - Ball.defaultVelocity.y));
        }

        if (fastBallEnable) {
            for (Ball b : mBalls) {
                b.GetVelocity().x *= 1.15;
                b.GetVelocity().y *= 1.15;
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

        mBalls.removeIf(ball -> !ball.IsEnable());
        mBricks.removeIf(Brick::IsRemovable);
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

        for (Ball mBall : mBalls) mBall.Draw(g);
        for (Brick mBrick : mBricks) mBrick.Draw(g);

        if (mBar != null)
            mBar.Draw(g);
    }

    void NewStage() {
        mProps.clear();
        mBalls.clear();
        mBricks.clear();
        mBar = null;

        final int maxWidth = Config.width - 30;
        final int maxHeight = Config.height / 2;
        final int maxCol = mStage + 2;
        final int maxRow = mStage + 1;
        int width = maxWidth / maxCol;
        int height = maxHeight / maxRow;

        for (int row = 0; row < maxRow; row++)
            for (int col = 0; col < maxCol; col++)
                AddBrick(new Vec2f(7 + col * width, 7 + row * height * 0.99), width, height, Color.white);

        AddBar(new Vec2f(390, 750), 400, 20, Color.white);
        AddBall(new Vec2f(600, 740), new Vec2f(Ball.defaultVelocity.x, Ball.defaultVelocity.y));
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
        if (mBar != null)
            mBar.Move(direction);
    }

    void AddBrick(Vec2f position, int width, int height, Color color) {
        Brick newBrick = new Brick(position, width, height, color);
        mProps.add(newBrick);
        mBricks.add(newBrick);
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

    void PlayBGM() {
        mAudioBGM.SetLoop(true);
    }

    void PlayHighScore() {
        mAudioHighScore.Play();
    }

    int GetHighScore() {
        return mHighScore;
    }

    int GetCurrentScore() {
        return mCurrentScore;
    }
}
