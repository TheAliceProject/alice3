package edu.cmu.cs.dennisc.layer;

public abstract class Bubble extends ShapeEnclosedText {
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultTextColor() {
		return edu.cmu.cs.dennisc.color.Color4f.BLACK;
	}
	@Override
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultFillColor() {
		return edu.cmu.cs.dennisc.color.Color4f.WHITE;
	}
	@Override
	protected edu.cmu.cs.dennisc.color.Color4f getDefaultOutlineColor() {
		return edu.cmu.cs.dennisc.color.Color4f.BLACK;
	}
}
