import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

class Brick extends Prop {

	int mWidth;
	int mHeight;
	int mType = 0;
	final static int normal = 0;
	final static int multipleBall = 1;
	final static int fastBall = 2;
	final static int slowBall = 3;
	final static int shortBar = 4;
	final static int longBar = 5;
	final static int addLife = 6;
	private int mAlpha = 255;
	boolean mRemovable = false;
	boolean mAnimationCalled = false;

	Brick(Vec2f position, int width, int height, Color color) {
		super(position, color);

		mType = (int) (Math.random() * 24);
		if (mType > 6)
			mType = 0;

		mWidth = width;
		mHeight = height;
	}

	@Override
	void Update(double dt) {
		if (mEnable == false) {
			if (mAnimationCalled == false) {
				mAnimationCalled = true;

				new Thread(() -> {
					try {
						for (; mAlpha >= 0; mAlpha -= 2) {
							Thread.sleep(1);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mRemovable = true;

				}).start();
			}

			return;
		}
	}

	@Override
	void Draw(Graphics2D g) {

		int padding = 10;
		mColor = new Color(mColor.getRed(), mColor.getGreen(), mColor.getBlue(), mAlpha);

		g.setColor(mColor);
		g.fillRect((int) mPosition.x + padding, (int) mPosition.y + padding, mWidth - 2 * padding,
				mHeight - 2 * padding);

		g.setColor(new Color(100, 100, 100, mAlpha));
		g.setStroke(new BasicStroke(padding));
		g.drawRect((int) mPosition.x + padding, (int) mPosition.y + padding, mWidth - padding * 2,
				mHeight - padding * 2);

		g.setColor(Color.black);
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

		switch (mType) {

		case Brick.multipleBall:
			g.drawString("Ball X 3", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;

		case Brick.slowBall:
			g.drawString("Speed * 0.8", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;

		case Brick.fastBall:
			g.drawString("Speed * 1.25", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;

		case Brick.shortBar:
			g.drawString("Bar * 0.8", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;

		case Brick.longBar:
			g.drawString("Bar * 1.25", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;

		case Brick.addLife:
			g.drawString("Life ++", (int) mPosition.x + 5 * padding, (int) mPosition.y + 5 * padding);
			break;
		}

	}
}
