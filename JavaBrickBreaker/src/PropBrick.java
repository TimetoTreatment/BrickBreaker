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
	final static int longBar = 3;
	final static int shortBar = 4;
	final static int addLife = 5;
	private int mAlpha = 255;
	boolean mRemovable = false;
	boolean mAnimationCalled = false;

	Brick(Vec2f position, int width, int height, Color color) {
		super(position, color);

		mType = (int) (Math.random() * 20);
		if (mType > 6)
			mType = 0;

		if (mType == 1)
			mColor = new Color(200, 200, 0);

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
						for (; mAlpha > 5; mAlpha -= 2) {
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

		int padding = 10;
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

		switch (mType) {

		case Brick.multipleBall:
			g.drawString("Multiple Ball", (int) mPosition.x + 40, (int) mPosition.y + 55);
			break;

		case Brick.fastBall:
			g.drawString("Speed Up", (int) mPosition.x + 50, (int) mPosition.y + 55);
			break;

		case Brick.shortBar:
			g.drawString("Short Bar", (int) mPosition.x + 45, (int) mPosition.y + 55);
			break;

		case Brick.longBar:
			g.drawString("Long Bar", (int) mPosition.x + 50, (int) mPosition.y + 55);
			break;

		case Brick.addLife:
			g.drawString("Bonus ++", (int) mPosition.x + 50, (int) mPosition.y + 55);
			break;
		}
	}
}
