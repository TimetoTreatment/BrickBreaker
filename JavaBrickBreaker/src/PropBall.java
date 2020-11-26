import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;

class Ball extends Prop {

	Vec2f mDefaultVelocity = new Vec2f(250, -500);
	Vec2f mVelocity;
	double mRadious;

	Ball(Vec2f position, Vec2f velocity) {
		super(position, Color.white);
		mRadious = 5;
		mVelocity = velocity;
	}

	Vec2f GetVelocity() {
		return mVelocity;
	}

	void Update(double dt) {
		
		mVelocity.x = mVelocity.x + mVelocity.x * 0.01 * dt;
		mVelocity.y = mVelocity.y + mVelocity.y * 0.01 * dt;
		
		mPosition.x = mPosition.x + mVelocity.x * dt;
		mPosition.y = mPosition.y + mVelocity.y * dt;
	}

	@Override
	void Draw(Graphics2D g) {
		g.setPaint(new GradientPaint((int) mPosition.x, (int) mPosition.y, Color.white,
				(int) mPosition.x + (int) mRadious * 2, (int) mPosition.y + (int) mRadious * 2,
				new Color(100, 100, 100)));
		g.fillOval((int) (mPosition.x), (int) (mPosition.y), (int) mRadious * 2, (int) mRadious * 2);

	}

	Prop Collision(Container container, ArrayList<Prop> props) {
		if (container.getHeight() <= 100)
			return null;

		if (mPosition.x < 0) {
			mPosition.x = 0;
			mVelocity.x = -mVelocity.x;
			return null;
		} else if (mPosition.x > container.getWidth() - mRadious * 2) {
			mPosition.x = container.getWidth() - mRadious * 2;
			mVelocity.x = -mVelocity.x;
			return null;
		}

		if (mPosition.y < 0) {
			mPosition.y = 0;
			mVelocity.y = -mVelocity.y;
			return null;
		} else if (mPosition.y > container.getHeight()) {
			return this;
		}

		for (var prop : props) {
			if (prop instanceof Bar) {

				Bar bar = (Bar) prop;
				if (mPosition.x > bar.mPosition.x && mPosition.x < bar.mPosition.x + bar.mWidth
						&& mPosition.y > bar.mPosition.y - mRadious * 2
						&& mPosition.y < bar.mPosition.y + bar.mHeight) {
					return bar;
				}
			} else if (prop instanceof Brick) {

				Brick brick = (Brick) prop;

				if (mPosition.x > brick.mPosition.x && mPosition.x < brick.mPosition.x + brick.mWidth
						&& mPosition.y > brick.mPosition.y && mPosition.y < brick.mPosition.y + brick.mHeight)
					return brick;
			}
		}

		return null;
	}
}
