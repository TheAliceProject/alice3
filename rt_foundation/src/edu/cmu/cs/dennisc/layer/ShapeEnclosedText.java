package edu.cmu.cs.dennisc.layer;

public abstract class ShapeEnclosedText extends Text {
	protected abstract edu.cmu.cs.dennisc.color.Color4f getDefaultFillColor();
	protected abstract edu.cmu.cs.dennisc.color.Color4f getDefaultOutlineColor();
	public edu.cmu.cs.dennisc.color.property.Color4fProperty fillColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, this.getDefaultFillColor(), true );
	public edu.cmu.cs.dennisc.color.property.Color4fProperty outlineColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, this.getDefaultOutlineColor(), true );
}
