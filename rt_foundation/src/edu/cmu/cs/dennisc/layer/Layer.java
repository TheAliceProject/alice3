package edu.cmu.cs.dennisc.layer;

public class Layer {
	private java.util.List< Graphic > graphics = java.util.Collections.synchronizedList( new java.util.LinkedList< Graphic >() );
	public void addGraphic( Graphic graphic ) {
		this.graphics.add( graphic );
	}
	public void removeGraphic( Graphic graphic ) {
		this.graphics.remove( graphic );
	}
	public Iterable<Graphic> getGraphics() {
		return this.graphics;
	}
}
