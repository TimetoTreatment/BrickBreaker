import java.awt.Color;
import java.awt.Graphics2D;

abstract class Prop {

	final static int stop = 0;
	final static int north = 1;
	final static int northEast = 2;
	final static int east = 3;
	final static int southEast = 4;
	final static int south = 5;
	final static int southWest = 6;
	final static int west = 7;
	final static int northWest = 8;

	protected boolean mEnable;
	protected Vec2f mPosition;
	protected Color mColor;

	Prop(Vec2f position, Color color) {
		mPosition = position;
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
