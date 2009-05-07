package stencil;

import java.awt.Dimension;
import java.awt.Rectangle;

import edu.cmu.cs.dennisc.print.PrintUtilities;

public class Stencil extends edu.cmu.cs.dennisc.swing.CornerSpringPane {
	// private java.util.List< Hole > holes = new java.util.LinkedList< Hole >();
	private java.util.List< HoleGroup > holeGroups = new java.util.LinkedList< HoleGroup >();
	protected java.awt.Container container;

	public Stencil( java.awt.Container container ) {
		this.container = container;
		this.container.addComponentListener( new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized( java.awt.event.ComponentEvent e ) {
				Stencil.this.synchronizeSize();
			}
		} );
		this.synchronizeSize();
		this.setBackground( new java.awt.Color( 171, 236, 131, 191 ) );
		this.setForeground( new java.awt.Color( 114, 157, 88 ) );
		setOpaque( false );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
		} );
		this.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
			public void mouseMoved( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
		} );
	}

	private static java.awt.Component getFirstMouseListeningInclusiveAncestor( java.awt.Component root, java.awt.Component descendant ) {
		java.awt.Component rv = descendant;
		while( rv != null ) {
			if( rv.getMouseListeners().length > 0 || rv.getMouseMotionListeners().length > 0 ) {
				break;
			}
			if( rv == root ) {
				rv = null;
				break;
			}
			rv = rv.getParent();
		}
		return rv;
	}
	
	protected static javax.swing.JScrollPane findFirstScrollPaneAncestor( java.awt.Component descendant ) {
		if (descendant != null) {
			return edu.cmu.cs.dennisc.awt.ComponentUtilities.findFirstAncestor( descendant, false, javax.swing.JScrollPane.class );
		} else return null;
	}

	private void redispatch( java.awt.event.MouseEvent e ) {
		java.awt.Point p = e.getPoint();
		if( this.contains( p.x, p.y ) ) {
			//pass
		} else {
			java.awt.Component deepestComponent = javax.swing.SwingUtilities.getDeepestComponentAt( this.container, p.x, p.y );
			java.awt.Component deepestMouseListener = getFirstMouseListeningInclusiveAncestor( this.container, deepestComponent );
			if( deepestMouseListener != null ) {
				java.awt.event.MouseEvent me = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( this, e, deepestMouseListener );
				deepestMouseListener.dispatchEvent( me );
			}
		}
	}
	
	public void synchronizeSize() {
		this.setSize( this.container.getSize() );
		this.revalidate();
	}


	public void addHoleGroup( HoleGroup holeGroup ) {
		synchronized( this.holeGroups ) {
			this.holeGroups.add( holeGroup );

			for( Hole hole : holeGroup.getHoles() ) {
				final java.awt.Component component = hole.getComponent();
				final java.awt.Component proxy = hole.getProxy();
				
				if (component != null) {
					java.awt.Point p = component.getLocation();
					component.addComponentListener( new java.awt.event.ComponentListener() {
						private void handleChange( java.awt.event.ComponentEvent e ) {
							Stencil.this.revalidate();
							Stencil.this.repaint();
						}
	
						public void componentShown( java.awt.event.ComponentEvent e ) {
							handleChange( e );
						}
	
						public void componentHidden( java.awt.event.ComponentEvent e ) {
							handleChange( e );
						}
	
						public void componentMoved( java.awt.event.ComponentEvent e ) {
							handleChange( e );
						}
	
						public void componentResized( java.awt.event.ComponentEvent e ) {
							handleChange( e );
						}
					} );
					javax.swing.JScrollPane scrollPane = findFirstScrollPaneAncestor( hole.getComponent() );
					if( scrollPane != null ) {
						javax.swing.JViewport viewport = scrollPane.getViewport();
						viewport.addChangeListener( new javax.swing.event.ChangeListener() {
							public void stateChanged( javax.swing.event.ChangeEvent arg0 ) {
								Stencil.this.revalidate();
								Stencil.this.repaint();
							}						
						} );
					}
					this.add( proxy, Horizontal.WEST, p.x, Vertical.NORTH, p.y );
					int pad = hole.getPad();
					java.awt.Component leadingDecorator = hole.getLeadingDecorator();
					if( leadingDecorator != null ) {
						this.add( leadingDecorator );
						this.getSpringLayout().putConstraint( javax.swing.SpringLayout.EAST, leadingDecorator, -pad, javax.swing.SpringLayout.WEST, proxy );
						this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, leadingDecorator, 0, javax.swing.SpringLayout.NORTH, proxy );
					}
					java.awt.Component trailingDecorator = hole.getTrailingDecorator();
					if( trailingDecorator != null ) {
						this.add( trailingDecorator );
						this.getSpringLayout().putConstraint( javax.swing.SpringLayout.WEST, trailingDecorator, pad*2, javax.swing.SpringLayout.EAST, proxy );
						this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, trailingDecorator, 0, javax.swing.SpringLayout.NORTH, proxy );
					}
				}
			}
			java.awt.Component northDecorator = holeGroup.getNorthDecorator();
			if( northDecorator != null ) {
				this.add( northDecorator );
			}
			this.revalidate();
			this.repaint();
		}
	}

	public void removeHoleGroup( HoleGroup holeGroup ) {
		synchronized( this.holeGroups ) {
			this.holeGroups.remove( holeGroup );
			for( Hole hole : holeGroup.getHoles() ) {
				final java.awt.Component proxy = hole.getProxy();
				this.remove( proxy );
				java.awt.Component leadingDecorator = hole.getLeadingDecorator();
				if( leadingDecorator != null ) {
					this.remove( leadingDecorator );
				}
				java.awt.Component trailingDecorator = hole.getTrailingDecorator();
				if( trailingDecorator != null ) {
					this.remove( trailingDecorator );
				}
			}
			java.awt.Component northDecorator = holeGroup.getNorthDecorator();
			if( northDecorator != null ) {
				this.remove( northDecorator );
			}
			this.revalidate();
			this.repaint();
		}
	}

	public Iterable< HoleGroup > getHoleGroups() {
		return this.holeGroups;
	}

	public void clearHoleGroups() {
		//todo synchronize
		HoleGroup[] array = new HoleGroup[ this.holeGroups.size() ];
		this.holeGroups.toArray( array );
		for( HoleGroup holeGroup : array ) {
			this.removeHoleGroup( holeGroup );
		}
	}

	
	//todo: check the hole components
	@Override
	public boolean contains( int x, int y ) {
//todo?
//		if( someone else has hooked up listeners to the glass pane or changed the cursor ) {
//			return super.contains( x, y );
//		} else {
			java.awt.geom.Area area = calculateArea( this.getBounds(), PADDING_NOT_DESIRED );
			return area.contains( x, y );
//		}
	}

	@Override
	public void doLayout() {

		//todo: super.doLayout() at beginning and end?
		
		synchronized( this.holeGroups ) {
			
			for( HoleGroup holeGroup : this.holeGroups ) {
				int minX = Short.MAX_VALUE;
				int minY = Short.MAX_VALUE;
				
				// get the clipped size of the holes
				for( Hole hole : holeGroup.getHoles() ) {
					javax.swing.JScrollPane scrollPane = findFirstScrollPaneAncestor( hole.getComponent() );
					java.awt.Component component = hole.getComponent();
					
					if (component != null) {
						component.doLayout();
						java.awt.Component proxy = hole.getProxy();
						java.awt.Point p = component.getLocation();
						p = javax.swing.SwingUtilities.convertPoint( component.getParent(), p, this );
						this.putConstraint( proxy, Horizontal.WEST, p.x, Vertical.NORTH, p.y );		
						
						Dimension preferredSize = component.getSize();
						preferredSize.width = ((javax.swing.JComponent)component).getVisibleRect().width;
						proxy.setPreferredSize(preferredSize);
						
						minX = Math.min( minX, p.x );
						minY = Math.min( minY, p.y );
					}
				}
				int maxWidth = 0;
				
				// make all the proxies be the same size as the clipped holes
				// and find the appropriate holeGroup width
				for( Hole hole : holeGroup.getHoles() ) {
					java.awt.Component proxy = hole.getProxy();
					java.awt.Dimension size = proxy.getPreferredSize();
					maxWidth = Math.max( maxWidth, size.width );
				}
				
				// set all the proxies to have the holeGroup width
				for( Hole hole : holeGroup.getHoles() ) {
					java.awt.Component proxy = hole.getProxy();
					java.awt.Dimension size = proxy.getPreferredSize();
					
					
					if (size.equals(new java.awt.Dimension(0,0)) ){
						if (hole.getLeadingDecorator() != null) hole.getLeadingDecorator().setVisible(false);
						if (hole.getTrailingDecorator() != null) hole.getTrailingDecorator().setVisible(false);
					} else {
						if (hole.getLeadingDecorator() != null) hole.getLeadingDecorator().setVisible(true);
						if (hole.getTrailingDecorator() != null) hole.getTrailingDecorator().setVisible(true);
					}
					size.width = maxWidth;
					proxy.setPreferredSize( size );
					
					
				}
				
				// clip against the viewport bounds for the component
				if (minY < this.getViewportBounds().y) {
					minY  = this.getViewportBounds().y;
					
					
				} 
				
				java.awt.Component northDecorator = holeGroup.getNorthDecorator();
				if( northDecorator != null ) {
					northDecorator.doLayout();
					java.awt.Dimension size = northDecorator.getPreferredSize();
					size.width = Math.max( size.width, maxWidth );
					northDecorator.setPreferredSize( size );
					this.putConstraint( northDecorator, Horizontal.WEST, minX, Vertical.NORTH, minY-size.height-holeGroup.getPad()/2 );
				}
			}
		}
		super.doLayout();
	}
	
	protected Rectangle getViewportBounds() {
		return this.getBounds();
	}

	private final boolean PADDING_DESIRED = true;
	private final boolean PADDING_NOT_DESIRED = false;
	protected java.awt.Rectangle calculateBoundsFor( HoleGroup holeGroup, boolean isPaddingDesired ) {
		java.awt.Rectangle rv = null;
		for( Hole hole : holeGroup.getHoles() ) {
			java.awt.Rectangle rectHole = hole.getProxy().getBounds();
			javax.swing.JScrollPane scrollPane = findFirstScrollPaneAncestor( hole.getComponent() );
			if( scrollPane != null ) {
				javax.swing.JViewport viewport = scrollPane.getViewport();
				rectHole = javax.swing.SwingUtilities.convertRectangle( hole.getProxy().getParent(), rectHole, viewport.getParent() );
				java.awt.Rectangle viewRect = new java.awt.Rectangle(viewport.getViewPosition(), viewport.getExtentSize());
				rectHole = rectHole.intersection( viewport.getVisibleRect() );
				rectHole = javax.swing.SwingUtilities.convertRectangle( viewport.getParent(), rectHole, this.container );
			} else {
				rectHole = javax.swing.SwingUtilities.convertRectangle( hole.getProxy().getParent(), rectHole, this.container );
			}
			if( rv != null ) {
				rv = rv.union( rectHole );
			} else {
				rv = new java.awt.Rectangle( rectHole );
			}
		}
		if( isPaddingDesired ) {
			int pad = holeGroup.getPad();
			rv.x -= pad;
			rv.y -= pad;
			rv.width += pad*2;
			rv.height += pad*2;
		}

		return rv;
	}
	private java.awt.geom.Area calculateArea( java.awt.Rectangle bounds, boolean isPaddingDesired ) {
		java.awt.geom.Area rv = new java.awt.geom.Area( bounds );
		synchronized( this.holeGroups ) {
			for( HoleGroup holeGroup : this.holeGroups ) {
				java.awt.Rectangle rectGroup = this.calculateBoundsFor( holeGroup, isPaddingDesired );
				if (rectGroup != null) {
					rv.subtract( new java.awt.geom.Area( rectGroup ) );
				}
			}
		}
		return rv;
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );

		java.awt.geom.Area area = calculateArea( this.getBounds(), PADDING_DESIRED );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( this.getBackground() );
		g2.fill( area );
		for( HoleGroup holeGroup : this.holeGroups ) {
			java.awt.Rectangle rectGroup = this.calculateBoundsFor( holeGroup, PADDING_DESIRED );
			g2.setColor( java.awt.Color.GRAY );
			g.draw3DRect( rectGroup.x, rectGroup.y, rectGroup.width, rectGroup.height, false );
			g2.setColor( this.getForeground() );
			final float BONUS_PAD = 10.0f;
			java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( rectGroup.x-BONUS_PAD, rectGroup.y-BONUS_PAD, rectGroup.width+2*BONUS_PAD, rectGroup.height+2*BONUS_PAD, BONUS_PAD, BONUS_PAD );
			java.awt.Stroke prevStroke = g2.getStroke();
			g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
			g2.draw( rr );
			g2.setStroke( prevStroke );
		}
	}
	
	protected java.awt.Dimension getClippedBoundsFor( javax.swing.JComponent component, javax.swing.JScrollPane scrollPane ) {		
		return component.getVisibleRect().getSize();
	}

}
