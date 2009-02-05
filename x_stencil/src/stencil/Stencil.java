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

	private java.awt.Rectangle calculateBoundsFor( HoleGroup holeGroup ) {
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
		int pad = holeGroup.getPad();
		rv.x -= pad;
		rv.y -= pad;
		rv.width += pad*2;
		rv.height += pad*2;
		return rv;
	}
	private java.awt.geom.Area calculateArea( java.awt.Rectangle bounds ) {
		java.awt.geom.Area rv = new java.awt.geom.Area( bounds );
		synchronized( this.holeGroups ) {
			for( HoleGroup holeGroup : this.holeGroups ) {
				java.awt.Rectangle rectGroup = this.calculateBoundsFor( holeGroup );
				rv.subtract( new java.awt.geom.Area( rectGroup ) );
			}
		}
		return rv;
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );

		java.awt.geom.Area area = calculateArea( this.getBounds() );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( new java.awt.Color( 127, 255, 127, 127 ) );
		g2.fill( area );
		g2.setColor( java.awt.Color.GRAY );
		for( HoleGroup holeGroup : this.holeGroups ) {
			java.awt.Rectangle rectGroup = this.calculateBoundsFor( holeGroup );
			g.draw3DRect( rectGroup.x, rectGroup.y, rectGroup.width, rectGroup.height, false );
		}
	}

}
