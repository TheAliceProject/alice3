package stencil;

public class Stencil extends edu.cmu.cs.dennisc.swing.CornerSpringPane {
	// private java.util.List< Hole > holes = new java.util.LinkedList< Hole >();
	private java.util.List< HoleGroup > holeGroups = new java.util.LinkedList< HoleGroup >();
	private java.awt.Container container;

	public Stencil( java.awt.Container container ) {
		this.container = container;
		this.container.addComponentListener( new java.awt.event.ComponentAdapter() {
			public void componentResized( java.awt.event.ComponentEvent e ) {
				Stencil.this.synchronizeSize();
			}
		} );
		this.synchronizeSize();
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

	private void redispatch( java.awt.event.MouseEvent e ) {
		java.awt.Point p = e.getPoint();
		if( this.contains( p.x, p.y ) ) {
			//pass
		} else {
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt( this.container, p.x, p.y );
			if( component != null ) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint( this, p, component );
				component.dispatchEvent( new java.awt.event.MouseEvent( component, e.getID(), e.getWhen(), e.getModifiers(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger() ) );
			}
		}
	}
	
	public void synchronizeSize() {
		this.setSize( this.container.getSize() );
		this.revalidate();
	}

	// public void addHole(Hole hole) {
	// synchronized (this.holes) {
	// this.holes.add(hole);
	// }
	// }
	// public void removeHole(Hole hole) {
	// synchronized (this.holes) {
	// this.holes.remove(hole);
	// }
	// }
	// public Iterable<Hole> getHoles() {
	// synchronized (this.holes) {
	// return this.holes;
	// }
	// }
	// public void clearHoles() {
	// synchronized (this.holes) {
	// this.holes.clear();
	// }
	// }

	public void addHoleGroup( HoleGroup holeGroup ) {
		synchronized( this.holeGroups ) {
			this.holeGroups.add( holeGroup );

			for( Hole hole : holeGroup.getHoles() ) {
				final java.awt.Component component = hole.getComponent();
				final java.awt.Component proxy = hole.getProxy();
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
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.WEST, trailingDecorator, pad, javax.swing.SpringLayout.EAST, proxy );
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, trailingDecorator, 0, javax.swing.SpringLayout.NORTH, proxy );
				}
			}
			java.awt.Component northDecorator = holeGroup.getNorthDecorator();
			if( northDecorator != null ) {
				this.add( northDecorator );
			}
		}
	}

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
		synchronized( this.holeGroups ) {
			for( HoleGroup holeGroup : this.holeGroups ) {
				int minX = Short.MAX_VALUE;
				int minY = Short.MAX_VALUE;
				for( Hole hole : holeGroup.getHoles() ) {
					java.awt.Component component = hole.getComponent();
					java.awt.Component proxy = hole.getProxy();
					java.awt.Point p = component.getLocation();
					p = javax.swing.SwingUtilities.convertPoint( proxy.getParent(), p, this );
					this.putConstraint( proxy, Horizontal.WEST, p.x, Vertical.NORTH, p.y );
					
					proxy.setPreferredSize( component.getSize() );
					minX = Math.min( minX, p.x );
					minY = Math.min( minY, p.y );
				}
				int maxWidth = 0;
				for( Hole hole : holeGroup.getHoles() ) {
					java.awt.Component proxy = hole.getProxy();
					java.awt.Dimension size = proxy.getPreferredSize();
					maxWidth = Math.max( maxWidth, size.width );
				}
				for( Hole hole : holeGroup.getHoles() ) {
					java.awt.Component proxy = hole.getProxy();
					java.awt.Dimension size = proxy.getPreferredSize();
					size.width = maxWidth;
					proxy.setPreferredSize( size );
				}
				
				java.awt.Component northDecorator = holeGroup.getNorthDecorator();
				if( northDecorator != null ) {
					int height = 32; 
					northDecorator.setPreferredSize( new java.awt.Dimension( maxWidth, height ) );
					this.putConstraint( northDecorator, Horizontal.WEST, minX, Vertical.NORTH, minY-height-holeGroup.getPad()/2 );
					
				}
			}
		}
		super.doLayout();
	}

	public void removeHoleGroup( HoleGroup holeGroup ) {
		synchronized( this.holeGroups ) {
			this.holeGroups.remove( holeGroup );
		}
	}

	public Iterable< HoleGroup > getHoleGroups() {
		return this.holeGroups;
	}

	public void clearHoleGroups() {
		synchronized( this.holeGroups ) {
			this.holeGroups.clear();
		}
	}

	private final boolean PADDING_DESIRED = true;
	private final boolean PADDING_NOT_DESIRED = false;
	private java.awt.Rectangle calculateBoundsFor( HoleGroup holeGroup, boolean isPaddingDesired ) {
		java.awt.Rectangle rv = null;
		for( Hole hole : holeGroup.getHoles() ) {
			java.awt.Rectangle rectHole = hole.getProxy().getBounds();
			rectHole = javax.swing.SwingUtilities.convertRectangle( hole.getProxy().getParent(), rectHole, this.container );
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
				rv.subtract( new java.awt.geom.Area( rectGroup ) );
			}
		}
		return rv;
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );

		java.awt.geom.Area area = calculateArea( this.getBounds(), PADDING_DESIRED );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( new java.awt.Color( 127, 255, 127, 127 ) );
		g2.fill( area );
		g2.setColor( java.awt.Color.GRAY );
		for( HoleGroup holeGroup : this.holeGroups ) {
			java.awt.Rectangle rectGroup = this.calculateBoundsFor( holeGroup, PADDING_DESIRED );
			g.draw3DRect( rectGroup.x, rectGroup.y, rectGroup.width, rectGroup.height, false );
		}
	}

}
