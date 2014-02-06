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
	private static int INSET = 2;

	private static class SmallerFootprintScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
		public static javax.swing.plaf.ScrollBarUI createUI() {
			return new SmallerFootprintScrollBarUI();
		}

		@Override
		protected void installDefaults() {
			super.installDefaults();
			this.thumbRolloverColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 100 );
			this.thumbPressedColor = new java.awt.Color( 100, 140, 255 );
		}

		@Override
		protected void paintThumb( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds ) {
			//super.paintThumb( g, c, thumbBounds );
			if( c instanceof javax.swing.JScrollBar ) {
				javax.swing.JScrollBar jScrollBar = (javax.swing.JScrollBar)c;
				int span = jScrollBar.getOrientation() == javax.swing.JScrollBar.VERTICAL ? c.getWidth() : c.getHeight();
				int arc = span - INSET - INSET;
				java.awt.Shape shape = new java.awt.geom.RoundRectangle2D.Float( thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, arc, arc );
				java.awt.Paint paint;
				if( this.isDragging ) {
					paint = this.thumbPressedColor;
				} else {
					paint = isThumbRollover() ? this.thumbRolloverColor : this.thumbColor;
				}
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				g2.setPaint( paint );
				g2.fill( shape );
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, prevAntialiasing );
			}
		}

		@Override
		protected void paintTrack( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds ) {
			//super.paintTrack( g, c, trackBounds );
		}

		private static javax.swing.JButton create0SizeButton() {
			javax.swing.JButton rv = new javax.swing.JButton();
			java.awt.Dimension size = new java.awt.Dimension( 0, 0 );
			rv.setMinimumSize( size );
			rv.setPreferredSize( size );
			rv.setMaximumSize( size );
			return rv;
		}

		@Override
		protected javax.swing.JButton createIncreaseButton( int orientation ) {
			return create0SizeButton();
		}

		@Override
		protected javax.swing.JButton createDecreaseButton( int orientation ) {
			return create0SizeButton();
		}

		private java.awt.Color thumbPressedColor;
		private java.awt.Color thumbRolloverColor;
	}

	protected static class JViewBasedBackgroundColorScrollBar extends javax.swing.JScrollBar {
		public JViewBasedBackgroundColorScrollBar( int orientation ) {
			super( orientation );
		}

		@Override
		public java.awt.Color getBackground() {
			javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)this.getParent();
			if( scrollPane != null ) {
				java.awt.Color scrollPaneBackground = scrollPane.getBackground();
				return scrollPaneBackground;
				//todo?			
				//return edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( scrollPaneBackground, 1.0, 0.7, 1.1 );
			}
			return super.getBackground();
		}
	}

	public JScrollPaneCoveringLinuxPaintBug( java.awt.Component view ) {
		super( view );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			this.getViewport().setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
		}
		javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET );
		javax.swing.JScrollBar verticalScrollBar = this.getVerticalScrollBar();
		verticalScrollBar.setUI( SmallerFootprintScrollBarUI.createUI() );
		verticalScrollBar.setBorder( border );

		javax.swing.JScrollBar horizontalScrollBar = this.getHorizontalScrollBar();
		horizontalScrollBar.setUI( SmallerFootprintScrollBarUI.createUI() );
		horizontalScrollBar.setBorder( border );
	}

	public JScrollPaneCoveringLinuxPaintBug() {
		this( null );
	}

	@Override
	public java.awt.Color getBackground() {
		javax.swing.JViewport viewport = this.getViewport();
		if( viewport != null ) {
			java.awt.Component view = viewport.getView();
			if( view != null ) {
				return view.getBackground();
			}
		}
		return super.getBackground();
	}

	@Override
	public javax.swing.JScrollBar createHorizontalScrollBar() {
		return new JViewBasedBackgroundColorScrollBar( javax.swing.JScrollBar.HORIZONTAL );
	}

	@Override
	public javax.swing.JScrollBar createVerticalScrollBar() {
		return new JViewBasedBackgroundColorScrollBar( javax.swing.JScrollBar.VERTICAL );
	}

	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		javax.swing.UIManager.put( "ScrollBar.width", 11 );
		javax.swing.JPanel view = new javax.swing.JPanel();
		view.setPreferredSize( new java.awt.Dimension( 1000, 400 ) );
		JScrollPaneCoveringLinuxPaintBug scrollPane = new JScrollPaneCoveringLinuxPaintBug( view );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( scrollPane, java.awt.BorderLayout.CENTER );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 320, 240 );
		frame.setVisible( true );
	}
}
