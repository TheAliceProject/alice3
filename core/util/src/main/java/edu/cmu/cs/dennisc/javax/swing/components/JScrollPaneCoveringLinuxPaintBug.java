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
package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
public class JScrollPaneCoveringLinuxPaintBug extends javax.swing.JScrollPane {
	private static class SmallerFootprintScrollBarUI extends javax.swing.plaf.ScrollBarUI {

		private static final int SIZE = 9;
		private static final float CENTER = SIZE * 0.5f;
		private static final java.awt.Stroke STROKE = new java.awt.BasicStroke( SIZE - 2, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER );

		private static final java.awt.Color BASE_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 140 );
		private static final java.awt.Color ROLL_OVER_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 100 );
		private static final java.awt.Color PRESSED_COLOR = new java.awt.Color( 100, 140, 255 );

		public static javax.swing.plaf.ScrollBarUI createUI( javax.swing.JScrollBar scrollBar ) {
			return new SmallerFootprintScrollBarUI( scrollBar );
		}

		private SmallerFootprintScrollBarUI( javax.swing.JScrollBar scrollBar ) {
			this.scrollBar = scrollBar;
		}

		@Override
		public void installUI( javax.swing.JComponent c ) {
			super.installUI( c );
			c.addMouseListener( this.mouseListener );
			c.addMouseMotionListener( this.mouseMotionListener );
			this.scrollBar.getModel().addChangeListener( this.changeListener );
		}

		@Override
		public void uninstallUI( javax.swing.JComponent c ) {
			this.scrollBar.getModel().removeChangeListener( this.changeListener );
			c.removeMouseMotionListener( this.mouseMotionListener );
			c.removeMouseListener( this.mouseListener );
			super.uninstallUI( c );
		}

		@Override
		public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
			super.paint( g, c );
			javax.swing.JScrollBar scrollBar = (javax.swing.JScrollBar)c;
			int orientation = scrollBar.getOrientation();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = new edu.cmu.cs.dennisc.java.awt.GraphicsContext();
			gc.pushAll( g2 );
			gc.pushPaint();
			gc.pushStroke();
			gc.pushAndSetAntialiasing( true );

			java.awt.Paint paint;
			if( isPressed() ) {
				paint = PRESSED_COLOR;
			} else {
				paint = isRollover() ? ROLL_OVER_COLOR : BASE_COLOR;
			}
			g2.setPaint( paint );
			g2.setStroke( STROKE );

			int indent = 4;

			int s = orientation == javax.swing.SwingConstants.VERTICAL ? c.getHeight() : c.getWidth();
			s -= 1;
			s -= indent;
			s -= indent;

			float min = scrollBar.getMinimum();
			float max = scrollBar.getMaximum();
			float extent = scrollBar.getVisibleAmount();
			float range = max - min;

			int value = scrollBar.getValue();
			int position = value; //todo
			int length = (int)( s * ( extent / range ) );

			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			if( orientation == javax.swing.SwingConstants.VERTICAL ) {
				path.moveTo( CENTER, position + indent );
				path.lineTo( CENTER, position + indent + length );
			} else {
				path.moveTo( position + indent, CENTER );
				path.lineTo( position + indent + length, CENTER );
			}
			g2.draw( path );

			gc.popAll();

			//g.setColor( java.awt.Color.RED );
			//g.drawRect( 0, 0, c.getWidth() - 1, c.getHeight() - 1 );
		}

		@Override
		public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
			return new java.awt.Dimension( SIZE, SIZE );
		}

		private boolean isRollover() {
			return this.isRollover;
		}

		private void setRollover( boolean isRollover ) {
			this.isRollover = isRollover;
			this.scrollBar.repaint();

		}

		private boolean isPressed() {
			return this.isPressed;
		}

		private void setPressed( boolean isPressed ) {
			this.isPressed = isPressed;
			this.scrollBar.repaint();

		}

		private final javax.swing.JScrollBar scrollBar;
		private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				setRollover( true );
			}

			public void mouseExited( java.awt.event.MouseEvent e ) {
				setRollover( false );
			}

			public void mousePressed( java.awt.event.MouseEvent e ) {
				setPressed( true );
			}

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				setPressed( false );
			}

			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		};

		private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}

			public void mouseDragged( java.awt.event.MouseEvent e ) {
				//todo:
			}
		};

		private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				scrollBar.repaint();
			}
		};

		private boolean isRollover;
		private boolean isPressed;
	};

	public JScrollPaneCoveringLinuxPaintBug() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			this.getViewport().setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
		}
		final boolean IS_SMALLER_FOOTPRINT_SCROLL_BAR_READY_FOR_PRIME_TIME = false;
		if( IS_SMALLER_FOOTPRINT_SCROLL_BAR_READY_FOR_PRIME_TIME ) {
			final java.awt.Color backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 180 );
			javax.swing.JScrollBar verticalScrollBar = this.getVerticalScrollBar();
			verticalScrollBar.setUI( SmallerFootprintScrollBarUI.createUI( verticalScrollBar ) );
			verticalScrollBar.setBackground( backgroundColor );

			javax.swing.JScrollBar horizontalScrollBar = this.getHorizontalScrollBar();
			horizontalScrollBar.setUI( SmallerFootprintScrollBarUI.createUI( horizontalScrollBar ) );
			horizontalScrollBar.setBackground( backgroundColor );
		}
	}
}
