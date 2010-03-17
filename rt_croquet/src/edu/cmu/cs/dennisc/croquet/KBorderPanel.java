package edu.cmu.cs.dennisc.croquet;

public class KBorderPanel extends KPanel {
	public enum KCardinalDirection {
		NORTH( java.awt.BorderLayout.NORTH ),
		SOUTH( java.awt.BorderLayout.SOUTH ),
		EAST( java.awt.BorderLayout.EAST ),
		WEST( java.awt.BorderLayout.WEST );
		private String internal;
		private KCardinalDirection( String internal ) {
			this.internal = internal;
		}
		/*package-private*/ String getInternal() {
			return this.internal;
		}
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout();
	}
	public void add( KComponent child, KCardinalDirection cardinalDirection ) {
		this.getJComponent().add( child.getJComponent(), cardinalDirection.internal );
	}
}
