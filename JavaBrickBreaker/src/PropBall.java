import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;

class Ball extends Prop {

	final static Vec2f defaultVelocity = new Vec2f(250, -500);
	private Vec2f mVelocity;
	private double mRadious;

	Ball(Vec2f position, Vec2f velocity) {
		super(position, Color.white);
		mRadious = 5;
		mVelocity = velocity;
	}

	@Override
	void Update(double dt) {
		mVelocity.x = mVelocity.x + mVelocity.x * 0.01 * dt;
		mVelocity.y = mVelocity.y + mVelocity.y * 0.01 * dt;
		
		mPositionPrev.x = mPosition.x;
		mPositionPrev.y = mPosition.y;

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

		if (mPosition.x < -100 || mPosition.x > Config.width + 100)
			return this;

		if (mPosition.y < -100)
			return this;

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

		for (Prop prop : props) {
			if (prop instanceof Bar) {

				Bar bar = (Bar) prop;
				if (mPosition.x > bar.GetPosition().x && mPosition.x < bar.GetPosition().x + bar.GetWidth()
						&& mPosition.y > bar.GetPosition().y - mRadious * 2
						&& mPosition.y < bar.GetPosition().y + bar.GetHeight()) {
					return bar;
				}
			} else if (prop instanceof Brick) {

				Brick brick = (Brick) prop;

				if (brick.IsEnable()) {
					if (mPosition.x + mRadious * 2 >= brick.GetPosition().x
							&& mPosition.x <= brick.GetPosition().x + brick.GetWidth()
							&& mPosition.y + mRadious * 2 >= brick.GetPosition().y
							&& mPosition.y <= brick.GetPosition().y + brick.GetHeight()) {

						if (mPositionPrev.x + mRadious * 2 <= brick.GetPosition().x
								|| mPositionPrev.x >= brick.GetPosition().x + brick.GetWidth())
							mVelocity.x = -mVelocity.x;
						else
							mVelocity.y = -mVelocity.y;

						return brick;

					}
				}
			}
		}

		return null;
	}

	Vec2f GetVelocity() {
		return mVelocity;
	}

	double GetRadious() {
		return mRadious;
	}
}
