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
public class JSideBySideScrollPane extends javax.swing.JPanel {
	//todo: handle right to left
	private static class SideBySideLayout implements java.awt.LayoutManager {
		public void setDividerLocationBasedOnMouseEvent( java.awt.event.MouseEvent e ) {

		}

		@Override
		public void layoutContainer( java.awt.Container parent ) {
			JSideBySideScrollPane jSideBySideScrollPane = (JSideBySideScrollPane)parent;

			java.awt.Insets insets = jSideBySideScrollPane.getInsets();

			java.awt.Dimension parentSize = jSideBySideScrollPane.getSize();

			java.awt.Dimension separatorPreferredSize = jSideBySideScrollPane.divider.getPreferredSize();
			java.awt.Dimension lineEndScrollBarPreferredSize = jSideBySideScrollPane.verticalScrollBar.getPreferredSize();
			java.awt.Dimension pageEndScrollBarPreferredSize = jSideBySideScrollPane.horizontalScrollBar.getPreferredSize();

			int w = parentSize.width - lineEndScrollBarPreferredSize.width - separatorPreferredSize.width - insets.left - insets.right;
			int h = parentSize.height - pageEndScrollBarPreferredSize.height - insets.top - insets.bottom;

			int leadingWidth = (int)( w * this.leadingPortion );
			int trailingWidth = w - leadingWidth;

			int x = insets.left;
			int y = insets.top;
			jSideBySideScrollPane.leadingViewport.setBounds( x, y, leadingWidth, h );
			x += jSideBySideScrollPane.leadingViewport.getWidth();

			jSideBySideScrollPane.divider.setBounds( x, y, separatorPreferredSize.width, h );
			x += jSideBySideScrollPane.divider.getWidth();

			jSideBySideScrollPane.trailingViewport.setBounds( x, y, trailingWidth, h );
			x += jSideBySideScrollPane.trailingViewport.getWidth();

			jSideBySideScrollPane.verticalScrollBar.setBounds( x, y, parentSize.width - x, h );

			y += h;
			jSideBySideScrollPane.horizontalScrollBar.setBounds( insets.left, y, w, pageEndScrollBarPreferredSize.height );
		}

		@Override
		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return new java.awt.Dimension( 0, 0 );
		}

		@Override
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

		@Override
		public void addLayoutComponent( String name, java.awt.Component comp ) {
		}

		@Override
		public void removeLayoutComponent( java.awt.Component comp ) {
		}

		private float leadingPortion = 0.5f;
	}

	public JSideBySideScrollPane() {
		this.add( this.leadingViewport );
		this.add( this.divider );
		this.add( this.trailingViewport );
		this.add( this.verticalScrollBar );
		this.add( this.horizontalScrollBar );

		this.horizontalScrollBar.setUnitIncrement( 12 );
		this.verticalScrollBar.setUnitIncrement( 12 );
		this.horizontalScrollBar.setBlockIncrement( 24 );
		this.verticalScrollBar.setBlockIncrement( 24 );

		this.setLayout( new SideBySideLayout() );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			this.leadingViewport.setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
			this.trailingViewport.setScrollMode( javax.swing.JViewport.SIMPLE_SCROLL_MODE );
		}
		int inset = edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.INSET;
		this.verticalScrollBar.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, inset, inset, inset ) );
		this.horizontalScrollBar.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, inset, inset, inset ) );
		this.divider.setBackground( java.awt.Color.DARK_GRAY );
	}

	public JSideBySideScrollPane( java.awt.Component leadingView, java.awt.Component trailingView ) {
		this();
		this.setLeadingView( leadingView );
		this.setTrailingView( trailingView );
	}

	public java.awt.Component getLeadingView() {
		return this.leadingViewport.getView();
	}

	public void setLeadingView( java.awt.Component leadingView ) {
		this.leadingViewport.setView( leadingView );
	}

	public java.awt.Component getTrailingView() {
		return this.trailingViewport.getView();
	}

	public void setTrailingView( java.awt.Component trailingView ) {
		this.trailingViewport.setView( trailingView );
	}

	public javax.swing.JScrollBar getVerticalScrollBar() {
		return this.verticalScrollBar;
	}

	public javax.swing.JScrollBar getHorizontalScrollBar() {
		return this.horizontalScrollBar;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.leadingViewport.addChangeListener( this.viewportListener );
		this.trailingViewport.addChangeListener( this.viewportListener );
		this.verticalScrollBar.getModel().addChangeListener( this.verticalScrollListener );
		this.horizontalScrollBar.getModel().addChangeListener( this.horizontalScrollListener );
		this.addMouseWheelListener( this.mouseWheelListener );
	}

	@Override
	public void removeNotify() {
		this.removeMouseWheelListener( this.mouseWheelListener );
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

	private void updateScrollBars() {
		java.awt.Dimension leadingExtentSize = this.leadingViewport.getExtentSize();
		java.awt.Dimension leadingViewSize = this.leadingViewport.getViewSize();
		java.awt.Dimension trailingExtentSize = this.trailingViewport.getExtentSize();
		java.awt.Dimension trailingViewSize = this.trailingViewport.getViewSize();

		int extentHorizontal;
		int maxHorizontal;

		if( ( leadingViewSize.width != 0 ) && ( trailingViewSize.width != 0 ) ) {
			float leading = leadingExtentSize.width / (float)leadingViewSize.width;
			float trailing = trailingExtentSize.width / (float)trailingViewSize.width;
			if( leading < trailing ) {
				extentHorizontal = leadingExtentSize.width;
				maxHorizontal = leadingViewSize.width;
			} else {
				extentHorizontal = trailingExtentSize.width;
				maxHorizontal = trailingViewSize.width;
			}
		} else {
			//todo?
			extentHorizontal = leadingExtentSize.width;
			maxHorizontal = leadingViewSize.width;
		}

		int extentVertical;
		int maxVertical;
		if( leadingViewSize.height > trailingViewSize.height ) {
			extentVertical = leadingExtentSize.height;
			maxVertical = leadingViewSize.height;
		} else {
			extentVertical = trailingExtentSize.height;
			maxVertical = trailingViewSize.height;
		}

		int horizontalValue = this.horizontalScrollBar.getValue();
		int verticalValue = this.verticalScrollBar.getValue();
		this.horizontalScrollBar.getModel().setRangeProperties( horizontalValue, extentHorizontal, 0, maxHorizontal, false );
		this.verticalScrollBar.getModel().setRangeProperties( verticalValue, extentVertical, 0, maxVertical, false );
	}

	private void handleMouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
		int rotation = e.getWheelRotation();

		javax.swing.BoundedRangeModel verticalModel = this.verticalScrollBar.getModel();
		javax.swing.BoundedRangeModel horizontalModel = this.horizontalScrollBar.getModel();

		boolean isVertical;
		if( rotation > 0 ) {
			isVertical = ( verticalModel.getValue() + verticalModel.getExtent() ) < verticalModel.getMaximum();
		} else {
			isVertical = verticalModel.getValue() > verticalModel.getMinimum();
			if( horizontalModel.getValue() > horizontalModel.getMinimum() ) {
				if( verticalModel.getValue() == ( verticalModel.getMaximum() - verticalModel.getExtent() ) ) {
					isVertical = false;
				}
			}
		}

		javax.swing.JScrollBar scrollBar;
		if( isVertical ) {
			scrollBar = this.verticalScrollBar;
		} else {
			scrollBar = this.horizontalScrollBar;
		}
		int units = scrollBar.getUnitIncrement();
		javax.swing.BoundedRangeModel model = scrollBar.getModel();
		int value;
		if( rotation > 0 ) {
			value = Math.min( model.getValue() + ( rotation * units ), model.getMaximum() );
		} else {
			value = Math.max( model.getValue() + ( rotation * units ), model.getMinimum() );
		}
		model.setValue( value );
	}

	private class JSideBySideDivider extends javax.swing.JComponent implements javax.swing.plaf.UIResource {
		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseListener );
			this.addMouseMotionListener( this.mouseMotionListener );
		}

		@Override
		public void removeNotify() {
			this.removeMouseMotionListener( this.mouseMotionListener );
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 4 );
		}

		private void handleMouseDragged( java.awt.event.MouseEvent e ) {
			java.awt.Point p = javax.swing.SwingUtilities.convertPoint( e.getComponent(), e.getPoint(), JSideBySideScrollPane.this );

			SideBySideLayout sideBySideLayout = (SideBySideLayout)JSideBySideScrollPane.this.getLayout();
			sideBySideLayout.leadingPortion = p.x / (float)JSideBySideScrollPane.this.getWidth();
			JSideBySideScrollPane.this.revalidate();
		}

		private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			@Override
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				edu.cmu.cs.dennisc.java.awt.CursorUtilities.pushAndSetPredefinedCursor( e.getComponent(), java.awt.Cursor.E_RESIZE_CURSOR );
			}

			@Override
			public void mouseExited( java.awt.event.MouseEvent e ) {
				edu.cmu.cs.dennisc.java.awt.CursorUtilities.popAndSet( e.getComponent() );
			}

			@Override
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		};

		private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
			@Override
			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}

			@Override
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				handleMouseDragged( e );
			}
		};
	}

	private class JSideBySideScrollBar extends javax.swing.JScrollBar implements javax.swing.plaf.UIResource {
		public JSideBySideScrollBar( int orientation ) {
			super( orientation );
		}

		@Override
		public java.awt.Color getBackground() {
			return JSideBySideScrollPane.this.getBackground();
		}

		@Override
		public void updateUI() {
			this.setUI( edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI.createUI() );
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			if( ScrollBarPaintUtilities.isPaintRequiredFor( this ) ) {
				super.paintComponent( g );
			} else {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.Shape clip = g.getClip();
				//				java.awt.Component component = trailingViewport.getView();
				//				if( component != null ) {
				//					//pass
				//				} else {
				//					component = leadingViewport.getView();
				//					if( component != null ) {
				//						//pass
				//					} else {
				//						component = JSideBySideScrollPane.this;
				//					}
				//				}
				//				g2.setPaint( component.getBackground() );
				g2.setPaint( this.getBackground() );
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
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			updateScrollBars();
		}
	};

	private final javax.swing.event.ChangeListener verticalScrollListener = new javax.swing.event.ChangeListener() {
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			javax.swing.BoundedRangeModel model = (javax.swing.BoundedRangeModel)( e.getSource() );
			int leadingY = model.getValue(); //todo
			int trailingY = model.getValue(); //todo
			updateViewY( leadingViewport, leadingY );
			updateViewY( trailingViewport, trailingY );
		}
	};
	private final javax.swing.event.ChangeListener horizontalScrollListener = new javax.swing.event.ChangeListener() {
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			javax.swing.BoundedRangeModel model = (javax.swing.BoundedRangeModel)( e.getSource() );
			int leadingX = model.getValue(); //todo
			int trailingX = model.getValue(); //todo
			updateViewX( leadingViewport, leadingX );
			updateViewX( trailingViewport, trailingX );
		}
	};

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		@Override
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
			handleMouseWheelMoved( e );
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
