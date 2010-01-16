package edu.cmu.cs.dennisc.layer;

public abstract class Text extends Graphic {
	public edu.cmu.cs.dennisc.property.StringProperty text = new edu.cmu.cs.dennisc.property.StringProperty( this, "" );
	public edu.cmu.cs.dennisc.color.property.Color4fProperty textColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, this.getDefaultTextColor(), false );
	protected abstract edu.cmu.cs.dennisc.color.Color4f getDefaultTextColor();
}
