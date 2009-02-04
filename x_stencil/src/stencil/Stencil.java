package stencil;

class Vacuum extends javax.swing.JPanel {
	public Vacuum() {
		// this.setOpaque( false );
		this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR );
	}
}

class Hole {
	private Vacuum vacuum = new Vacuum();
	private java.awt.Component component;

	private java.awt.Component leadingDecorator;
	private java.awt.Component trailingDecorator;

	public Hole( java.awt.Component component ) {
		this.component = component;
	}

	public java.awt.Component getComponent() {
		return this.component;
	}
	public Vacuum getVacuum() {
		return this.vacuum;
	}

	public java.awt.Component getLeadingDecorator() {
		return this.leadingDecorator;
	}
	public void setLeadingDecorator( java.awt.Component leadingDecorator ) {
		this.leadingDecorator = leadingDecorator;
	}


	public java.awt.Component getTrailingDecorator() {
		return this.trailingDecorator;
	}
	public void setTrailingDecorator( java.awt.Component trailingDecorator ) {
		this.trailingDecorator = trailingDecorator;
	}
}

class HoleGroup {
	private Vacuum vacuum = new Vacuum();
	private Stencil stencil;
	private Hole[] holes;
	private java.awt.Component northDecorator;
	private java.awt.Component eastDecorator;
	private java.awt.Component southDecorator;
	private java.awt.Component westDecorator;

	public HoleGroup( Hole... holes ) {
		this.holes = holes;
		for( Hole hole : this.holes ) {
			Vacuum vacuum = hole.getVacuum();
			//todo
		}
	}

	public Hole[] getHoles() {
		return this.holes;
	}
	private void addDecoratorIfNecessary( java.awt.Component decorator ) {
		if( decorator != null ) {
			if( this.stencil != null ) {
				// todo
			}
		}
	}

	private void removeDecoratorIfNecessary( java.awt.Component decorator ) {
		if( decorator != null ) {
			if( this.stencil != null ) {
				// todo
			}
		}
	}

	public Vacuum getVacuum() {
		return vacuum;
	}

	public Stencil getStencil() {
		return this.stencil;
	}

	public void setStencil( Stencil stencil ) {
		this.stencil = stencil;
	}

	public java.awt.Component getNorthDecorator() {
		return this.northDecorator;
	}

	public void setNorthDecorator( java.awt.Component northDecorator ) {
		removeDecoratorIfNecessary( this.northDecorator );
		this.northDecorator = northDecorator;
		addDecoratorIfNecessary( this.northDecorator );
	}

	public java.awt.Component getEastDecorator() {
		return this.eastDecorator;
	}

	public void setEastDecorator( java.awt.Component eastDecorator ) {
		removeDecoratorIfNecessary( this.eastDecorator );
		this.eastDecorator = eastDecorator;
		addDecoratorIfNecessary( this.eastDecorator );
	}

	public java.awt.Component getSouthDecorator() {
		return this.southDecorator;
	}

	public void setSouthDecorator( java.awt.Component southDecorator ) {
		removeDecoratorIfNecessary( this.southDecorator );
		this.southDecorator = southDecorator;
		addDecoratorIfNecessary( this.southDecorator );
	}

	public java.awt.Component getWestDecorator() {
		return this.westDecorator;
	}

	public void setWestDecorator( java.awt.Component westDecorator ) {
		removeDecoratorIfNecessary( this.westDecorator );
		this.westDecorator = westDecorator;
		addDecoratorIfNecessary( this.westDecorator );
	}

}

// protected java.awt.Rectangle calculateVacuumBounds() {
// java.awt.Rectangle rv = null;
// for (javax.swing.JComponent sibling : this.siblings) {
// java.awt.Rectangle siblingBounds = sibling.getBounds();
// if (rv != null) {
// rv.union(siblingBounds);
// } else {
// rv = siblingBounds;
// }
// }
// return rv;
// }

public class Stencil extends edu.cmu.cs.dennisc.swing.CornerSpringPane {
	private java.util.List< Hole > holes = new java.util.LinkedList< Hole >();
	private java.util.List< HoleGroup > holeGroups = new java.util.LinkedList< HoleGroup >();
	private java.awt.Container container;

	public Stencil( java.awt.Container container ) {
		this.container = container;
		setOpaque( false );
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
				final java.awt.Component vacuum = hole.getVacuum();
				java.awt.Point p = component.getLocation();
				p.x = 200;
				p.y = 100;
				vacuum.setLocation( p );
				vacuum.setSize( 100, 100 );
				component.addComponentListener( new java.awt.event.ComponentListener() {
					private void handleChange( java.awt.event.ComponentEvent e ) {
						Stencil.this.revalidate();
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
				this.add( vacuum );
				this.getSpringLayout().putConstraint( javax.swing.SpringLayout.WEST, vacuum, 100, javax.swing.SpringLayout.WEST, this );
				this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, vacuum, 100, javax.swing.SpringLayout.NORTH, this );
				
				final int PAD = 8;
				java.awt.Component leadingDecorator = hole.getLeadingDecorator();
				if( leadingDecorator != null ) {
					this.add( leadingDecorator );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( leadingDecorator );
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.EAST, leadingDecorator, -PAD, javax.swing.SpringLayout.WEST, vacuum );
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, leadingDecorator, 0, javax.swing.SpringLayout.NORTH, vacuum );
				}
				java.awt.Component trailingDecorator = hole.getTrailingDecorator();
				if( trailingDecorator != null ) {
					this.add( trailingDecorator );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( trailingDecorator );
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.WEST, trailingDecorator, PAD, javax.swing.SpringLayout.EAST, vacuum );
					this.getSpringLayout().putConstraint( javax.swing.SpringLayout.NORTH, trailingDecorator, 0, javax.swing.SpringLayout.NORTH, vacuum );
				}
			}
		}
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

//	private java.awt.geom.Area calculateArea( java.awt.Rectangle bounds ) {
//		java.awt.geom.Area rv = new java.awt.geom.Area( bounds );
//		synchronized( this.holes ) {
//			for( Hole hole : this.holes ) {
//				rv.subtract( hole.getArea( this.container ) );
//			}
//		}
//		return rv;
//	}

	// @Override
	// protected void paintComponent( java.awt.Graphics g ) {
	// super.paintComponent( g );
	//		
	// java.awt.geom.Area area = calculateArea( g.getClipBounds() );
	// java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
	// g2.setPaint( new java.awt.Color( 127, 255, 127, 127 ) );
	// g2.fill( area );
	// }

	public static void main( String[] args ) {
		class Pane extends javax.swing.JPanel {
			private javax.swing.JButton button = new javax.swing.JButton( "button" );

			public Pane() {
				button.addActionListener( new java.awt.event.ActionListener() {
					public void actionPerformed( java.awt.event.ActionEvent e ) {
						Pane.this.button.setSize( Pane.this.button.getHeight(), Pane.this.button.getWidth() );
					}
				} );
				button.setSize( 240, 80 );
				button.setLocation( 300, 200 );
				this.setLayout( null );
				this.add( button );
				this.setSize( 640, 480 );
			}

			public javax.swing.JComponent getComponentFor( java.util.UUID uuid ) {
				return this.button;
			}
		}

		javax.swing.JFrame frame = new javax.swing.JFrame();

		Pane pane = new Pane();

		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final Stencil stencil = new Stencil( layeredPane );

		Hole hole = new Hole( pane.getComponentFor( java.util.UUID.randomUUID() ) );
		hole.setLeadingDecorator( new javax.swing.JButton( "1" ) );
		hole.setTrailingDecorator( new javax.swing.JButton( "west" ) );
		
		HoleGroup holeGroup = new HoleGroup( hole );
		stencil.addHoleGroup( holeGroup );

		stencil.setNorthEastComponent( new javax.swing.JButton( "exit" ) );

		layeredPane.addComponentListener( new java.awt.event.ComponentAdapter() {
			public void componentResized( java.awt.event.ComponentEvent e ) {
				stencil.setSize( e.getComponent().getSize() );
				stencil.revalidate();
			}
		} );
		layeredPane.add( stencil, javax.swing.JLayeredPane.DRAG_LAYER );

		frame.getContentPane().add( pane );

		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
