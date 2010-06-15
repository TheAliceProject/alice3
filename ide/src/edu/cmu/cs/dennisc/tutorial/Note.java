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
/*package-private*/ class Note extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
	/*package-private*/ class JNote extends javax.swing.JPanel {
		public boolean isActive() {
			return Note.this.isActive();
		}
	}
	private static java.awt.Composite INACTIVE_COMPOSITE = java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 0.3f );
	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
	
	private static int X_PAD = 16;
	private static int Y_PAD = 16;
	private Tutorial tutorial;
	private java.util.List< Feature > features = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private String text;
	private String label = null;
	
	public Note( String text ) {
		assert text != null;
		this.text = text;
	}
	
	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Tutorial getTutorial() {
		return this.tutorial;
	}
	public void setTutorial(Tutorial tutorial) {
		this.tutorial = tutorial;
	}
	
	protected void addFeature( Feature feature ) {
		this.features.add( feature );
	}
	public java.util.List< Feature > getFeatures() {
		return this.features;
	}
	/*package-private*/ java.awt.Point calculateLocation( edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
		java.awt.Point rv = new java.awt.Point( 20, 20 );
		if( this.features.size() > 0 ) {
			Feature feature = this.features.get( 0 );
			rv = feature.calculateNoteLocation( container, this );
		} else {
			rv.x = (container.getWidth()-this.getWidth())/2;
			rv.y = 64;
		}
		return rv;
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
		
		//does not appear to be necessary
		final String PREFIX = "<html>";
		final String POSTFIX = "</html>";
		StringBuilder sb = new StringBuilder();
		if( this.text.startsWith( PREFIX ) ) {
			//pass
		} else {
			sb.append( PREFIX );
		}
		sb.append( this.text );
		if( this.text.endsWith( POSTFIX ) ) {
			//pass
		} else {
			sb.append( POSTFIX );
		}
		textComponent.setText( sb.toString() );

		//this.textPane.setEnabled( false );

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
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.drawCenteredText(g2, label, 0, 0, X_PAD*3, Y_PAD*3 );
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
				if( Step.IS_NOTE_OVERLAPPING_DESIRED==false || Note.this.isActive() ) {
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
				rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumHeight( rv, 256 );
				rv.width = 256;
				return rv;
			}
		};
		rv.setLayout( new java.awt.BorderLayout() );
		rv.add( textComponent, java.awt.BorderLayout.NORTH );
		//rv.setBackground( BASE_COLOR );
		edu.cmu.cs.dennisc.croquet.BorderPanel southPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		edu.cmu.cs.dennisc.croquet.Hyperlink hyperlink = getTutorial().getNextOperation().createHyperlink();
		hyperlink.scaleFont( 1.4f );
		southPanel.addComponent( hyperlink, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
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
			edu.cmu.cs.dennisc.croquet.Container< ? > container = this.getParent();
			if( container != null ) {
				container.repaint();
			}
		}
	}

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
