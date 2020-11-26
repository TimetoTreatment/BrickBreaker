import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

class Brick extends Prop {

	final static int normal = 0;
	final static int multipleBall = 1;
	final static int fastBall = 2;
	final static int longBar = 3;
	final static int shortBar = 4;
	final static int addLife = 5;
	final static int ultimate = 6;

	private int mWidth;
	private int mHeight;
	private int mType = 0;
	private int mAlpha = 254;
	private boolean mRemovable = false;
	private boolean mAnimationCalled = false;

	int GetWidth() {
		return mWidth;
	}

	int GetHeight() {
		return mHeight;
	}

	int GetType() {
		return mType;
	}

	boolean IsRemovable() {
		return mRemovable;
	}

	Brick(Vec2f position, int width, int height, Color color) {
		super(position, color);

		int random = (int) (Math.random() * 100);

		if (random < 6) {
			mType = multipleBall;
			mColor = new Color(200, 200, 0);
		} else if (random < 12) {
			mType = fastBall;
			mColor = new Color(200, 0, 0);
		} else if (random < 18) {
			mType = longBar;
			mColor = new Color(0, 200, 0);
		} else if (random < 24) {
			mType = shortBar;
			mColor = new Color(0, 0, 200);
		} else if (random < 30) {
			mType = addLife;
			mColor = new Color(200, 100, 0);
		} else if (random < 31) {
			mType = ultimate;
			mColor = new Color(100, 100, 100);
		} else {
			mType = normal;
			mColor = Color.white;
		}

		mWidth = width;
		mHeight = height;
	}

	@Override
	void Update(double dt) {
		if (IsEnable() == false) {
			if (mAnimationCalled == false) {
				mAnimationCalled = true;

				new Thread(() -> {
					try {
						for (; mAlpha > 0; mAlpha -= 2) {
							Thread.sleep(1);
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
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

		int padding = mHeight / 10;
		mColor = new Color(mColor.getRed(), mColor.getGreen(), mColor.getBlue(), mAlpha);

		g.setColor(mColor);
		g.fillRect((int) mPosition.x + padding, (int) mPosition.y + padding, mWidth - 2 * padding,
				mHeight - 2 * padding);

		g.setPaint(new GradientPaint((int) mPosition.x, (int) mPosition.y, mColor, (int) mPosition.x,
				(int) mPosition.y + mHeight, new Color(50, 50, 50, mAlpha)));
		g.setStroke(new BasicStroke(padding));
		g.drawRect((int) mPosition.x + padding, (int) mPosition.y + padding, mWidth - padding * 2,
				mHeight - padding * 2);

		g.setColor(new Color(100, 100, 100, mAlpha));
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
	}
}
