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
/*package-private*/ abstract class NoteStep extends Step {
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
		public Note getNote() {
			return this.note;
		}
	}
	
	private StepPanel notePanel = new StepPanel();
	private String title;

//	private edu.cmu.cs.dennisc.croquet.Model modelWaitingOn;
//	public static NoteStep createMessageNoteStep( Tutorial tutorial, String title, String text ) {
//		return new NoteStep( tutorial, title, text, null, null );
//	}
//	public static NoteStep createSpotlightMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component ) {
//		return new NoteStep( tutorial, title, text, new Frame(component, Feature.ConnectionPreference.EAST_WEST ), null );
//	}
//	public static NoteStep createActionMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component, edu.cmu.cs.dennisc.croquet.Model modelWaitingOn ) {
//		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.EAST_WEST), modelWaitingOn );
//	}
//	public static NoteStep createBooleanStateMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component, edu.cmu.cs.dennisc.croquet.Model modelWaitingOn ) {
//		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.EAST_WEST), modelWaitingOn );
//	}
//	public static NoteStep createTabStateMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component ) {
//		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.NORTH_SOUTH), null );
//	}
//	public static NoteStep createDialogOpenMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component, edu.cmu.cs.dennisc.croquet.Model modelWaitingOn ) {
//		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.EAST_WEST), modelWaitingOn );
//	}
//	public static NoteStep createDialogCloseMessageNoteStep( Tutorial tutorial, String title, String text, edu.cmu.cs.dennisc.croquet.JComponent< ? > component, edu.cmu.cs.dennisc.croquet.Model modelWaitingOn ) {
//		return new NoteStep( tutorial, title, text, new Hole(component, Feature.ConnectionPreference.EAST_WEST), modelWaitingOn );
//	}
	
	public NoteStep( Tutorial tutorial, String title, String text ) {
		super( tutorial );
		this.title = title;
		this.notePanel.note.setText( text );
	}
	
	@Override
	public String toString() {
		return this.title;
	}
	
	@Override
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > getNote() {
		return this.notePanel.getNote();
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > getCard() {
		return this.notePanel;
	}
}	
