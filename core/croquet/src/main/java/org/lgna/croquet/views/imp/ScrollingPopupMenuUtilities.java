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
package org.lgna.croquet.views.imp;

/**
 * @author Dennis Cosgrove
 */
public class ScrollingPopupMenuUtilities {
	private ScrollingPopupMenuUtilities() {
		throw new AssertionError();
	}

	private static final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		@Override
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
			java.awt.Component component = e.getComponent();
			if( component instanceof javax.swing.JPopupMenu ) {
				javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)component;
				java.awt.LayoutManager layoutManager = jPopupMenu.getLayout();
				if( layoutManager instanceof ScrollingPopupMenuLayout ) {
					ScrollingPopupMenuLayout scrollingPopupMenuLayout = (ScrollingPopupMenuLayout)layoutManager;
					scrollingPopupMenuLayout.adjustIndex( e.getWheelRotation() );
				}
			}
			e.consume();
		}
	};

	public static void initializeScrollingCapability( javax.swing.JPopupMenu jPopupMenu ) {
		ScrollingPopupMenuLayout layout = new ScrollingPopupMenuLayout( jPopupMenu );
		jPopupMenu.setLayout( layout );
		jPopupMenu.addMouseWheelListener( mouseWheelListener );
		jPopupMenu.add( new JScrollMenuItem( layout, ScrollDirection.UP ), ScrollingPopupMenuLayout.ScrollConstraint.PAGE_START );
		jPopupMenu.add( new JScrollMenuItem( layout, ScrollDirection.DOWN ), ScrollingPopupMenuLayout.ScrollConstraint.PAGE_END );
	}

	public static void addSideMenu( javax.swing.JPopupMenu jPopupMenu, javax.swing.JMenu jSideMenu ) {
		jPopupMenu.add( jSideMenu, ScrollingPopupMenuLayout.ColumnConstraint.SIDE );
	}

	public static void removeAllNonScrollComponents( javax.swing.JPopupMenu jPopupMenu ) {
		for( java.awt.Component component : jPopupMenu.getComponents() ) {
			if( component instanceof JScrollMenuItem ) {
				//pass
			} else {
				jPopupMenu.remove( component );
			}
		}
	}
}
