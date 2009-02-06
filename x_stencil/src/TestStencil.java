import stencil.*;

public class TestStencil {
	public static void main( String[] args ) {
		class Pane extends edu.cmu.cs.dennisc.swing.SpringPane {
			private javax.swing.JButton[] buttons = { 
					new javax.swing.JButton( "four score" ), 
					new javax.swing.JButton( "and seven years ago" ), 
					new javax.swing.JButton( "our forefathers brought forth" ), 
					new javax.swing.JButton( "upon this continent," ),
					new javax.swing.JButton( "a new nation," ),
					new javax.swing.JButton( "conceived in Liberty," ), 
					new javax.swing.JButton( "and dedicated to the proposition that" ), 
					new javax.swing.JButton( "that all men are created equal." ) 
			};

			public Pane() {
				int x = 0;
				int y = 0;
				for( final javax.swing.JButton button : this.buttons ) {
					this.add( button, Horizontal.WEST, x, Vertical.NORTH, y );
					button.addActionListener( new java.awt.event.ActionListener() {
						public void actionPerformed( java.awt.event.ActionEvent e ) {
							java.awt.Dimension size = button.getPreferredSize();
							size.width += 10;
							size.height += 10;
							button.setPreferredSize( size );
							Pane.this.revalidate();
						}
					} );
					y += 32;
				}
			}
		}

		javax.swing.JFrame frame = new javax.swing.JFrame();

		Pane pane = new Pane();
		pane.setPreferredSize( new java.awt.Dimension( 1000, 3000 ) );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( pane );
		frame.getContentPane().add( new javax.swing.JButton( "top" ), java.awt.BorderLayout.NORTH );
		frame.getContentPane().add( new javax.swing.JButton( "left" ), java.awt.BorderLayout.WEST );
		frame.getContentPane().add( scrollPane, java.awt.BorderLayout.CENTER );

		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final Stencil stencil = new Stencil( layeredPane );

		final int MIN = 2;
		final int MAX = 5;
		int i = 0;
		java.util.List< Hole > holes = new java.util.LinkedList< Hole >();
		for( javax.swing.JButton button : pane.buttons ) {
			if( MIN <= i && i <= MAX ) {
				Hole hole = new Hole( button );
				hole.setLeadingDecorator( new javax.swing.JButton( Integer.toString( i - MIN + 1 ) ) );
				hole.setTrailingDecorator( new javax.swing.JButton( "inspect" ) );
				holes.add( hole );
			}
			i += 1;
		}
		
		class Header extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
			public Header() {
				this.add( new javax.swing.JButton( "a" ) );
				this.add( new javax.swing.JLabel( "/" ) );
				this.add( new javax.swing.JButton( "b" ) );
				this.add( new javax.swing.JLabel( "/" ) );
				this.add( new javax.swing.JButton( "c" ) );
				this.add( new javax.swing.JLabel( "/" ) );
				this.add( new javax.swing.JButton( "d" ) );
				this.setBackground( java.awt.Color.BLUE );
				this.setOpaque( true );
			}
		}

		HoleGroup holeGroup = new HoleGroup( holes );
		holeGroup.setNorthDecorator( new Header() );
		stencil.addHoleGroup( holeGroup );

		stencil.setNorthEastComponent( new javax.swing.JButton( "exit" ) );

		java.awt.Component specialPane = new javax.swing.JPanel() {
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "special", this.getSize() );
			}
		};
		specialPane.setBackground( java.awt.Color.RED );
		specialPane.setPreferredSize( new java.awt.Dimension( 160, 240 ) );
		stencil.add( specialPane, edu.cmu.cs.dennisc.swing.SpringPane.Horizontal.WEST, 450, edu.cmu.cs.dennisc.swing.SpringPane.Vertical.NORTH, 50 );

//		layeredPane.addComponentListener( new java.awt.event.ComponentAdapter() {
//			public void componentResized( java.awt.event.ComponentEvent e ) {
//				stencil.setSize( e.getComponent().getSize() );
//				stencil.revalidate();
//			}
//		} );
		layeredPane.add( stencil, javax.swing.JLayeredPane.DRAG_LAYER );


		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );

	}
}
