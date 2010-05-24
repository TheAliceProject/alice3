/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class NoteStep extends Step {
	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
	private class Note extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
		private javax.swing.JTextPane textComponent = new javax.swing.JTextPane() {
			@Override
			public boolean contains(int x, int y) {
				return false;
			}
		};

		@Override
		protected javax.swing.JComponent createAwtComponent() {
			this.textComponent.setContentType( "text/html" );
			this.textComponent.setOpaque( false );
			this.textComponent.setEditable( false );
			//this.textPane.setEnabled( false );

			javax.swing.JPanel rv = new javax.swing.JPanel() {
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					
					int w = this.getWidth();
					int h = this.getHeight();
					

					g2.setPaint( java.awt.Color.GRAY );
					java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
					pathShadow.moveTo( w-4, 0 );
					pathShadow.lineTo( w, h );
					pathShadow.lineTo( 0, h-4 );
					pathShadow.lineTo( w-4, h-4 );
					pathShadow.closePath();
					g2.fill( pathShadow );

					
					int x1 = w-20;
					int y1 = h-20;
					java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, HIGHLIGHT_COLOR, x1-200, y1-200, BASE_COLOR );
					g2.setPaint( paint );
					
					g2.fillRect( 0, 0, w-4, h-4 );
					
					super.paintComponent( g );
				}
//				@Override
//				public void paint(java.awt.Graphics g) {
//					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//					g2.rotate( Math.PI / 90 );
//					super.paint(g);
//				}
				@Override
				public java.awt.Dimension getPreferredSize() {
					java.awt.Dimension rv = super.getPreferredSize();
					rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumHeight( rv, 256 );
					rv.width = 256;
					return rv;
				}
			};
			rv.setLayout( new java.awt.BorderLayout() );
			rv.add( this.textComponent, java.awt.BorderLayout.NORTH );
			edu.cmu.cs.dennisc.croquet.BorderPanel southPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
			southPanel.addComponent( getTutorial().getNextOperation().createHyperlink(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
			rv.add( southPanel.getAwtComponent(), java.awt.BorderLayout.SOUTH );
			rv.setOpaque( false );
			final int X_PAD = 16;
			final int Y_PAD = 12;
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD,X_PAD,Y_PAD,X_PAD ) );
			return rv;
		}
		public void setText( String text ) {
			this.textComponent.setText( text );
		}
		private javax.swing.event.MouseInputListener mouseInputListener = new javax.swing.event.MouseInputListener() {

			private java.awt.event.MouseEvent ePressed;
			private java.awt.Point ptPressed;
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					getTutorial().getNextOperation().fire(e);
				}
			}

			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}

			public void mouseExited( java.awt.event.MouseEvent e ) {
			}

			public void mousePressed( java.awt.event.MouseEvent e ) {
				this.ePressed = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
				this.ptPressed = Note.this.getAwtComponent().getLocation();
			}

			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}

			public void mouseDragged( java.awt.event.MouseEvent e ) {
				java.awt.event.MouseEvent eDragged = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
				int xDelta = eDragged.getX() - this.ePressed.getX();
				int yDelta = eDragged.getY() - this.ePressed.getY();
				int x = ptPressed.x + xDelta;
				int y = ptPressed.y + yDelta;
				Note.this.getAwtComponent().setLocation( x, y );
				NoteStep.this.notePanel.getAwtComponent().repaint();
			}

			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}
			
		};
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			super.handleAddedTo( parent );
			this.addMouseListener( this.mouseInputListener );
			this.addMouseMotionListener( this.mouseInputListener );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.removeMouseMotionListener( this.mouseInputListener );
			this.removeMouseListener( this.mouseInputListener );
			super.handleRemovedFrom( parent );
		}
	}
	

	private class StepLayoutManager implements java.awt.LayoutManager {
		private java.util.Set<java.awt.Component> set = edu.cmu.cs.dennisc.java.util.Collections.newHashSet(); 
		public void addLayoutComponent( java.lang.String name, java.awt.Component comp ) {
		}
		public void removeLayoutComponent( java.awt.Component comp ) {
		}
		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return parent.getMinimumSize();
		}
		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			return parent.getPreferredSize();
		}
		public void layoutContainer( java.awt.Container parent ) {
			for( java.awt.Component awtComponent : parent.getComponents() ) {
				awtComponent.setSize( awtComponent.getPreferredSize() );
				if( set.contains( awtComponent ) ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.croquet.Component<?> component = edu.cmu.cs.dennisc.croquet.Component.lookup( awtComponent );
					if (component instanceof Note) {
						Note note = (Note) component;
						NoteStep.this.layoutNote( note );
					} else {
						awtComponent.setLocation( 10, 10 );
					}
					set.add( awtComponent );
				}
			}
		}
	}
	private class StepPanel extends edu.cmu.cs.dennisc.croquet.Panel {
		private Note note = new Note();
		public StepPanel() {
			this.internalAddComponent( this.note );
		}
		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new StepLayoutManager();
		}
	}
	
	private StepPanel notePanel = new StepPanel();
	private String title;

	public static NoteStep createMessageNoteStep( Tutorial tutorial, String title, String text ) {
		return new NoteStep( tutorial, title, text, null );
	}
	public static NoteStep createSpotlightMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		return new NoteStep( tutorial, title, text, new Frame(component, Feature.ConnectionPreference.EAST_WEST ) );
	}
	public static NoteStep createActionMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.EAST_WEST) );
	}
	private NoteStep( Tutorial tutorial, String title, String text, Feature feature ) {
		super( tutorial );
		this.title = title;
		this.notePanel.note.setText( text );
		if( feature != null ) {
			this.addFeature( feature );
		}
	}
	@Override
	public String toString() {
		return this.title;
	}
	
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > getNote() {
		//todo
		return ((edu.cmu.cs.dennisc.croquet.Container<?>)this.notePanel.getComponent( 0 )).getComponent( 0 );
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > getCard() {
		return this.notePanel;
	}
	
//	@Override
//	public java.awt.geom.Area subtractIfAppropriate( java.awt.geom.Area rv, edu.cmu.cs.dennisc.croquet.Component<?> panel, boolean isStrict ) {
//		if( this.component != null ) {
//			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
//			if( this.isHole ) {
//				if( isStrict ) {
//					//pass
//				} else {
//					edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
//				}
//			} else {
//				if( isStrict ) {
//					componentBounds = null;
//				}
//			}
//			if( componentBounds != null ) {
//				rv.subtract( new java.awt.geom.Area( componentBounds ) );
//			}
//		}
//		return rv;
//	}
//	
//	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo ) {
//		float xVector = xTo-xFrom; 
//		float yVector = yTo-yFrom;
//		
//		final float A = 0.15f;
//		final float B = 1.0f;
//		
//		float xA = xFrom + xVector*A;
//		float yA = yFrom + yVector*A;
//
//		float xB = xFrom + xVector*B;
//		float yB = yFrom + yVector*B;
//		
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		path.moveTo( xFrom, yFrom );
//		path.curveTo( xB, yA, xA, yB, xTo, yTo );
//
//		g2.draw( path );
//		//g2.drawLine( (int)xB, (int)yA, (int)xA, (int)yB );
//	}
//
//	private final static java.awt.Paint HIGHLIGHT_PAINT = new java.awt.Color( 255, 255, 0, 23 );
//	private final static java.awt.Stroke[] HIGHLIGHT_STROKES = {
//		new java.awt.BasicStroke(  5.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 10.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 15.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 20.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 25.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 30.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 35.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 40.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND )
//	};
//	
//	private static void fill( java.awt.Graphics2D g2, java.awt.Shape shape, float x, float y, double theta ) {
//		java.awt.geom.AffineTransform m = g2.getTransform();
//		try {
//			g2.translate( x, y );
//			g2.rotate( theta );
//			g2.fill( shape );
//		} finally {
//			g2.setTransform( m );
//		}
//	}
//	protected boolean paintComponent( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Panel panel, edu.cmu.cs.dennisc.croquet.Component<?> note ) {
//		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//		if( this.component != null ) {
//			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
//			if( this.isHole ) {
//				edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
//			}
//			
//			java.awt.Stroke prevStroke = g2.getStroke();
//			
//			if( this.isHole ) {
//				java.awt.Shape prevClip = g2.getClip();
//				
//				java.awt.geom.Area area = new java.awt.geom.Area( prevClip );
//				area.subtract( new java.awt.geom.Area( componentBounds ) );
//				
//				g2.setClip( area );
//
//				g2.setPaint( HIGHLIGHT_PAINT );
//				for( java.awt.Stroke stroke : HIGHLIGHT_STROKES ) {
//					g2.setStroke( stroke );
//					g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//				}
//
//				g2.setClip( prevClip );
//			}
//	
//			
//			if( this.isHole ) {
//				g2.setStroke( HOLE_BEVEL_STROKE );
//				
////				g2.setPaint( java.awt.Color.GRAY );
////				g2.draw3DRect(componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height, false);
//
//				int x0 = componentBounds.x;
//				int y0 = componentBounds.y;
//				int x1 = componentBounds.x + componentBounds.width;
//				int y1 = componentBounds.y + componentBounds.height;
//				g2.setPaint( java.awt.Color.LIGHT_GRAY );
//				g2.drawLine( x1, y0, x1, y1 );
//				g2.drawLine( x1, y1, x0, y1 );
//				g2.setPaint( java.awt.Color.DARK_GRAY );
//				g2.drawLine( x0, y1, x0, y0 );
//				g2.drawLine( x0, y0, x1, y0 );
//			} else {
//				//g2.draw3DRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height, true );
//				g2.setPaint( getCallOutPaint() );
//				g2.fillRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//
//				g2.setStroke( FRAME_STROKE );
//				g2.setPaint( java.awt.Color.RED );
//				g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//				
//				g2.setPaint( java.awt.Color.BLACK );
//
//				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//				path.moveTo( -3, -3 );
//				path.lineTo( -3, 9 );
//				path.lineTo( 3, 6 );
//				path.lineTo( 3, 3 );
//				path.lineTo( 6, 3 );
//				path.lineTo( 9, -3 );
//				path.closePath();
//
//				//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
//				fill( g2, path, componentBounds.x, componentBounds.y, 0 );
//				fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y, Math.PI/2 );
//				fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y + componentBounds.height, Math.PI );
//				fill( g2, path, componentBounds.x, componentBounds.y + componentBounds.height, 3*Math.PI/2 );
//				//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//			}
//			g2.setPaint( java.awt.Color.BLACK );
//
//					
//			g2.setStroke( ARROW_STROKE );
//			//g2.draw( createNoisyRectangle( componentBounds, HOLE_PAD ) );
//
//			java.awt.Rectangle noteBounds = note.getBounds( panel );
//			
//			java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( componentBounds, javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER );
//			java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER );
//			
//			final int ARROW_WIDTH = 19;
//			final int ARROW_HALF_HEIGHT = 6;
//			
//			drawPath( g2, ptNote.x, ptNote.y, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1);
//			g2.setStroke( prevStroke );
//
//			int x = ptComponent.x;
//			int y = ptComponent.y - ARROW_HALF_HEIGHT;
//			g2.translate( x, y );
//			edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.WEST, new java.awt.Dimension( ARROW_WIDTH, ARROW_HALF_HEIGHT*2+1 ) );
//			g2.translate( -x, -y );
//		}
//		return true;
//	}
}

///*package-private*/ class NoteStep extends Step {
//	private static java.awt.Paint callOutPaint = null;
//	private static java.awt.Paint getCallOutPaint() {
//		if( NoteStep.callOutPaint != null ) {
//			//pass
//		} else {
//			int width = 1;
//			int height = 4;
//			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
//			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
//			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//			java.awt.Color c = Stencil.STENCIL_BASE_COLOR;
//			g2.setColor( new java.awt.Color( c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()/4 ) );
//			g2.fillRect( 0, 0, width, height );
//			g2.setColor( Stencil.STENCIL_LINE_COLOR );
//			int y = 0;
//			g2.drawLine( 0, y, width, y );
//			g2.dispose();
//			NoteStep.callOutPaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
//		}
//		return NoteStep.callOutPaint;
//	}
//
//	private static final java.awt.Stroke ARROW_STROKE = new java.awt.BasicStroke( 3.0f ); 
//	private static final java.awt.Stroke HOLE_BEVEL_STROKE = new java.awt.BasicStroke( 2.0f ); 
//	private static final java.awt.Stroke FRAME_STROKE = new java.awt.BasicStroke( 3.0f ); 
//
//	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
//	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
//	private class Note extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
//		private javax.swing.JTextPane textComponent = new javax.swing.JTextPane() {
//			@Override
//			public boolean contains(int x, int y) {
//				return false;
//			}
//		};
//
//		@Override
//		protected javax.swing.JComponent createAwtComponent() {
//			this.textComponent.setContentType( "text/html" );
//			this.textComponent.setOpaque( false );
//			this.textComponent.setEditable( false );
//			//this.textPane.setEnabled( false );
//
//			javax.swing.JPanel rv = new javax.swing.JPanel() {
//				@Override
//				protected void paintComponent( java.awt.Graphics g ) {
//					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//					
//					int w = this.getWidth();
//					int h = this.getHeight();
//					
//
//					g2.setPaint( java.awt.Color.GRAY );
//					java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
//					pathShadow.moveTo( w-4, 0 );
//					pathShadow.lineTo( w, h );
//					pathShadow.lineTo( 0, h-4 );
//					pathShadow.lineTo( w-4, h-4 );
//					pathShadow.closePath();
//					g2.fill( pathShadow );
//
//					
//					int x1 = w-20;
//					int y1 = h-20;
//					java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, HIGHLIGHT_COLOR, x1-200, y1-200, BASE_COLOR );
//					g2.setPaint( paint );
//					
//					g2.fillRect( 0, 0, w-4, h-4 );
//					
//					super.paintComponent( g );
//				}
////				@Override
////				public void paint(java.awt.Graphics g) {
////					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
////					g2.rotate( Math.PI / 90 );
////					super.paint(g);
////				}
//				@Override
//				public java.awt.Dimension getPreferredSize() {
//					java.awt.Dimension rv = super.getPreferredSize();
//					rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumHeight( rv, 256 );
//					rv.width = 256;
//					return rv;
//				}
//			};
//			rv.setLayout( new java.awt.BorderLayout() );
//			rv.add( this.textComponent, java.awt.BorderLayout.NORTH );
//			edu.cmu.cs.dennisc.croquet.BorderPanel southPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
//			southPanel.addComponent( getTutorial().getNextOperation().createHyperlink(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
//			rv.add( southPanel.getAwtComponent(), java.awt.BorderLayout.SOUTH );
//			rv.setOpaque( false );
//			final int X_PAD = 16;
//			final int Y_PAD = 12;
//			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD,X_PAD,Y_PAD,X_PAD ) );
//			return rv;
//		}
//		public void setText( String text ) {
//			this.textComponent.setText( text );
//		}
//		private javax.swing.event.MouseInputListener mouseInputListener = new javax.swing.event.MouseInputListener() {
//
//			private java.awt.event.MouseEvent ePressed;
//			private java.awt.Point ptPressed;
//			public void mouseClicked( java.awt.event.MouseEvent e ) {
//				if( e.getClickCount() == 2 ) {
//					getTutorial().getNextOperation().fire(e);
//				}
//			}
//
//			public void mouseEntered( java.awt.event.MouseEvent e ) {
//			}
//
//			public void mouseExited( java.awt.event.MouseEvent e ) {
//			}
//
//			public void mousePressed( java.awt.event.MouseEvent e ) {
//				this.ePressed = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
//				this.ptPressed = Note.this.getAwtComponent().getLocation();
//			}
//
//			public void mouseReleased( java.awt.event.MouseEvent e ) {
//			}
//
//			public void mouseDragged( java.awt.event.MouseEvent e ) {
//				java.awt.event.MouseEvent eDragged = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
//				int xDelta = eDragged.getX() - this.ePressed.getX();
//				int yDelta = eDragged.getY() - this.ePressed.getY();
//				int x = ptPressed.x + xDelta;
//				int y = ptPressed.y + yDelta;
//				Note.this.getAwtComponent().setLocation( x, y );
//				NoteStep.this.notePanel.getAwtComponent().repaint();
//			}
//
//			public void mouseMoved( java.awt.event.MouseEvent e ) {
//			}
//			
//		};
//		@Override
//		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
//			super.handleAddedTo( parent );
//			this.addMouseListener( this.mouseInputListener );
//			this.addMouseMotionListener( this.mouseInputListener );
//		}
//		@Override
//		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
//			this.removeMouseMotionListener( this.mouseInputListener );
//			this.removeMouseListener( this.mouseInputListener );
//			super.handleRemovedFrom( parent );
//		}
//	}
//	
//
//	private class StepLayoutManager implements java.awt.LayoutManager {
//		public void addLayoutComponent( java.lang.String name, java.awt.Component comp ) {
//		}
//		public void removeLayoutComponent( java.awt.Component comp ) {
//		}
//		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
//			return parent.getMinimumSize();
//		}
//		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
//			return parent.getPreferredSize();
//		}
//		public void layoutContainer( java.awt.Container parent ) {
//			int x = 500;
//			int y = 360;
//			for( java.awt.Component component : parent.getComponents() ) {
//				component.setSize( component.getPreferredSize() );
//				component.setLocation( x, y );
//			}
//		}
//	}
//	private class StepPanel extends edu.cmu.cs.dennisc.croquet.Panel {
//		private Note note = new Note();
//		public StepPanel() {
//			this.internalAddComponent( this.note );
//		}
//		@Override
//		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
//			return new StepLayoutManager();
//		}
//		@Override
//		protected boolean paintComponent( java.awt.Graphics2D g2 ) {
//			return NoteStep.this.paintComponent( g2, this, this.note.getComponent( 0 ) );
//		}
//	}
//	
//	private static final int HOLE_PAD = 4;
//	private edu.cmu.cs.dennisc.croquet.Component< ? > component;
//	private StepPanel notePanel = new StepPanel();
//	private String title;
//	private boolean isHole;
//
//	public static NoteStep createMessageNoteStep( Tutorial tutorial, String title, String text ) {
//		return new NoteStep( tutorial, title, text, null, false );
//	}
//	public static NoteStep createSpotlightMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
//		return new NoteStep( tutorial, title, text, component, false );
//	}
//	public static NoteStep createActionMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
//		return new NoteStep( tutorial, title, text, component, true );
//	}
//	private NoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component, boolean isHole ) {
//		super( tutorial );
//		this.title = title;
//		this.notePanel.note.setText( text );
//		this.component = component;
//		this.isHole = isHole;
//	}
//	@Override
//	public String toString() {
//		return this.title;
//	}
//	
//	@Override
//	public edu.cmu.cs.dennisc.croquet.Component< ? > getComponent() {
//		return this.notePanel;
//	}
//	
//	@Override
//	public java.awt.geom.Area subtractIfAppropriate( java.awt.geom.Area rv, edu.cmu.cs.dennisc.croquet.Component<?> panel, boolean isStrict ) {
//		if( this.component != null ) {
//			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
//			if( this.isHole ) {
//				if( isStrict ) {
//					//pass
//				} else {
//					edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
//				}
//			} else {
//				if( isStrict ) {
//					componentBounds = null;
//				}
//			}
//			if( componentBounds != null ) {
//				rv.subtract( new java.awt.geom.Area( componentBounds ) );
//			}
//		}
//		return rv;
//	}
//	
//	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo ) {
//		float xVector = xTo-xFrom; 
//		float yVector = yTo-yFrom;
//		
//		final float A = 0.15f;
//		final float B = 1.0f;
//		
//		float xA = xFrom + xVector*A;
//		float yA = yFrom + yVector*A;
//
//		float xB = xFrom + xVector*B;
//		float yB = yFrom + yVector*B;
//		
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		path.moveTo( xFrom, yFrom );
//		path.curveTo( xB, yA, xA, yB, xTo, yTo );
//
//		g2.draw( path );
//		//g2.drawLine( (int)xB, (int)yA, (int)xA, (int)yB );
//	}
//
//	private final static java.awt.Paint HIGHLIGHT_PAINT = new java.awt.Color( 255, 255, 0, 23 );
//	private final static java.awt.Stroke[] HIGHLIGHT_STROKES = {
//		new java.awt.BasicStroke(  5.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 10.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 15.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 20.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 25.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 30.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 35.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ),
//		new java.awt.BasicStroke( 40.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND )
//	};
//	
//	private static void fill( java.awt.Graphics2D g2, java.awt.Shape shape, float x, float y, double theta ) {
//		java.awt.geom.AffineTransform m = g2.getTransform();
//		try {
//			g2.translate( x, y );
//			g2.rotate( theta );
//			g2.fill( shape );
//		} finally {
//			g2.setTransform( m );
//		}
//	}
//	protected boolean paintComponent( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Panel panel, edu.cmu.cs.dennisc.croquet.Component<?> note ) {
//		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//		if( this.component != null ) {
//			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
//			if( this.isHole ) {
//				edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
//			}
//			
//			java.awt.Stroke prevStroke = g2.getStroke();
//			
//			if( this.isHole ) {
//				java.awt.Shape prevClip = g2.getClip();
//				
//				java.awt.geom.Area area = new java.awt.geom.Area( prevClip );
//				area.subtract( new java.awt.geom.Area( componentBounds ) );
//				
//				g2.setClip( area );
//
//				g2.setPaint( HIGHLIGHT_PAINT );
//				for( java.awt.Stroke stroke : HIGHLIGHT_STROKES ) {
//					g2.setStroke( stroke );
//					g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//				}
//
//				g2.setClip( prevClip );
//			}
//	
//			
//			if( this.isHole ) {
//				g2.setStroke( HOLE_BEVEL_STROKE );
//				
////				g2.setPaint( java.awt.Color.GRAY );
////				g2.draw3DRect(componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height, false);
//
//				int x0 = componentBounds.x;
//				int y0 = componentBounds.y;
//				int x1 = componentBounds.x + componentBounds.width;
//				int y1 = componentBounds.y + componentBounds.height;
//				g2.setPaint( java.awt.Color.LIGHT_GRAY );
//				g2.drawLine( x1, y0, x1, y1 );
//				g2.drawLine( x1, y1, x0, y1 );
//				g2.setPaint( java.awt.Color.DARK_GRAY );
//				g2.drawLine( x0, y1, x0, y0 );
//				g2.drawLine( x0, y0, x1, y0 );
//			} else {
//				//g2.draw3DRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height, true );
//				g2.setPaint( getCallOutPaint() );
//				g2.fillRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//
//				g2.setStroke( FRAME_STROKE );
//				g2.setPaint( java.awt.Color.RED );
//				g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
//				
//				g2.setPaint( java.awt.Color.BLACK );
//
//				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//				path.moveTo( -3, -3 );
//				path.lineTo( -3, 9 );
//				path.lineTo( 3, 6 );
//				path.lineTo( 3, 3 );
//				path.lineTo( 6, 3 );
//				path.lineTo( 9, -3 );
//				path.closePath();
//
//				//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
//				fill( g2, path, componentBounds.x, componentBounds.y, 0 );
//				fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y, Math.PI/2 );
//				fill( g2, path, componentBounds.x + componentBounds.width, componentBounds.y + componentBounds.height, Math.PI );
//				fill( g2, path, componentBounds.x, componentBounds.y + componentBounds.height, 3*Math.PI/2 );
//				//g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
//			}
//			g2.setPaint( java.awt.Color.BLACK );
//
//					
//			g2.setStroke( ARROW_STROKE );
//			//g2.draw( createNoisyRectangle( componentBounds, HOLE_PAD ) );
//
//			java.awt.Rectangle noteBounds = note.getBounds( panel );
//			
//			java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( componentBounds, javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER );
//			java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER );
//			
//			final int ARROW_WIDTH = 19;
//			final int ARROW_HALF_HEIGHT = 6;
//			
//			drawPath( g2, ptNote.x, ptNote.y, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1);
//			g2.setStroke( prevStroke );
//
//			int x = ptComponent.x;
//			int y = ptComponent.y - ARROW_HALF_HEIGHT;
//			g2.translate( x, y );
//			edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.WEST, new java.awt.Dimension( ARROW_WIDTH, ARROW_HALF_HEIGHT*2+1 ) );
//			g2.translate( -x, -y );
//		}
//		return true;
//	}
//}
