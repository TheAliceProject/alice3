package org.lgna.croquet.components;

/**
 * @author Kyle J. Harms
 */
public class LayeredPaneComponent extends JComponent<javax.swing.JLayeredPane> {

	private final java.awt.event.ComponentListener componentListener;
	private final javax.swing.JLayeredPane layeredPane;

	public LayeredPaneComponent() {
		this( new javax.swing.JLayeredPane() );
	}

	/*package-private*/ LayeredPaneComponent( javax.swing.JLayeredPane layeredPane ) {
		this.layeredPane = layeredPane;
		this.componentListener = new java.awt.event.ComponentListener() {
			public void componentResized( java.awt.event.ComponentEvent e ) {
				LayeredPaneComponent.this.getAwtComponent().setBounds( e.getComponent().getBounds() );
				LayeredPaneComponent.this.revalidateAndRepaint();
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
		};
		this.layeredPane.addComponentListener( this.componentListener );
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

	@Override
	protected javax.swing.JLayeredPane createAwtComponent() {
		return this.layeredPane;
	}
}
