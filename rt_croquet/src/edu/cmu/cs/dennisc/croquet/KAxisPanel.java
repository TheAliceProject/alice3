package edu.cmu.cs.dennisc.croquet;

public abstract class KAxisPanel extends KPanel {
	private int axis;
	protected KAxisPanel( int axis, KComponent... components ) {
		this.axis = axis;
		for( KComponent component : components ) {
			this.add( component );
		}
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new javax.swing.BoxLayout( jPanel, this.axis );
	}
	public void add( KComponent component ) {
		this.internalAddComponent( component );
	}
	
}
