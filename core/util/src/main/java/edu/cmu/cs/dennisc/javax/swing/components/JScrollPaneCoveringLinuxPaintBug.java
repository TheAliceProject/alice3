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
package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
public class JScrollPaneCoveringLinuxPaintBug extends javax.swing.JScrollPane {

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
		int inset = edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.INSET;
		javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder( inset, inset, inset, inset );
		javax.swing.JScrollBar verticalScrollBar = this.getVerticalScrollBar();
		verticalScrollBar.setUI( edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.createUI() );
		verticalScrollBar.setBorder( border );

		javax.swing.JScrollBar horizontalScrollBar = this.getHorizontalScrollBar();
		horizontalScrollBar.setUI( edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.createUI() );
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
