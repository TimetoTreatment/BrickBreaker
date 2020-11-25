import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.util.Vector;

abstract class Prop {

	boolean mEnable;
	protected Vec2f mPosition;
	protected Color mColor;

	Prop(Vec2f position, Color color) {
		mPosition = position;
		mColor = color;
		mEnable = true;
	}

	abstract void Update(double dt);
	abstract void Draw(Graphics2D g);
}
