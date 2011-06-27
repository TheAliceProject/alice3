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
package org.lgna.stencil;

/**
 * @author Dennis Cosgrove
 */
public abstract class Note extends org.lgna.croquet.components.JComponent< javax.swing.JComponent > {
	public class JNote extends javax.swing.JPanel {
		public boolean isActive() {
			return Note.this.isActive();
		}
	}
	private static java.awt.Composite INACTIVE_COMPOSITE = java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 0.3f );
	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
	
	private static int X_PAD = 16;
	private static int Y_PAD = 16;
	private final java.util.List< Feature > features = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private String label = null;
	

	protected abstract String getText();
	protected abstract org.lgna.croquet.Operation< ? > getNextOperation();
	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	protected void addFeature( Feature feature ) {
		this.features.add( feature );
	}
	public java.util.List< Feature > getFeatures() {
		return this.features;
	}
	public java.awt.Point calculateLocation( org.lgna.croquet.components.Container< ? > container ) {
		java.awt.Point rv = new java.awt.Point( 20, 20 );
		if( this.features.size() > 0 ) {
			Feature feature = this.features.get( 0 );
			rv = feature.calculateNoteLocation( container, this );
		} else {
			rv.x = (container.getWidth()-this.getWidth())/2;
			rv.y = 320;
		}

		System.err.println( "todo: remove text calculateLocation special case" );
		if( this.getText().contains( "Drag..." ) ) {
			rv.y = 400;
		}
		return rv;
	}

//	private boolean prevFeatureInView = false;
//	private boolean isRepaintRequiredForFeatureViewChanged = false;
//
//	private boolean isFeatureInView() {
//		for( Feature feature : this.features ) {
//			edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = feature.getTrackableShape();
//			if( trackableShape != null ) {
//				if( trackableShape.isInView() ) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	/*package-private*/ boolean isRepaintAllRequired() {
//		boolean nextFeatureInView = isFeatureInView();
//		if( nextFeatureInView != prevFeatureInView ) {
//			prevFeatureInView = nextFeatureInView;
//			if( isRepaintRequiredForFeatureViewChanged ) {
//				return true;
//			}
//		}
//		isRepaintRequiredForFeatureViewChanged = true;
//		return false;
//	}
		
	public org.lgna.croquet.edits.ReplacementAcceptability getReplacementAcceptability() {
		return null;
	}

	@Override
	protected javax.swing.JComponent createAwtComponent() {
		javax.swing.JEditorPane textComponent = new javax.swing.JEditorPane() {
			@Override
			public boolean contains(int x, int y) {
				return false;
			}
			@Override
			public void updateUI() {
				this.setUI( new javax.swing.plaf.basic.BasicEditorPaneUI() );
			}
		};
		textComponent.setContentType( "text/html" );
		textComponent.setOpaque( false );
		textComponent.setEditable( false );

		String text = this.getText();
		//does not appear to be necessary
		final String PREFIX = "<html>";
		final String POSTFIX = "</html>";
		StringBuilder sb = new StringBuilder();
		if( text.startsWith( PREFIX ) ) {
			//pass
		} else {
			sb.append( PREFIX );
		}
		sb.append( text );
		if( text.endsWith( POSTFIX ) ) {
			//pass
		} else {
			sb.append( POSTFIX );
		}
		textComponent.setText( sb.toString() );

		//textPane.setEnabled( false );

		JNote rv = new JNote() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

				int x = X_PAD;
				int y = Y_PAD;
				
				int w = this.getWidth()-x;
				int h = this.getHeight()-y;

				String label = Note.this.getLabel();
				java.awt.Shape shape = new java.awt.geom.Rectangle2D.Float( x, y, w-4, h-4 );
				
				if( label != null ) {
					java.awt.geom.Area area = new java.awt.geom.Area( shape );
					area.add( new java.awt.geom.Area( new java.awt.geom.Ellipse2D.Float( 0, 0, X_PAD*3, Y_PAD*3 ) ) );
					shape = area;
				}
				
				int x1 = w-20;
				int y1 = h-20;
				java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, HIGHLIGHT_COLOR, x1-200, y1-200, BASE_COLOR );
				g2.setPaint( paint );
				
				g2.fill( shape );

				if( label != null ) {
					java.awt.Font prevFont = g2.getFont();
					g2.setPaint( java.awt.Color.BLUE.darker().darker() );
					g2.fillOval( 3, 3, X_PAD*3-6, Y_PAD*3-6 );
					g2.setPaint( java.awt.Color.YELLOW );
					
					java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont(prevFont, 2.5f);
					g2.setFont( font );
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText(g2, label, 0, 0, X_PAD*3, Y_PAD*3 );
					g2.setFont( prevFont );
				}

				if( Note.this.isActive() ) {
					g2.translate(x, y);
					g2.setPaint( java.awt.Color.GRAY );
					java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
					pathShadow.moveTo( w-4, 0 );
					pathShadow.lineTo( w, h );
					pathShadow.lineTo( 0, h-4 );
					pathShadow.lineTo( w-4, h-4 );
					pathShadow.closePath();
					g2.fill( pathShadow );
					g2.translate(-x, -y);
				}
				super.paintComponent( g );
			}
			@Override
			protected void paintChildren(java.awt.Graphics g) {
				if( Page.IS_NOTE_OVERLAPPING_DESIRED==false || Note.this.isActive() ) {
					super.paintChildren(g);
				}
			}
			@Override
			public void paint(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.Composite composite = g2.getComposite();
				if( Note.this.isActive() ) {
					//pass
				} else {
					g2.setComposite( INACTIVE_COMPOSITE );
				}
				super.paint(g);
				g2.setComposite( composite );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				rv.width = 270;
				rv.width *= 2;
				rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( rv, rv.width );
				return rv;
			}
		};
		rv.setLayout( new java.awt.BorderLayout() );
		rv.add( textComponent, java.awt.BorderLayout.NORTH );
		rv.setCursor( java.awt.Cursor.getDefaultCursor() );
		
		//rv.setBackground( BASE_COLOR );
		org.lgna.croquet.components.BorderPanel southPanel = new org.lgna.croquet.components.BorderPanel();

		org.lgna.croquet.components.Hyperlink hyperlink = getNextOperation().createHyperlink();
		hyperlink.scaleFont( 1.4f );
		southPanel.addComponent( hyperlink, org.lgna.croquet.components.BorderPanel.Constraint.LINE_END );

		rv.add( southPanel.getAwtComponent(), java.awt.BorderLayout.SOUTH );
		final int X_BORDER_PAD = 16;
		final int Y_BORDER_PAD = 12;
		int top = Y_PAD+Y_BORDER_PAD;
		int bottom = Y_BORDER_PAD;
		int left = Y_PAD+X_BORDER_PAD;
		int right = X_BORDER_PAD;
		
		if( this.label != null ) {
			top += 8;
			left += 8;
		}
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( top, left, bottom, right ) );
		rv.setBackground( BASE_COLOR );
		rv.setOpaque( false );
		return rv;
	}
	private javax.swing.event.MouseInputListener mouseInputListener = new javax.swing.event.MouseInputListener() {

		private java.awt.event.MouseEvent ePressed;
		private java.awt.Point ptPressed;
		public void mouseClicked( java.awt.event.MouseEvent e ) {
			if( e.getClickCount() == 2 ) {
				getNextOperation().fire(e);
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
			Note.this.getAwtComponent().getParent().repaint();
		}

		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}
		
	};
		
	private boolean isActive = true;
	public boolean isActive() {
		return this.isActive;
	}
	public void setActive( boolean isActive ) {
		if( this.isActive != isActive ) {
			this.isActive = isActive;
			for( Feature feature : this.features ) {
				feature.updateTrackableShapeIfNecessary();
			}
			org.lgna.croquet.components.Container< ? > container = this.getParent();
			if( container != null ) {
				container.repaint();
			}
		}
	}
	
	private void bind() {
		for( Feature feature : this.features ) {
			feature.bind();
		}
	}
	private void unbind() {
		for( Feature feature : this.features ) {
			feature.unbind();
		}
	}

	private java.awt.event.HierarchyListener hierarchyListener = new java.awt.event.HierarchyListener() {
		public void hierarchyChanged( java.awt.event.HierarchyEvent e ) {
			if( ( e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED ) != 0 ) {
				Note.this.handleShowingChanged( e.getChanged().isShowing() );
			}
		}
	};
	
	private void handleShowingChanged( boolean isShowing ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleShowingChanged", isShowing );
	}
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.addHierarchyListener( this.hierarchyListener );
		this.addMouseListener( this.mouseInputListener );
		this.addMouseMotionListener( this.mouseInputListener );
		this.bind();
	}
	@Override
	protected void handleUndisplayable() {
		this.unbind();
		this.removeMouseMotionListener( this.mouseInputListener );
		this.removeMouseListener( this.mouseInputListener );
		this.removeHierarchyListener( this.hierarchyListener );
		super.handleUndisplayable();
	}
	
	public void reset() {
		unbind();
		bind();
	}
}
