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
public class JSideBySideScrollPane extends javax.swing.JPanel {
	//todo: handle right to left
	private static class SideBySideLayout implements java.awt.LayoutManager {
		public void layoutContainer( java.awt.Container parent ) {
			JSideBySideScrollPane jSideBySideScrollPane = (JSideBySideScrollPane)parent;

			java.awt.Dimension parentSize = jSideBySideScrollPane.getSize();

			java.awt.Dimension separatorPreferredSize = jSideBySideScrollPane.divider.getPreferredSize();
			java.awt.Dimension lineEndScrollBarPreferredSize = jSideBySideScrollPane.verticalScrollBar.getPreferredSize();
			java.awt.Dimension pageEndScrollBarPreferredSize = jSideBySideScrollPane.horizontalScrollBar.getPreferredSize();

			int w = parentSize.width - lineEndScrollBarPreferredSize.width - separatorPreferredSize.width;
			int h = parentSize.height - pageEndScrollBarPreferredSize.height;

			int leadingWidth = w / 2;
			int trailingWidth = w - leadingWidth;

			int x = 0;
			jSideBySideScrollPane.leadingViewport.setBounds( x, 0, leadingWidth, h );
			x += jSideBySideScrollPane.leadingViewport.getWidth();

			jSideBySideScrollPane.divider.setBounds( x, 0, separatorPreferredSize.width, h );
			x += jSideBySideScrollPane.divider.getWidth();

			jSideBySideScrollPane.trailingViewport.setBounds( x, 0, trailingWidth, h );
			x += jSideBySideScrollPane.trailingViewport.getWidth();

			jSideBySideScrollPane.verticalScrollBar.setBounds( x, 0, parentSize.width - x, h );
			jSideBySideScrollPane.horizontalScrollBar.setBounds( 0, h, x, pageEndScrollBarPreferredSize.height );
		}

		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return new java.awt.Dimension( 0, 0 );
		}

		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			JSideBySideScrollPane jSideBySideScrollPane = (JSideBySideScrollPane)parent;
			java.awt.Dimension leadingViewportPreferredSize = jSideBySideScrollPane.leadingViewport.getPreferredSize();
			java.awt.Dimension trailingViewportPreferredSize = jSideBySideScrollPane.trailingViewport.getPreferredSize();
			java.awt.Dimension pageEndScrollBarPreferredSize = jSideBySideScrollPane.horizontalScrollBar.getPreferredSize();
			java.awt.Dimension lineEndScrollBarPreferredSize = jSideBySideScrollPane.verticalScrollBar.getPreferredSize();

			return new java.awt.Dimension(
					leadingViewportPreferredSize.width + trailingViewportPreferredSize.width + lineEndScrollBarPreferredSize.width,
					Math.max( leadingViewportPreferredSize.height, trailingViewportPreferredSize.height ) + pageEndScrollBarPreferredSize.height );
		}

		public void addLayoutComponent( String name, java.awt.Component comp ) {
		}

		public void removeLayoutComponent( java.awt.Component comp ) {
		}
	}

	public JSideBySideScrollPane( java.awt.Component leadingView, java.awt.Component trailingView ) {
		this.leadingViewport.setView( leadingView );
		this.trailingViewport.setView( trailingView );
		this.add( this.leadingViewport );
		this.add( this.divider );
		this.add( this.trailingViewport );
		this.add( this.verticalScrollBar );
		this.add( this.horizontalScrollBar );
		this.setLayout( new SideBySideLayout() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			this.leadingViewport.setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
			this.trailingViewport.setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.leadingViewport.addChangeListener( this.viewportListener );
		this.trailingViewport.addChangeListener( this.viewportListener );
		this.verticalScrollBar.getModel().addChangeListener( this.verticalScrollListener );
		this.horizontalScrollBar.getModel().addChangeListener( this.horizontalScrollListener );
	}

	@Override
	public void removeNotify() {
		this.horizontalScrollBar.getModel().removeChangeListener( this.horizontalScrollListener );
		this.verticalScrollBar.getModel().removeChangeListener( this.verticalScrollListener );
		this.trailingViewport.removeChangeListener( this.viewportListener );
		this.leadingViewport.removeChangeListener( this.viewportListener );
		super.removeNotify();
	}

	private static void updateViewX( javax.swing.JViewport viewport, int value ) {
		java.awt.Point viewPosition = viewport.getViewPosition();
		viewPosition.x = value;
		viewport.setViewPosition( viewPosition );
	}

	private static void updateViewY( javax.swing.JViewport viewport, int value ) {
		java.awt.Point viewPosition = viewport.getViewPosition();
		viewPosition.y = value;
		viewport.setViewPosition( viewPosition );
	}

	private static class JSideBySideDivider extends javax.swing.JComponent implements javax.swing.plaf.UIResource {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 8 );
		}
	}

	private class JSideBySideScrollBar extends javax.swing.JScrollBar implements javax.swing.plaf.UIResource {
		public JSideBySideScrollBar( int orientation ) {
			super( orientation );
		}

		@Override
		public void updateUI() {
			this.setUI( edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.createUI() );
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: omit paint when appropriate" );
			if( ScrollBarPaintUtilities.isPaintRequiredFor( this ) ) {
				super.paintComponent( g );
			} else {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.Shape clip = g.getClip();
				java.awt.Component component = trailingViewport.getView();
				if( component != null ) {
					//pass
				} else {
					component = leadingViewport.getView();
					if( component != null ) {
						//pass
					} else {
						component = JSideBySideScrollPane.this;
					}
				}
				g2.setPaint( component.getBackground() );
				g2.fill( clip );
			}
		}
	}

	private final javax.swing.JViewport leadingViewport = new javax.swing.JViewport();
	private final JSideBySideDivider divider = new JSideBySideDivider();
	private final javax.swing.JViewport trailingViewport = new javax.swing.JViewport();

	private final JSideBySideScrollBar verticalScrollBar = new JSideBySideScrollBar( javax.swing.JScrollBar.VERTICAL );
	private final JSideBySideScrollBar horizontalScrollBar = new JSideBySideScrollBar( javax.swing.JScrollBar.HORIZONTAL );

	private final javax.swing.event.ChangeListener viewportListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
		}
	};

	private final javax.swing.event.ChangeListener verticalScrollListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			javax.swing.BoundedRangeModel model = (javax.swing.BoundedRangeModel)( e.getSource() );
			int leadingY = model.getValue(); //todo
			int trailingY = model.getValue(); //todo
			updateViewY( leadingViewport, leadingY );
			updateViewY( trailingViewport, trailingY );
		}
	};
	private final javax.swing.event.ChangeListener horizontalScrollListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			javax.swing.BoundedRangeModel model = (javax.swing.BoundedRangeModel)( e.getSource() );
			int leadingX = model.getValue(); //todo
			int trailingX = model.getValue(); //todo
			updateViewX( leadingViewport, leadingX );
			updateViewX( trailingViewport, trailingX );
		}
	};

	public static void main( String[] args ) {
		class OvalPanel extends javax.swing.JPanel {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				java.awt.Dimension size = this.getPreferredSize();
				g.fillOval( 0, 0, size.width, size.height );
			}
		}
		javax.swing.JPanel leadingView = new OvalPanel();
		leadingView.setPreferredSize( new java.awt.Dimension( 1000, 600 ) );

		javax.swing.JPanel trailingView = new OvalPanel();
		trailingView.setPreferredSize( new java.awt.Dimension( 600, 1000 ) );

		leadingView.setBackground( java.awt.Color.RED );
		trailingView.setBackground( java.awt.Color.WHITE );

		JSideBySideScrollPane jSideBySideScrollPane = new JSideBySideScrollPane( leadingView, trailingView );
		jSideBySideScrollPane.setBackground( java.awt.Color.GREEN );

		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( jSideBySideScrollPane );
		frame.setSize( 400, 300 );
		//frame.pack();
		frame.setVisible( true );
	}
}
