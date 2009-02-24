import javax.swing.Box;

import stencil.*;

public class TestStencil {
	public static void main( String[] args ) {
		class Pane extends edu.cmu.cs.dennisc.swing.SpringPane {
			private javax.swing.JButton[] buttons = { 
					new javax.swing.JButton( "a" ), 
					new javax.swing.JButton( "b" ), 
					new javax.swing.JButton( "c" ), 
					new javax.swing.JButton( "d" ), 
					new javax.swing.JButton( "d" ), 
					new javax.swing.JButton( "e" ), 
					new javax.swing.JButton( "f" ), 
					new javax.swing.JButton( "g" ), 
					new javax.swing.JButton( "h" ), 
					new javax.swing.JButton( "i" ), 
					new javax.swing.JButton( "j" ), 
					new javax.swing.JButton( "k" ), 
					new javax.swing.JButton( "l" ), 
					new javax.swing.JButton( "m" ), 
					new javax.swing.JButton( "n" ), 
					new javax.swing.JButton( "o" ), 
					new javax.swing.JButton( "p" ), 
					new javax.swing.JButton( "q" ), 
					new javax.swing.JButton( "r" ), 
					new javax.swing.JButton( "s" ), 
					new javax.swing.JButton( "t" ), 
					new javax.swing.JButton( "u" ), 
					new javax.swing.JButton( "v" ), 
					new javax.swing.JButton( "w" ), 
					new javax.swing.JButton( "x" ), 
					new javax.swing.JButton( "y" ), 
					new javax.swing.JButton( "z" ), 
					new javax.swing.JButton( "four score" ), 
					new javax.swing.JButton( "and seven years ago" ), 
					new javax.swing.JButton( "our forefathers brought forth" ), 
					new javax.swing.JButton( "upon this continent," ),
					new javax.swing.JButton( "a new nation," ),
					new javax.swing.JButton( "conceived in Liberty," ), 
					new javax.swing.JButton( "and dedicated to the proposition that" ), 
					new javax.swing.JButton( "that all men are created equal." ), 
					new javax.swing.JButton( "0" ), 
					new javax.swing.JButton( "1" ), 
					new javax.swing.JButton( "2" ), 
					new javax.swing.JButton( "3" ), 
					new javax.swing.JButton( "4" ), 
					new javax.swing.JButton( "5" ), 
					new javax.swing.JButton( "6" ), 
					new javax.swing.JButton( "7" ), 
					new javax.swing.JButton( "8" ), 
					new javax.swing.JButton( "9" ) 
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
			
			public javax.swing.JButton getButtonWithText( String text ) { 
				for( javax.swing.JButton button : this.buttons ) {
					if( button.getText().equals( text ) ) {
						return button;
					}
				}
				return null;
			}
		}

		javax.swing.JFrame frame = new javax.swing.JFrame();

		final Pane pane = new Pane();
		pane.setPreferredSize( new java.awt.Dimension( 1000, 3000 ) );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( pane );


		final javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final Stencil stencil = new Stencil( layeredPane );

		javax.swing.JButton topButton = new javax.swing.JButton( "enable stencil" );
		topButton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				layeredPane.add( stencil, javax.swing.JLayeredPane.DRAG_LAYER );
			}
		} );

		javax.swing.JButton leftButton = new javax.swing.JButton( "scroll to \"0\"" );
		leftButton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				javax.swing.JComponent component = pane.getButtonWithText( "0" );
				component.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( component ) );
			}
		} );
		frame.getContentPane().add( topButton, java.awt.BorderLayout.NORTH );
		frame.getContentPane().add( leftButton, java.awt.BorderLayout.WEST );
		frame.getContentPane().add( scrollPane, java.awt.BorderLayout.CENTER );

		final int MIN = 27;
		final int MAX = 31;
		int i = 0;
		java.util.List< Hole > holes = new java.util.LinkedList< Hole >();
		for( final javax.swing.JButton button : pane.buttons ) {
			if( MIN <= i && i <= MAX ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						if( button.isShowing() ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: scrollToVisible is not going to work for (not yet showing):", button );
						}
						button.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( button ) );
					}
				} );
				Hole hole = new Hole( button );
				hole.setLeadingDecorator( new javax.swing.JButton( Integer.toString( i - MIN + 1 ) ) );
				hole.setTrailingDecorator( new javax.swing.JButton( "inspect" ) );
				holes.add( hole );
			}
			i += 1;
		}
		
		class Header extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
			public Header() {
				this.add( new javax.swing.JLabel( "a" ) );
				this.add( new javax.swing.JLabel( "/" ) );
				this.add( new javax.swing.JLabel( "b" ) );
				this.add( new javax.swing.JLabel( "/" ) );
				this.add( new javax.swing.JLabel( "c" ) );
				this.add( Box.createHorizontalGlue() );
				this.setBackground( java.awt.Color.BLUE );
				this.setOpaque( true );
			}
		}

		HoleGroup holeGroup = new HoleGroup( holes );
		holeGroup.setNorthDecorator( new Header() );
		stencil.addHoleGroup( holeGroup );

		stencil.setNorthEastComponent( new javax.swing.JButton( "exit" ) );

		java.awt.Component specialPane = new javax.swing.JPanel() {
			@Override
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


		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );

	}
}
