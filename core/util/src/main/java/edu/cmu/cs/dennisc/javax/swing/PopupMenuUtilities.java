/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.javax.swing;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Dennis Cosgrove
 */
public class PopupMenuUtilities {
	public static void showModal( JPopupMenu popupMenu, Component invoker, Point pt ) {
		Component root = SwingUtilities.getRoot( invoker );
		final JLayeredPane layeredPane;
		if( root instanceof JFrame ) {
			JFrame window = (JFrame)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof JDialog ) {
			JDialog window = (JDialog)root;
			layeredPane = window.getLayeredPane();
		} else if( root instanceof JWindow ) {
			JWindow window = (JWindow)root;
			layeredPane = window.getLayeredPane();
		} else {
			layeredPane = null;
		}
		if( layeredPane != null ) {
			class EventConsumer extends JComponent {
				private MouseListener mouseAdapter = new MouseListener() {
					@Override
					public void mouseEntered( MouseEvent e ) {
					}

					@Override
					public void mouseExited( MouseEvent e ) {
					}

					@Override
					public void mousePressed( MouseEvent e ) {
					}

					@Override
					public void mouseReleased( MouseEvent e ) {
						EventConsumer.this.removeFromParentJustInCaseAnExceptionWasThrownSomewhere();
					}

					@Override
					public void mouseClicked( MouseEvent e ) {
					}
				};

				private void removeFromParentJustInCaseAnExceptionWasThrownSomewhere() {
					Container parent = this.getParent();
					if( parent != null ) {
						parent.remove( this );
					}
				}

				@Override
				public void addNotify() {
					super.addNotify();
					this.setLocation( 0, 0 );
					Component parent = this.getParent();
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
			popupMenu.addPopupMenuListener( new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible( PopupMenuEvent e ) {
					layeredPane.add( eventConsumer, new Integer( JLayeredPane.MODAL_LAYER ) );
				}

				@Override
				public void popupMenuWillBecomeInvisible( PopupMenuEvent e ) {
					layeredPane.remove( eventConsumer );
				}

				@Override
				public void popupMenuCanceled( PopupMenuEvent e ) {
				}
			} );
		}

		int x;
		int y;
		if( pt != null ) {
			x = pt.x;
			y = pt.y;
		} else {
			if( invoker != null ) {
				//todo
				//if( invoker.getComponentOrientation().isLeftToRight() ) {
				x = 0;
				y = invoker.getHeight();
			} else {
				x = 0;
				y = 0;
			}
		}
		popupMenu.show( invoker, x, y );
	}
}
