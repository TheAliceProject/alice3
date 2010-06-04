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
public abstract class Step {
	private static java.awt.Composite NOTE_INACTIVE_COMPOSITE = java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 0.5f );
	private static java.awt.Color NOTE_BASE_COLOR = new java.awt.Color( 255, 255, 100 ); 
	private static java.awt.Color NOTE_HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );
	/*package-private*/ class Note extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > {
		private java.util.List< Feature > features = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		private String text;
		public Note( String text ) {
			assert text != null;
			this.text = text;
		}
		protected void addFeature( Feature feature ) {
			this.features.add( feature );
		}
		public Iterable< Feature > getFeatures() {
			return this.features;
		}
		private void setLocation( edu.cmu.cs.dennisc.croquet.Container< ? > card ) {
			assert card == this.getParent();
			javax.swing.JFrame jFrame = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtWindow();
			javax.swing.JLayeredPane layeredPane = jFrame.getLayeredPane();
			java.awt.Rectangle cardBounds = javax.swing.SwingUtilities.getLocalBounds( layeredPane );
			assert cardBounds.width > 0 && cardBounds.height > 0;
			
			java.awt.Rectangle noteBounds = this.getLocalBounds();
			int x = 20;
			int y = 20;
			if( this.features.size() > 0 ) {
				Feature feature = this.features.get( 0 );
				Feature.Connection actualConnection = null;
				edu.cmu.cs.dennisc.croquet.Component<?> featureComponent = feature.getComponent();
				if( featureComponent != null ) {
					java.awt.Rectangle featureComponentBounds = featureComponent.getBounds( this.getParent() );
					Feature.ConnectionPreference connectionPreference = feature.getConnectionPreference();
					if( connectionPreference == Feature.ConnectionPreference.EAST_WEST ) {
						x = getXForWestLayout( noteBounds, featureComponentBounds );
						if( x >= 32 ) {
							actualConnection = Feature.Connection.WEST;
						} else {
							x = getXForEastLayout(noteBounds, featureComponentBounds);
							if( x <= ( cardBounds.width - noteBounds.width - 32 ) ) {
								actualConnection = Feature.Connection.EAST;
							}
						}
					}
					if( actualConnection != null ) {
						int yFeatureComponentCenter = featureComponentBounds.y + featureComponentBounds.height/2;
						int yCardCenter = ( cardBounds.y + cardBounds.height ) / 2;
						y = yFeatureComponentCenter;
						if( yFeatureComponentCenter < yCardCenter ) {
							y += 200;
						} else {
							y -= noteBounds.height;
							y -= 200;
						}
					} else {
						y = getYForNorthLayout( noteBounds, featureComponentBounds );
						if( y >= 32 ) {
							actualConnection = Feature.Connection.NORTH;
						} else {
							y = getYForSouthLayout( noteBounds, featureComponentBounds);
							if( y <= ( cardBounds.height - noteBounds.height - 32 ) ) {
								actualConnection = Feature.Connection.SOUTH;
							} else {
								actualConnection = Feature.Connection.SOUTH;
								y = 200;
							}
						}
						int xFeatureComponentCenter = featureComponentBounds.x + featureComponentBounds.width/2;
						int xCardCenter = ( cardBounds.x + cardBounds.width ) / 2;
						x = xFeatureComponentCenter;
						if( xFeatureComponentCenter < xCardCenter ) {
							x += 200;
						} else {
							x -= noteBounds.width;
							x -= 200;
						}
					}
				}
				feature.setActualConnection( actualConnection );
			} else {
				x = (cardBounds.width-noteBounds.width)/2;
				y = 64;
			}
			this.getAwtComponent().setLocation( x, y );
		}

		@Override
		protected javax.swing.JComponent createAwtComponent() {
			javax.swing.JTextPane textComponent = new javax.swing.JTextPane() {
				@Override
				public boolean contains(int x, int y) {
					return false;
				}
			};
			textComponent.setContentType( "text/html" );
			textComponent.setOpaque( false );
			textComponent.setEditable( false );
			textComponent.setText( this.text );
			//this.textPane.setEnabled( false );

			javax.swing.JPanel rv = new javax.swing.JPanel() {
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					int w = this.getWidth();
					int h = this.getHeight();
					if( Note.this.isActive() ) {
						g2.setPaint( java.awt.Color.GRAY );
						java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
						pathShadow.moveTo( w-4, 0 );
						pathShadow.lineTo( w, h );
						pathShadow.lineTo( 0, h-4 );
						pathShadow.lineTo( w-4, h-4 );
						pathShadow.closePath();
						g2.fill( pathShadow );
					}
					int x1 = w-20;
					int y1 = h-20;
					java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, NOTE_HIGHLIGHT_COLOR, x1-200, y1-200, NOTE_BASE_COLOR );
					g2.setPaint( paint );
					
					g2.fillRect( 0, 0, w-4, h-4 );
					
					super.paintComponent( g );
				}
				@Override
				public void paint(java.awt.Graphics g) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					java.awt.Composite composite = g2.getComposite();
					if( Note.this.isActive() ) {
						//pass
					} else {
						g2.setComposite( NOTE_INACTIVE_COMPOSITE );
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
			
			edu.cmu.cs.dennisc.croquet.BorderPanel southPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
			southPanel.addComponent( getTutorial().getNextOperation().createHyperlink(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST );
			rv.add( southPanel.getAwtComponent(), java.awt.BorderLayout.SOUTH );
			rv.setOpaque( false );
			final int X_PAD = 16;
			final int Y_PAD = 12;
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD,X_PAD,Y_PAD,X_PAD ) );
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
						note.setLocation( Step.this.stepPanel );
					} else {
						awtComponent.setLocation( 10, 10 );
					}
					set.add( awtComponent );
				}
			}
		}
	}

	private class StepPanel extends edu.cmu.cs.dennisc.croquet.Panel {
		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new StepLayoutManager();
		}
		@Override
		protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			for( Note note : Step.this.getNotes() ) {
				this.internalAddComponent( note );
			}
			super.handleAddedTo( parent );
		}
		@Override
		protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
			this.removeAllComponents();
			super.handleRemovedFrom( parent );
		}
	}

	private java.util.UUID id = java.util.UUID.randomUUID();
	private Tutorial tutorial;
	private StepPanel stepPanel = new StepPanel();
	private java.util.List< Note > notes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private String title;
	public Step( Tutorial tutorial, String title, String text ) {
		this.tutorial = tutorial;
		this.title = title;
		this.addNote( new Note( text ) );
	}
	public void addNote( Note note ) {
		this.notes.add( note );
	}
	public Note getNoteAt( int index ) {
		return this.notes.get( index );
	}
	public Iterable< Note > getNotes() {
		return this.notes;
	}
	public edu.cmu.cs.dennisc.croquet.Component< ? > getCard() {
		return this.stepPanel;
	}

	public java.util.UUID getId() {
		return this.id;
	}
	public Tutorial getTutorial() {
		return this.tutorial;
	}
	public boolean isAutoAdvanceDesired() {
		return true;
	}

	private static int getXForWestLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x -= 200;
		x -= noteBounds.width;
		return x;
	}
	private static int getXForEastLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int x = featureComponentBounds.x;
		x += featureComponentBounds.width;
		x += 200;
		return x;
	}

	private static int getYForNorthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y -= 200;
		y -= noteBounds.height;
		return y;
	}
	private static int getYForSouthLayout( java.awt.Rectangle noteBounds, java.awt.Rectangle featureComponentBounds ) {
		int y = featureComponentBounds.y;
		y += featureComponentBounds.height;
		y += 200;
		return y;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
