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

import javax.swing.AbstractAction;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class JScrollMenuItem extends javax.swing.JMenuItem {
	private static final java.awt.Dimension ARROW_SIZE = new java.awt.Dimension( 10, 10 );

	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		@Override
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

	private static class ScrollAction extends AbstractAction {
		private final ScrollingPopupMenuLayout layout;
		private final ScrollDirection scrollDirection;

		public ScrollAction( ScrollingPopupMenuLayout layout, ScrollDirection scrollDirection ) {
			this.layout = layout;
			this.scrollDirection = scrollDirection;
		}

		public ScrollDirection getScrollDirection() {
			return this.scrollDirection;
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			this.layout.adjustIndex( this.scrollDirection.getDelta() );
		}
	}

	private final ScrollAction scrollAction;
	private final javax.swing.Timer timer;

	private int count;

	public JScrollMenuItem( ScrollingPopupMenuLayout layout, ScrollDirection scrollDirection ) {
		this.scrollAction = new ScrollAction( layout, scrollDirection );
		this.timer = new javax.swing.Timer( 100, this.scrollAction );
	}

	public int getCount() {
		return this.count;
	}

	public void setCount( int count ) {
		if( this.count != count ) {
			this.count = count;
			this.repaint();
		}
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
		java.awt.Font font = this.getFont();
		java.awt.FontMetrics fontMetrics = this.getFontMetrics( font );
		int height = Math.max( fontMetrics.getHeight(), ARROW_SIZE.height );
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), height + 8 );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		javax.swing.ButtonModel model = this.getModel();
		//		if( model.isArmed() ) {
		//			//g.setColor( javax.swing.UIManager.getColor( "textBackground" ) );
		//		} else {
		//			g.setColor( javax.swing.UIManager.getColor( "menu" ) );
		//			g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		//		}
		super.paintComponent( g );
		if( model.isArmed() ) {
			g.setColor( javax.swing.UIManager.getColor( "textHighlightText" ) );
		} else {
			g.setColor( javax.swing.UIManager.getColor( "textForeground" ) );
		}
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading heading = this.scrollAction.getScrollDirection().getArrowHeading();
		int x = ( this.getWidth() - ARROW_SIZE.width ) / 2;
		int y = ( this.getHeight() - ARROW_SIZE.height ) / 2;

		final int SPACE = 10;
		for( int i = -2; i < 3; i++ ) {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g2, heading, x + ( i * ( ARROW_SIZE.width + SPACE ) ), y, ARROW_SIZE.width, ARROW_SIZE.height );
		}
		if( count != 0 ) {
			StringBuilder sb = new StringBuilder();
			sb.append( "(" );
			sb.append( this.count );
			sb.append( ")" );
			g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			g2.drawString( sb.toString(), x + ( 3 * ( ARROW_SIZE.width + SPACE ) ), ( y + ARROW_SIZE.height ) - 2 );
		}
	}
}
