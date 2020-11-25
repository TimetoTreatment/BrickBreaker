import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.Vector;

class Bar extends Prop {

	Vec2f mVelocity;
	private Vec2f mAcceleration;
	int mWidth;
	int mHeight;

	final static int left = 0;
	final static int right = 1;
	final static int stop = 2;

	Bar(Vec2f position, int width, int height, Color color) {
		super(position, color);

		mVelocity = new Vec2f(0, 0);
		mAcceleration = new Vec2f(0, 0);
		mWidth = width;
		mHeight = height;
	}

	@Override
	void Update(double dt) {
		mVelocity.x = mVelocity.x + mAcceleration.x * dt;
		mVelocity.y = mVelocity.y + mAcceleration.y * dt;

		mPosition.x = mPosition.x + mVelocity.x * dt;
		mPosition.y = mPosition.y + mVelocity.y * dt;
	}

	@Override
	void Draw(Graphics2D g) {

		g.setColor(Color.darkGray);
		g.fillRect((int) mPosition.x, (int) mPosition.y, mWidth, mHeight * 1 / 3);

		g.setPaint(new GradientPaint((int) mPosition.x + mWidth / 2, (int) mPosition.y + mHeight / 3, Color.lightGray,
				(int) mPosition.x + (int) mWidth / 2, (int) mPosition.y + mHeight, Color.black));
		g.fillRect((int) mPosition.x, (int) mPosition.y + mHeight / 3, mWidth, mHeight * 2 / 3);
	}

	void Move(int direction) {
		if (direction == left)
			mVelocity.x = -1000;
		else if (direction == right)
			mVelocity.x = 1000;
		else
			mVelocity.x = 0;
	}

	void Collision(Container container) {
		if (container.getHeight() <= 100)
			return;

		if (mPosition.x < 0) {
			mPosition.x = 0;
			mVelocity.x = 0;
		} else if (mPosition.x > container.getWidth() - mWidth) {
			mPosition.x = container.getWidth() - mWidth;
			mVelocity.x = 0;
		}
	}

}
