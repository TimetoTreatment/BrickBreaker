import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

class Bar extends Prop {

	private Vec2f mVelocity;
	private int mWidth;
	private int mHeight;
	private int mDirection = stop;

	Bar(Vec2f position, int width, int height, Color color) {
		super(position, color);

		mVelocity = new Vec2f(0, 0);
		mWidth = width;
		mHeight = height;
	}

	Vec2f GetVelocity() {
		return mVelocity;
	}

	int GetWidth() {
		return mWidth;
	}

	void SetWidth(int width) {
		mWidth = width;
	}

	int GetHeight() {
		return mHeight;
	}

	@Override
	void Update(double dt) {

		if (mDirection == west)
			mVelocity.x = -1000;
		else if (mDirection == east)
			mVelocity.x = 1000;
		else
			mVelocity.x = 0;

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
		mDirection = direction;
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

		if (mWidth > container.getWidth())
			mWidth = container.getWidth();
	}

}
