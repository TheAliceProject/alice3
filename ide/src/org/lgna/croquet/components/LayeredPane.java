package org.lgna.croquet.components;

/**
 * @author Kyle J. Harms
 */
public final class LayeredPane extends JComponent<javax.swing.JLayeredPane> {

	private final java.awt.event.ComponentListener componentListener;
	private final javax.swing.JLayeredPane layeredPane;

	/*package-private*/ LayeredPane( javax.swing.JLayeredPane layeredPane ) {
		this.layeredPane = layeredPane;
		this.componentListener = new java.awt.event.ComponentListener() {
			public void componentResized( java.awt.event.ComponentEvent e ) {
				LayeredPane.this.revalidateAndRepaint();
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
		};
	}

	public void addToLayeredPane( Component<?> component, int layer ) {
		javax.swing.JLayeredPane layeredPane = this.getAwtComponent();
		layeredPane.add( component.getAwtComponent() );
		layeredPane.setLayer( component.getAwtComponent(), layer );
		layeredPane.repaint();
	}

	public void removeFromLayeredPane( Component<?> component ) {
		javax.swing.JLayeredPane layeredPane = this.getAwtComponent();
		layeredPane.remove( component.getAwtComponent() );
		layeredPane.repaint();
	}

	public boolean contains( Component<?> component ) {
		for ( Component<?> c : this.getComponents() ) {
			if ( c == component ) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected javax.swing.JLayeredPane createAwtComponent() {
		return this.layeredPane;
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.layeredPane.addComponentListener( this.componentListener );
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		this.layeredPane.removeComponentListener( this.componentListener );
	}
}
