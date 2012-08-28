package org.lgna.croquet.components;

/**
 * @author Kyle J. Harms
 */
public abstract class Stencil extends Panel implements LayeredPane.ResizeListener {
	public enum LayerId {
		BELOW_POPUP_LAYER( javax.swing.JLayeredPane.POPUP_LAYER - 1 ),
		ABOVE_POPUP_LAYER( javax.swing.JLayeredPane.POPUP_LAYER + 1 );

		private int stencilLayer;

		private LayerId( int stencilLayer ) {
			this.stencilLayer = stencilLayer;
		}

		public int getLayer() {
			return this.stencilLayer;
		}

		public boolean isAboveStencil() {
			return this.stencilLayer < javax.swing.JLayeredPane.POPUP_LAYER;
		}

		public boolean isBelowStencil() {
			return this.stencilLayer > javax.swing.JLayeredPane.POPUP_LAYER;
		}
	}

	private final LayeredPane layeredPane;
	private boolean isEventInterceptEnabled;
	private javax.swing.RepaintManager repaintManager;

	private final class StencilRepaintManager extends javax.swing.RepaintManager {
		private org.lgna.croquet.components.Stencil stencil;

		public StencilRepaintManager( org.lgna.croquet.components.Stencil stencil ) {
			this.stencil = stencil;
		}

		@Override
		public void addDirtyRegion( javax.swing.JComponent c, int x, int y, int w, int h ) {
			super.addDirtyRegion( c, x, y, w, h );
			final javax.swing.JComponent stencil = this.stencil.getAwtComponent();
			if( ( stencil == c ) || stencil.isAncestorOf( c ) ) {
				//pass
			} else {
				java.awt.Component srcRoot = javax.swing.SwingUtilities.getRoot( c );
				java.awt.Component dstRoot = javax.swing.SwingUtilities.getRoot( stencil );

				if( ( srcRoot != null ) && ( srcRoot == dstRoot ) ) {
					java.awt.Rectangle rect = new java.awt.Rectangle( x, y, w, h );
					java.awt.Rectangle visibleRect = rect.intersection( c.getVisibleRect() );
					if( ( visibleRect.width != 0 ) && ( visibleRect.height != 0 ) ) {
						final java.awt.Rectangle rectAsSeenByStencil = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.convertRectangle( c, visibleRect, stencil );
						javax.swing.SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								StencilRepaintManager.super.addDirtyRegion( stencil, rectAsSeenByStencil.x, rectAsSeenByStencil.y, rectAsSeenByStencil.width, rectAsSeenByStencil.height );
							}
						} );
					}
				}
			}
		}
	}

	private final java.awt.event.AWTEventListener awtEventListener = new java.awt.event.AWTEventListener() {
		public void eventDispatched( java.awt.AWTEvent event ) {
			java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)event;
			e = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, Stencil.this.getAwtComponent() );
			Stencil.this.handleMouseMoved( e );
		}
	};

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		public void mousePressed( java.awt.event.MouseEvent e ) {
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		public void mouseDragged( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
		}
	};

	public Stencil( AbstractWindow<?> window ) {
		this.layeredPane = window.getLayeredPane();
		this.isEventInterceptEnabled = false;
		this.setOpaque( false );
	}

	protected abstract void handleMouseMoved( java.awt.event.MouseEvent e );

	protected abstract void paintComponentPrologue( java.awt.Graphics2D g2 );

	protected abstract void paintComponentEpilogue( java.awt.Graphics2D g2 );

	protected abstract void paintEpilogue( java.awt.Graphics2D g2 );

	protected abstract boolean contains( int x, int y, boolean superContains );

	protected abstract LayerId getStencilsLayer();

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new javax.swing.JPanel() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
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
			public void paint( java.awt.Graphics g ) {
				super.paint( g );
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				Stencil.this.paintEpilogue( g2 );
			}

			@Override
			public boolean contains( int x, int y ) {
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
				this.getAwtComponent().removeMouseListener( this.mouseListener );
				this.getAwtComponent().removeMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().removeMouseWheelListener( this.mouseWheelListener );
			}
			this.isEventInterceptEnabled = isEventInterceptEnabled;
			if( this.isEventInterceptEnabled ) {
				this.getAwtComponent().addMouseListener( this.mouseListener );
				this.getAwtComponent().addMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().addMouseWheelListener( this.mouseWheelListener );
			}
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.repaintManager = javax.swing.RepaintManager.currentManager( this.getAwtComponent() );
		javax.swing.RepaintManager.setCurrentManager( new StencilRepaintManager( this ) );
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}

	@Override
	protected void handleUndisplayable() {
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		javax.swing.RepaintManager.setCurrentManager( this.repaintManager );
		super.handleUndisplayable();
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return null;
	}

	public boolean isStencilShowing() {
		return this.layeredPane.contains( this );
	}

	public void setStencilShowing( boolean isShowing ) {
		if( this.isStencilShowing() != isShowing ) {
			if( isShowing ) {
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

	public void layeredPaneResized( int width, int height ) {
		this.getAwtComponent().setBounds( 0, 0, width, height );
	}
}
