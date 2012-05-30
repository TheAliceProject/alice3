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
				LayeredPane.this.resizeStencils();
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
		this.resizeStencils();
		layeredPane.repaint();
	}

	public void removeFromLayeredPane( Component<?> component ) {
		javax.swing.JLayeredPane layeredPane = this.getAwtComponent();
		layeredPane.remove( component.getAwtComponent() );
		this.resizeStencils();
		layeredPane.repaint();
	}

	public boolean contains( Component<?> component ) {
		for ( java.awt.Component c : this.getAwtComponent().getComponents() ) {
			if ( c == component.getAwtComponent() ) {
				return true;
			}
		}
		return false;
	}

	private void resizeStencils() {
		for( Component<?> component : this.getComponents() ) {
			if( component instanceof Stencil ) {
				component.getAwtComponent().setBounds(0, 0, this.layeredPane.getWidth(), this.layeredPane.getHeight());
			}
		}
	}

	@Override
	protected javax.swing.JLayeredPane createAwtComponent() {
		return this.layeredPane;
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.resizeStencils();
		this.layeredPane.addComponentListener( this.componentListener );
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		this.layeredPane.removeComponentListener( this.componentListener );
	}
}
