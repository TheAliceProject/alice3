package org.lgna.croquet.components;

/**
 * @author Kyle J. Harms
 */
public abstract class Stencil extends Panel {

	private LayeredPane layeredPane;
	private boolean isEventInterceptEnabled;
	private javax.swing.RepaintManager repaintManager;

	public Stencil( AbstractWindow<?> window ) {
		this.layeredPane = window.getLayeredPane();
		this.isEventInterceptEnabled = false;
		this.setOpaque( false );
	}

	protected abstract void handleMouseMoved(java.awt.event.MouseEvent e);
	protected abstract void redispatchMouseEvent(java.awt.event.MouseEvent e);
	protected abstract void paintComponentPrologue( java.awt.Graphics2D g2 );
	protected abstract void paintComponentEpilogue( java.awt.Graphics2D g2 );
	protected abstract void paintEpilogue( java.awt.Graphics2D g2 );
	protected abstract boolean contains( int x, int y, boolean superContains );
	protected abstract org.lgna.croquet.stencil.StencilLayer getStencilsLayer();

	private final java.awt.event.AWTEventListener awtEventListener = new java.awt.event.AWTEventListener() {
		public void eventDispatched(java.awt.AWTEvent event) {
			java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)event;
			e = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, Stencil.this.getAwtComponent());
			Stencil.this.handleMouseMoved( e );
		}
	};

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseDragged(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		// TODO: <kjh/> this seems wrong... this should also redispatch
		public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
			java.awt.Point p = e.getPoint();
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent().getContentPane(), p.x, p.y);
			if (component != null) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(e.getComponent(), p, component);
				component.dispatchEvent(new java.awt.event.MouseWheelEvent(component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()));
			}
		}
	};

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new javax.swing.JPanel() {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				java.awt.Paint prevPaint = g2.getPaint();
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				try {
					Stencil.this.paintComponentPrologue( g2 );
					super.paintComponent( g2 );
					Stencil.this.paintComponentEpilogue( g2 );
				} finally {
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					g2.setPaint( prevPaint );
				}
			}

			@Override
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				Stencil.this.paintEpilogue( g2 );
			}

			@Override
			public boolean contains(int x, int y) {
				return Stencil.this.contains( x, y, super.contains( x, y ) );
			}
		};
	}

	public boolean isEventInterceptEnabled() {
		return this.isEventInterceptEnabled;
	}

	public void setEventInterceptEnabled( boolean isEventInterceptEnabled ) {
		if( this.isEventInterceptEnabled != isEventInterceptEnabled ) {
			if( this.isEventInterceptEnabled ) {
				this.layeredPane.getAwtComponent().removeMouseListener( this.mouseListener );
				this.layeredPane.getAwtComponent().removeMouseMotionListener( this.mouseMotionListener );
				this.layeredPane.getAwtComponent().removeMouseWheelListener( this.mouseWheelListener );
			}
			this.isEventInterceptEnabled = isEventInterceptEnabled;
			if( this.isEventInterceptEnabled ) {
				this.layeredPane.getAwtComponent().addMouseListener( this.mouseListener );
				this.layeredPane.getAwtComponent().addMouseMotionListener( this.mouseMotionListener );
				this.layeredPane.getAwtComponent().addMouseWheelListener( this.mouseWheelListener );
			}
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.repaintManager = javax.swing.RepaintManager.currentManager( this.getAwtComponent() );
		javax.swing.RepaintManager.setCurrentManager( new org.lgna.croquet.stencil.StencilRepaintManager( this ) );
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}

	@Override
	protected void handleUndisplayable() {
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		javax.swing.RepaintManager.setCurrentManager( this.repaintManager );
		super.handleUndisplayable();
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
		return null;
	}

	public boolean isShowing() {
		return this.layeredPane.contains( this );
	}

	public void setShowing( boolean isShowing ) {
		if ( this.isShowing() != isShowing ) {
			if ( isShowing ) {
				this.addToLayeredPane();
			} else {
				this.removeFromLayeredPane();
			}
		}
	}

	private void addToLayeredPane() {
		this.layeredPane.addToLayeredPane( this, this.getStencilsLayer().getLayer() );
	}

	private void removeFromLayeredPane() {
		this.layeredPane.removeFromLayeredPane( this );
	}
}
