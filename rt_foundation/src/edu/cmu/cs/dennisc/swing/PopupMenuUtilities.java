/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class PopupMenuUtilities {
	public static void showModal( javax.swing.JPopupMenu popupMenu, java.awt.Component invoker, int x, int y ) {
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( invoker );
		final javax.swing.JLayeredPane layeredPane;
		if( root instanceof javax.swing.JFrame ) {
			javax.swing.JFrame window = (javax.swing.JFrame)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof javax.swing.JDialog ) {
			javax.swing.JDialog window = (javax.swing.JDialog)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof javax.swing.JWindow ) {
			javax.swing.JWindow window = (javax.swing.JWindow)root;
			layeredPane = window.getLayeredPane();
		} else {
			layeredPane = null;
		}
		if( layeredPane != null ) {
			class EventConsumer extends javax.swing.JComponent {
				private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
					public void mouseEntered( java.awt.event.MouseEvent e ) {
					}
					public void mouseExited( java.awt.event.MouseEvent e ) {
					}
					public void mousePressed( java.awt.event.MouseEvent e ) {
					}
					public void mouseReleased( java.awt.event.MouseEvent e ) {
						EventConsumer.this.removeFromParentJustInCaseAnExceptionWasThrownSomewhere();
					}
					public void mouseClicked( java.awt.event.MouseEvent e ) {
					}
				};
				private void removeFromParentJustInCaseAnExceptionWasThrownSomewhere() {
					java.awt.Container parent = this.getParent();
					if( parent != null ) {
						parent.remove( this );
					}
				}
				@Override
				public void addNotify() {
					super.addNotify();
					this.setLocation( 0, 0 );
					java.awt.Component parent = this.getParent();
					if( parent != null ) {
						this.setSize( parent.getSize() );
					}
					this.addMouseListener( this.mouseAdapter );
				}
				@Override
				public void removeNotify() {
					this.removeMouseListener( this.mouseAdapter );
					super.removeNotify();
				}

			}
			final EventConsumer eventConsumer = new EventConsumer();
			popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
				public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
					layeredPane.add( eventConsumer, new Integer( javax.swing.JLayeredPane.MODAL_LAYER ) );
				}
				public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
					layeredPane.remove( eventConsumer );
				}
				public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				}
			} );
		}
		popupMenu.show( invoker, x, y );
	}
}
