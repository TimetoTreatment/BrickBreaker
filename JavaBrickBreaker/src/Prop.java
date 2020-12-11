import java.awt.Color;
import java.awt.Graphics2D;

abstract class Prop {

	final static int stop = 0;
	final static int east = 1;
	final static int west = 2;

	protected boolean mEnable;
	protected Vec2f mPosition;
	protected Vec2f mPositionPrev;
	protected Color mColor;

	Prop(Vec2f position, Color color) {
		mPosition = position;
		mPositionPrev = new Vec2f(position.x, position.y);
		mColor = color;
		mEnable = true;
	}

	abstract void Update(double dt);

	abstract void Draw(Graphics2D g);

	Vec2f GetPosition() {
		return mPosition;
	}

	boolean IsEnable() {
		return mEnable;
	}

	void SetEnable(boolean enable) {
		mEnable = enable;
	}
}
