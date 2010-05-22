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
	private static java.awt.Paint highlightPaint = null;
	private static java.awt.Paint getHighlightPaint() {
		if( NoteStep.highlightPaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			java.awt.Color c = Tutorial.STENCIL_BASE_COLOR;
			g2.setColor( new java.awt.Color( c.getRed(), c.getGreen(), c.getBlue(), 63 ) );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( Tutorial.STENCIL_LINE_COLOR );
			g2.drawLine( 0, height, width, 0 );
			g2.drawLine( 0, 0, 0, 0 );
//			g2.drawLine( 0, 0, width, height );
			g2.dispose();
			NoteStep.highlightPaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return NoteStep.highlightPaint;
	}

	private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( 2.0f ); 

	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
	private class Note extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
		private javax.swing.JTextPane textPane = new javax.swing.JTextPane();

		@Override
		protected javax.swing.JComponent createAwtComponent() {
			this.textPane.setContentType( "text/html" );
			this.textPane.setOpaque( false );
			this.textPane.setEditable( false );
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
			rv.add( this.textPane, java.awt.BorderLayout.NORTH );
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
			this.textPane.setText( text );
		}
		private javax.swing.event.MouseInputListener mouseInputListener = new javax.swing.event.MouseInputListener() {

			private java.awt.event.MouseEvent ePressed;
			private java.awt.Point ptPressed;
			public void mouseClicked( java.awt.event.MouseEvent e ) {
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
			int x = 300;
			int y = 260;
			for( java.awt.Component component : parent.getComponents() ) {
				component.setSize( component.getPreferredSize() );
				component.setLocation( x, y );
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
		@Override
		protected boolean paintComponent( java.awt.Graphics2D g2 ) {
			return NoteStep.this.paintComponent( g2, this, this.note.getComponent( 0 ) );
		}
	}
	
	private static final int HOLE_PAD = 4;
	private edu.cmu.cs.dennisc.croquet.Component< ? > component;
	private StepPanel notePanel = new StepPanel();
	private String title;
	private boolean isHole;

//	private static float generateNoise() {
//		return (float)(( Math.random() * 3.0 ) - 1.5 );
//	}
//	private float x0ADelta = generateNoise();
//	private float x0BDelta = generateNoise();
//	private float x1ADelta = generateNoise();
//	private float x1BDelta = generateNoise();
//	private float y0ADelta = generateNoise();
//	private float y0BDelta = generateNoise();
//	private float y1ADelta = generateNoise();
//	private float y1BDelta = generateNoise();
	
	public static NoteStep createMessageNoteStep( Tutorial tutorial, String title, String text ) {
		return new NoteStep( tutorial, title, text, null, false );
	}
	public static NoteStep createSpotlightMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		return new NoteStep( tutorial, title, text, component, false );
	}
	public static NoteStep createActionMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component ) {
		return new NoteStep( tutorial, title, text, component, true );
	}
	private NoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.Component< ? > component, boolean isHole ) {
		super( tutorial );
		this.title = title;
		this.notePanel.note.setText( text );
		this.component = component;
		this.isHole = isHole;
	}
	@Override
	public String toString() {
		return this.title;
	}
	
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > getComponent() {
		return this.notePanel;
	}
	
	@Override
	public java.awt.geom.Area subtractIfAppropriate( java.awt.geom.Area rv, edu.cmu.cs.dennisc.croquet.Component<?> panel, boolean isStrict ) {
		if( this.component != null ) {
			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
			if( this.isHole ) {
				if( isStrict ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
				}
			} else {
				if( isStrict ) {
					componentBounds = null;
				}
			}
			if( componentBounds != null ) {
				rv.subtract( new java.awt.geom.Area( componentBounds ) );
			}
		}
		return rv;
	}

//	public java.awt.geom.GeneralPath createNoisyRectangle( Rectangle componentBounds, int noise ) {
//		int x0 = componentBounds.x;
//		int y0 = componentBounds.y;
//		int x1 = componentBounds.x + componentBounds.width;
//		int y1 = componentBounds.y + componentBounds.height;
//		
//		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
//		rv.moveTo( x0 + x0ADelta, y0 + y0ADelta );
//		rv.lineTo( x1 + x1ADelta, y0 + y0BDelta );
//		rv.lineTo( x1 + x1BDelta, y1 + y1ADelta );
//		rv.lineTo( x0 + x0BDelta, y1 + y1BDelta );
//		rv.closePath();
//		return rv;
//	}
	
	private static void drawPath( java.awt.Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo ) {
//		float xDistance = Math.abs( ptComponent.x - ptNote.x );
//		float yDistance = Math.abs( ptComponent.y - ptNote.y );
//		
//		float x1 = ptComponent.x + xDistance*0.15f;
//		float y1 = ptComponent.y + yDistance*0.85f;
//		float x2 = ptComponent.x + xDistance*0.85f;
//		float y2 = ptComponent.y + yDistance*0.0f;
//
//		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//		path.moveTo( ptNote.x, ptNote.y );
//		path.curveTo( x1, y1, x2, y2, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1 );
//		//path.closePath();
//		

		float xVector = xTo-xFrom; 
		float yVector = yTo-yFrom;
		
		final float A = 0.15f;
		final float B = 1.0f;
		
		float xA = xFrom + xVector*A;
		float yA = yFrom + yVector*A;

		float xB = xFrom + xVector*B;
		float yB = yFrom + yVector*B;
		
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xFrom, yFrom );
		path.curveTo( xB, yA, xA, yB, xTo, yTo );

		g2.draw( path );
		//g2.drawLine( (int)xB, (int)yA, (int)xA, (int)yB );
	}

	protected boolean paintComponent( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Panel panel, edu.cmu.cs.dennisc.croquet.Component<?> note ) {
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		if( this.component != null ) {
			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
			if( this.isHole ) {
				edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, HOLE_PAD );
			}
			
			java.awt.Stroke prevStroke = g2.getStroke();
			
			if( this.isHole ) {
				java.awt.Shape prevClip = g2.getClip();
				
				java.awt.geom.Area area = new java.awt.geom.Area( prevClip );
				area.subtract( new java.awt.geom.Area( componentBounds ) );
				
				g2.setClip( area );

				g2.setPaint( new java.awt.Color( 255, 255, 0, 15 ) );
				for( float width = 1.0f; width<49.0f; width+=7.0f ) {
					g2.setStroke( new java.awt.BasicStroke( width, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );
					g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
				}

				g2.setClip( prevClip );
			}
	
			g2.setStroke( STROKE );
			
//			java.awt.Paint paint;
			if( this.isHole ) {
//				paint = java.awt.Color.BLACK;
				int x0 = componentBounds.x;
				int y0 = componentBounds.y;
				int x1 = componentBounds.x + componentBounds.width;
				int y1 = componentBounds.y + componentBounds.height;
				g2.setPaint( java.awt.Color.WHITE );
				g2.drawLine( x1, y0, x1, y1 );
				g2.drawLine( x1, y1, x0, y1 );
				g2.setPaint( java.awt.Color.DARK_GRAY );
				g2.drawLine( x0, y1, x0, y0 );
				g2.drawLine( x0, y0, x1, y0 );
			} else {
				g2.setStroke( prevStroke );
				g2.setPaint( java.awt.Color.GRAY );
				g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
				//g2.draw3DRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height, true );
				g2.setPaint( getHighlightPaint() );
				g2.fillRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );
				g2.setPaint( java.awt.Color.BLACK );

				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
				path.moveTo( -2, -2 );
				path.lineTo( -2, HOLE_PAD+4 );
				path.lineTo( 2, HOLE_PAD+2 );
				path.lineTo( 2, 2 );
				path.lineTo( HOLE_PAD+2, 2 );
				path.lineTo( HOLE_PAD+4, -2 );
				path.closePath();

				float xt;
				float yt;
				float theta;
				
				xt = componentBounds.x;
				yt = componentBounds.y;
				theta = 0;
				
				g2.translate( xt, yt );
				g2.fill( path );
				g2.translate( -xt, -yt );

				xt = componentBounds.x + componentBounds.width;
				yt = componentBounds.y;
				theta = (float)Math.PI/2;
				g2.translate( xt, yt );
				g2.rotate( theta );
				g2.fill( path );
				g2.rotate( -theta );
				g2.translate( -xt, -yt );

				xt = componentBounds.x + componentBounds.width;
				yt = componentBounds.y + componentBounds.height;
				theta = (float)Math.PI;
				g2.translate( xt, yt );
				g2.rotate( theta );
				g2.fill( path );
				g2.rotate( -theta );
				g2.translate( -xt, -yt );

				xt = componentBounds.x;
				yt = componentBounds.y + componentBounds.height;
				theta = (float)-Math.PI/2;
				g2.translate( xt, yt );
				g2.rotate( theta );
				g2.fill( path );
				g2.rotate( -theta );
				g2.translate( -xt, -yt );
				
				g2.setStroke( STROKE );
			}
			g2.setPaint( java.awt.Color.BLACK );

					
			//g2.draw( createNoisyRectangle( componentBounds, HOLE_PAD ) );

			java.awt.Rectangle noteBounds = note.getBounds( panel );
			
			java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( componentBounds, javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER );
			java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER );
			
			final int ARROW_WIDTH = 19;
			final int ARROW_HALF_HEIGHT = 6;
			
			drawPath( g2, ptNote.x, ptNote.y, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1);
			g2.setStroke( prevStroke );

			int x = ptComponent.x;
			int y = ptComponent.y - ARROW_HALF_HEIGHT;
			g2.translate( x, y );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.WEST, new java.awt.Dimension( ARROW_WIDTH, ARROW_HALF_HEIGHT*2+1 ) );
			g2.translate( -x, -y );
		}
		return true;
	}
}
