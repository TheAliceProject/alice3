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
					int w = this.getWidth();
					int h = this.getHeight();
					int x1 = w-20;
					int y1 = h-20;
					java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, HIGHLIGHT_COLOR, x1-200, y1-200, BASE_COLOR );
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					g2.setPaint( paint );
					g2.fill( g2.getClip() );
					super.paintComponent( g );
					g2.setPaint( java.awt.Color.LIGHT_GRAY );
					g2.fillRect( w-1, 0, 1, h );
					g2.setPaint( java.awt.Color.DARK_GRAY );
					g2.fillRect( 0, h-1, w-1, 1 );
				}
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
			return NoteStep.this.paintComponent( g2, this, this.note );
		}
	}
	
	private edu.cmu.cs.dennisc.croquet.Component< ? > component;
	private StepPanel notePanel = new StepPanel();
	private String title;
	private boolean isHole;

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
	public java.awt.geom.Area subtractHoleIfAppropriate( java.awt.geom.Area rv, edu.cmu.cs.dennisc.croquet.Panel panel ) {
		if( this.component != null ) {
			if( this.isHole ) {
				java.awt.Rectangle componentBounds = this.component.getBounds( panel );
				edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, 3 );
				rv.subtract( new java.awt.geom.Area( componentBounds ) );
			}
		}
		return rv;
	}
	protected boolean paintComponent( java.awt.Graphics2D g2, edu.cmu.cs.dennisc.croquet.Panel panel, edu.cmu.cs.dennisc.croquet.Component<?> note ) {
		if( this.component != null ) {
			java.awt.Rectangle componentBounds = this.component.getBounds( panel );
			edu.cmu.cs.dennisc.java.awt.RectangleUtilties.grow( componentBounds, 3 );
			
			java.awt.Paint paint;
			if( this.isHole ) {
				paint = java.awt.Color.BLACK;
			} else {
				paint = java.awt.Color.RED;
			}
			g2.setPaint( paint );
			
			java.awt.Stroke prevStroke = g2.getStroke();
			g2.setStroke( STROKE );
			g2.drawRect( componentBounds.x, componentBounds.y, componentBounds.width, componentBounds.height );

			java.awt.Rectangle noteBounds = note.getBounds( panel );
			
			java.awt.Point ptComponent = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( componentBounds, javax.swing.SwingConstants.TRAILING, javax.swing.SwingConstants.CENTER );
			java.awt.Point ptNote = edu.cmu.cs.dennisc.java.awt.RectangleUtilties.getPoint( noteBounds, javax.swing.SwingConstants.LEADING, javax.swing.SwingConstants.CENTER );
			
			final int ARROW_WIDTH = 19;
			final int ARROW_HALF_HEIGHT = 6;
			
			float xDistance = Math.abs( ptComponent.x - ptNote.x );
			float yDistance = Math.abs( ptComponent.y - ptNote.y );
			
			float x1 = ptComponent.x + xDistance*0.15f;
			float y1 = ptComponent.y + yDistance*0.85f;
			float x2 = ptComponent.x + xDistance*0.85f;
			float y2 = ptComponent.y + yDistance*0.0f;

			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( ptNote.x, ptNote.y );
			path.curveTo( x1, y1, x2, y2, ptComponent.x+ARROW_WIDTH-1, ptComponent.y-1 );
			//path.closePath();
			
			g2.draw( path );
			
			//g2.drawLine( (int)x1, (int)y1, (int)x2, (int)y2 );
			
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
