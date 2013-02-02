/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.croquet.components.impl;

/**
 * @author Dennis Cosgrove
 */
public class ScrollingPopupMenuImpl {
	private final java.util.List<org.lgna.croquet.components.JComponent<?>> menuItems = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final javax.swing.JPopupMenu jPopupMenu;

	private int index0;

	private static final java.awt.Dimension ARROW_SIZE = new java.awt.Dimension( 10, 10 );

	private final class JScrollMenuItem extends javax.swing.JMenuItem {
		private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				javax.swing.ButtonModel buttonModel = getModel();
				if( buttonModel.isArmed() ) {
					if( timer.isRunning() ) {
						//pass
					} else {
						timer.start();
					}
				} else {
					if( timer.isRunning() ) {
						timer.stop();
					} else {
						//pass
					}
				}
			}
		};

		private final javax.swing.Timer timer;
		private final int increment;

		public JScrollMenuItem( int increment ) {
			this.increment = increment;
			this.timer = new javax.swing.Timer( 25, new javax.swing.AbstractAction() {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					index0 += JScrollMenuItem.this.increment;
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( index0 );
				}
			} );
		}

		@Override
		protected void processMouseEvent( java.awt.event.MouseEvent e ) {
			int id = e.getID();
			if( ( id == java.awt.event.MouseEvent.MOUSE_PRESSED ) || ( id == java.awt.event.MouseEvent.MOUSE_RELEASED ) ) {
				//pass
			} else {
				super.processMouseEvent( e );
			}
		}

		@Override
		public void addNotify() {
			this.addChangeListener( this.changeListener );
			super.addNotify();
		}

		@Override
		public void removeNotify() {
			super.removeNotify();
			this.removeChangeListener( this.changeListener );
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), ARROW_SIZE.height + 4 );
		}

		@Override
		public void paint( java.awt.Graphics g ) {
			super.paint( g );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			javax.swing.ButtonModel model = this.getModel();
			java.awt.Paint paint;
			if( model.isEnabled() ) {
				if( model.isArmed() ) {
					paint = java.awt.Color.WHITE;
				} else {
					paint = java.awt.Color.DARK_GRAY;
				}
			} else {
				paint = java.awt.Color.GRAY;
			}
			g2.setPaint( paint );
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading heading = increment == 1 ? edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.SOUTH : edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.NORTH;
			int x = ( this.getWidth() - ARROW_SIZE.width ) / 2;
			int y = ( this.getHeight() - ARROW_SIZE.height ) / 2;
			for( int i = -1; i < 2; i++ ) {
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g2, heading, x + ( i * ( ARROW_SIZE.width + 8 ) ), y, ARROW_SIZE.width, ARROW_SIZE.height );
			}
		}
	}

	private final javax.swing.JMenuItem jUpMenuItem = new JScrollMenuItem( -1 );
	private final javax.swing.JMenuItem jDownMenuItem = new JScrollMenuItem( +1 );

	private int getNonSeparatorCount() {
		int rv = 0;
		for( org.lgna.croquet.components.JComponent<?> menuItem : menuItems ) {
			if( menuItem != null ) {
				rv++;
			}
		}
		return rv;
	}

	private final javax.swing.event.PopupMenuListener popupMenuListener = new javax.swing.event.PopupMenuListener() {
		public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
			javax.swing.JPopupMenu menu = (javax.swing.JPopupMenu)e.getSource();
			if( menu.getLayout() instanceof javax.swing.SpringLayout ) {
				//pass
			} else {
				final int MAX_NON_SEPARATOR_COUNT = 4;
				int nonSeparatorCount = getNonSeparatorCount();
				if( nonSeparatorCount > MAX_NON_SEPARATOR_COUNT ) {
					menu.removeAll();
					menu.add( jUpMenuItem );
					int i = 0;
					for( org.lgna.croquet.components.JComponent<?> menuItem : menuItems ) {
						if( menuItem != null ) {
							menu.add( menuItem.getAwtComponent() );
							i++;
						} else {
							menu.addSeparator();
						}
						if( i == MAX_NON_SEPARATOR_COUNT ) {
							break;
						}
					}
					menu.add( jDownMenuItem );
					menu.revalidate();
					menu.repaint();
				}
			}
		}

		public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
			//			javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)e.getSource();
			//			popupMenu.removeAll();
		}

		public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
			//			javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)e.getSource();
			//			popupMenu.removeAll();
		}
	};

	public ScrollingPopupMenuImpl( javax.swing.JPopupMenu jPopupMenu ) {
		this.jPopupMenu = jPopupMenu;
		//this.jPopupMenu.addPopupMenuListener( this.popupMenuListener );
		//todo: dispose()
	}

	public ScrollingPopupMenuImpl( javax.swing.JMenu jMenu ) {
		this( jMenu.getPopupMenu() );
	}

	public void addMenu( org.lgna.croquet.components.Menu menu ) {
		this.menuItems.add( menu );
		jPopupMenu.add( menu.getAwtComponent() );
	}

	public void addMenuItem( org.lgna.croquet.components.MenuItem menuItem ) {
		this.menuItems.add( menuItem );
		this.jPopupMenu.add( menuItem.getAwtComponent() );
	}

	public void addCascadeMenu( org.lgna.croquet.components.CascadeMenu cascadeMenu ) {
		this.menuItems.add( cascadeMenu );
		this.jPopupMenu.add( cascadeMenu.getAwtComponent() );
	}

	public void addCascadeMenuItem( org.lgna.croquet.components.CascadeMenuItem cascadeMenuItem ) {
		this.menuItems.add( cascadeMenuItem );
		this.jPopupMenu.add( cascadeMenuItem.getAwtComponent() );
	}

	public void addCheckBoxMenuItem( org.lgna.croquet.components.CheckBoxMenuItem checkBoxMenuItem ) {
		this.menuItems.add( checkBoxMenuItem );
		this.jPopupMenu.add( checkBoxMenuItem.getAwtComponent() );
	}

	public void addSeparator( org.lgna.croquet.components.MenuTextSeparator menuTextSeparator ) {
		this.menuItems.add( menuTextSeparator );
		if( menuTextSeparator != null ) {
			this.jPopupMenu.add( menuTextSeparator.getAwtComponent() );
		} else {
			this.jPopupMenu.addSeparator();
		}
	}

	public void removeAllMenuItems() {
		this.menuItems.clear();
		//this.internalRemoveAllComponents();
		this.jPopupMenu.removeAll();
	}

	public void forgetAndRemoveAllMenuItems() {
		this.menuItems.clear();
		//this.internalForgetAndRemoveAllComponents();
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "forget" );
		this.jPopupMenu.removeAll();
	}
}
