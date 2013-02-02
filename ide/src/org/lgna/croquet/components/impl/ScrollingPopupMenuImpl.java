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

	private final javax.swing.JMenuItem jUpMenuItem = new JScrollMenuItem( this, ScrollDirection.UP );
	private final javax.swing.JMenuItem jDownMenuItem = new JScrollMenuItem( this, ScrollDirection.DOWN );

	private int index0;

	private int getNonSeparatorCount() {
		int rv = 0;
		for( org.lgna.croquet.components.JComponent<?> menuItem : menuItems ) {
			if( menuItem != null ) {
				rv++;
			}
		}
		return rv;
	}

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
			adjustIndex( e.getWheelRotation() );
			e.consume();
		}
	};

	//	private static class ScrollingPopupMenuLayout implements java.awt.LayoutManager2 {
	//		private javax.swing.SizeRequirements[] childWidthRequirements;
	//		private javax.swing.SizeRequirements[] childHeightRequirements;
	//		private javax.swing.SizeRequirements widthRequirement;
	//		private javax.swing.SizeRequirements heightRequirement;
	//
	//		public float getLayoutAlignmentX( java.awt.Container target ) {
	//			return 0.0f;
	//		}
	//
	//		public float getLayoutAlignmentY( java.awt.Container target ) {
	//			return 0.0f;
	//		}
	//
	//		public void addLayoutComponent( String name, java.awt.Component comp ) {
	//		}
	//
	//		public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
	//		}
	//
	//		public void removeLayoutComponent( java.awt.Component comp ) {
	//		}
	//
	//		public synchronized void invalidateLayout( java.awt.Container target ) {
	//			this.childWidthRequirements = null;
	//			this.childHeightRequirements = null;
	//			this.widthRequirement = null;
	//			this.heightRequirement = null;
	//		}
	//
	//		private void updateRequirementsIfNecessary( java.awt.Container target ) {
	//			if( ( this.childWidthRequirements != null ) && ( this.childHeightRequirements != null ) ) {
	//				//pass
	//			} else {
	//				final int N = target.getComponentCount();
	//				this.childWidthRequirements = new javax.swing.SizeRequirements[ N ];
	//				this.childHeightRequirements = new javax.swing.SizeRequirements[ N ];
	//				for( int i = 0; i < N; i++ ) {
	//					java.awt.Component componentI = target.getComponent( i );
	//					if( componentI.isVisible() ) {
	//
	//					} else {
	//
	//					}
	//				}
	//			}
	//		}
	//
	//		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
	//			return new java.awt.Dimension( 0, 0 );
	//		}
	//
	//		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
	//			return new java.awt.Dimension( 100, 100 );
	//		}
	//
	//		public java.awt.Dimension maximumLayoutSize( java.awt.Container target ) {
	//			return this.preferredLayoutSize( target );
	//		}
	//
	//		public void layoutContainer( java.awt.Container parent ) {
	//		}
	//	}

	//	private final javax.swing.event.PopupMenuListener popupMenuListener = new javax.swing.event.PopupMenuListener() {
	//		public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
	//			javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)e.getSource();
	//			if( jPopupMenu.getLayout() instanceof javax.swing.SpringLayout ) {
	//				//pass
	//			} else {
	//				final int MAX_NON_SEPARATOR_COUNT = 5;
	//				int nonSeparatorCount = getNonSeparatorCount();
	//				if( nonSeparatorCount > MAX_NON_SEPARATOR_COUNT ) {
	//					jPopupMenu.removeAll();
	//					jPopupMenu.add( jUpMenuItem );
	//					int i = 0;
	//					for( org.lgna.croquet.components.JComponent<?> menuItem : menuItems ) {
	//						if( menuItem != null ) {
	//							jPopupMenu.add( menuItem.getAwtComponent() );
	//							i++;
	//						} else {
	//							jPopupMenu.addSeparator();
	//						}
	//						if( i == MAX_NON_SEPARATOR_COUNT ) {
	//							break;
	//						}
	//					}
	//					jPopupMenu.add( jDownMenuItem );
	//					jPopupMenu.revalidate();
	//					jPopupMenu.repaint();
	//				}
	//			}
	//		}
	//
	//		public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
	//			//			javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)e.getSource();
	//			//			popupMenu.removeAll();
	//		}
	//
	//		public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
	//			//			javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)e.getSource();
	//			//			popupMenu.removeAll();
	//		}
	//	};

	public ScrollingPopupMenuImpl( javax.swing.JPopupMenu jPopupMenu ) {
		this.jPopupMenu = jPopupMenu;
		//		final boolean IS_SCROLLING_READY_FOR_PRIME_TIME = true;
		//		if( IS_SCROLLING_READY_FOR_PRIME_TIME ) {
		//			this.jPopupMenu.setLayout( new ScrollingPopupMenuLayout() );
		//			//this.jPopupMenu.addPopupMenuListener( this.popupMenuListener );
		//			this.jPopupMenu.addMouseWheelListener( this.mouseWheelListener );
		//			//todo: dispose()
		//		}
	}

	public ScrollingPopupMenuImpl( javax.swing.JMenu jMenu ) {
		this( jMenu.getPopupMenu() );
	}

	private void adjustIndex( int delta ) {
		this.index0 += delta;
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( delta );
	}

	public void scroll( ScrollDirection scrollDirection ) {
		this.adjustIndex( scrollDirection.getDelta() );
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
