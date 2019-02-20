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

import edu.cmu.cs.dennisc.java.awt.CursorUtilities;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.javax.swing.plaf.SmallerFootprintScrollBarUI;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author Dennis Cosgrove
 */
public class JSideBySideScrollPane extends JPanel {
	//todo: handle right to left
	private static class SideBySideLayout implements LayoutManager {
		public void setDividerLocationBasedOnMouseEvent( MouseEvent e ) {

		}

		@Override
		public void layoutContainer( Container parent ) {
			JSideBySideScrollPane jSideBySideScrollPane = (JSideBySideScrollPane)parent;

			Insets insets = jSideBySideScrollPane.getInsets();

			Dimension parentSize = jSideBySideScrollPane.getSize();

			Dimension separatorPreferredSize = jSideBySideScrollPane.divider.getPreferredSize();
			Dimension lineEndScrollBarPreferredSize = jSideBySideScrollPane.verticalScrollBar.getPreferredSize();
			Dimension pageEndScrollBarPreferredSize = jSideBySideScrollPane.horizontalScrollBar.getPreferredSize();

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
		public Dimension minimumLayoutSize( Container parent ) {
			return new Dimension( 0, 0 );
		}

		@Override
		public Dimension preferredLayoutSize( Container parent ) {
			JSideBySideScrollPane jSideBySideScrollPane = (JSideBySideScrollPane)parent;
			Dimension leadingViewportPreferredSize = jSideBySideScrollPane.leadingViewport.getPreferredSize();
			Dimension trailingViewportPreferredSize = jSideBySideScrollPane.trailingViewport.getPreferredSize();
			Dimension pageEndScrollBarPreferredSize = jSideBySideScrollPane.horizontalScrollBar.getPreferredSize();
			Dimension lineEndScrollBarPreferredSize = jSideBySideScrollPane.verticalScrollBar.getPreferredSize();

			return new Dimension(
					leadingViewportPreferredSize.width + trailingViewportPreferredSize.width + lineEndScrollBarPreferredSize.width,
					Math.max( leadingViewportPreferredSize.height, trailingViewportPreferredSize.height ) + pageEndScrollBarPreferredSize.height );
		}

		@Override
		public void addLayoutComponent( String name, Component comp ) {
		}

		@Override
		public void removeLayoutComponent( Component comp ) {
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
		if( SystemUtilities.isLinux() ) {
			this.leadingViewport.setScrollMode( JViewport.SIMPLE_SCROLL_MODE );
			this.trailingViewport.setScrollMode( JViewport.SIMPLE_SCROLL_MODE );
		}
		int inset = SmallerFootprintScrollBarUI.INSET;
		this.verticalScrollBar.setBorder( BorderFactory.createEmptyBorder( inset, inset, inset, inset ) );
		this.horizontalScrollBar.setBorder( BorderFactory.createEmptyBorder( inset, inset, inset, inset ) );
		this.divider.setBackground( Color.DARK_GRAY );
	}

	public JSideBySideScrollPane( Component leadingView, Component trailingView ) {
		this();
		this.setLeadingView( leadingView );
		this.setTrailingView( trailingView );
	}

	public Component getLeadingView() {
		return this.leadingViewport.getView();
	}

	public void setLeadingView( Component leadingView ) {
		this.leadingViewport.setView( leadingView );
	}

	public Component getTrailingView() {
		return this.trailingViewport.getView();
	}

	public void setTrailingView( Component trailingView ) {
		this.trailingViewport.setView( trailingView );
	}

	public JScrollBar getVerticalScrollBar() {
		return this.verticalScrollBar;
	}

	public JScrollBar getHorizontalScrollBar() {
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

	private static void updateViewX( JViewport viewport, int value ) {
		Point viewPosition = viewport.getViewPosition();
		viewPosition.x = value;
		viewport.setViewPosition( viewPosition );
	}

	private static void updateViewY( JViewport viewport, int value ) {
		Point viewPosition = viewport.getViewPosition();
		viewPosition.y = value;
		viewport.setViewPosition( viewPosition );
	}

	private void updateScrollBars() {
		Dimension leadingExtentSize = this.leadingViewport.getExtentSize();
		Dimension leadingViewSize = this.leadingViewport.getViewSize();
		Dimension trailingExtentSize = this.trailingViewport.getExtentSize();
		Dimension trailingViewSize = this.trailingViewport.getViewSize();

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

	private void handleMouseWheelMoved( MouseWheelEvent e ) {
		int rotation = e.getWheelRotation();

		BoundedRangeModel verticalModel = this.verticalScrollBar.getModel();
		BoundedRangeModel horizontalModel = this.horizontalScrollBar.getModel();

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

		JScrollBar scrollBar;
		if( isVertical ) {
			scrollBar = this.verticalScrollBar;
		} else {
			scrollBar = this.horizontalScrollBar;
		}
		int units = scrollBar.getUnitIncrement();
		BoundedRangeModel model = scrollBar.getModel();
		int value;
		if( rotation > 0 ) {
			value = Math.min( model.getValue() + ( rotation * units ), model.getMaximum() );
		} else {
			value = Math.max( model.getValue() + ( rotation * units ), model.getMinimum() );
		}
		model.setValue( value );
	}

	private class JSideBySideDivider extends JComponent implements UIResource {
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
		public Dimension getPreferredSize() {
			return DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 4 );
		}

		private void handleMouseDragged( MouseEvent e ) {
			Point p = SwingUtilities.convertPoint( e.getComponent(), e.getPoint(), JSideBySideScrollPane.this );

			SideBySideLayout sideBySideLayout = (SideBySideLayout)JSideBySideScrollPane.this.getLayout();
			sideBySideLayout.leadingPortion = p.x / (float)JSideBySideScrollPane.this.getWidth();
			JSideBySideScrollPane.this.revalidate();
		}

		private final MouseListener mouseListener = new MouseListener() {
			@Override
			public void mouseEntered( MouseEvent e ) {
				CursorUtilities.pushAndSetPredefinedCursor( e.getComponent(), Cursor.E_RESIZE_CURSOR );
			}

			@Override
			public void mouseExited( MouseEvent e ) {
				CursorUtilities.popAndSet( e.getComponent() );
			}

			@Override
			public void mousePressed( MouseEvent e ) {
			}

			@Override
			public void mouseReleased( MouseEvent e ) {
			}

			@Override
			public void mouseClicked( MouseEvent e ) {
			}
		};

		private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
			@Override
			public void mouseMoved( MouseEvent e ) {
			}

			@Override
			public void mouseDragged( MouseEvent e ) {
				handleMouseDragged( e );
			}
		};
	}

	private class JSideBySideScrollBar extends JScrollBar implements UIResource {
		public JSideBySideScrollBar( int orientation ) {
			super( orientation );
		}

		@Override
		public Color getBackground() {
			return JSideBySideScrollPane.this.getBackground();
		}

		@Override
		public void updateUI() {
			this.setUI( SmallerFootprintScrollBarUI.createUI() );
		}

		@Override
		protected void paintComponent( Graphics g ) {
			if( ScrollBarPaintUtilities.isPaintRequiredFor( this ) ) {
				super.paintComponent( g );
			} else {
				Graphics2D g2 = (Graphics2D)g;
				Shape clip = g.getClip();
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

	private final JViewport leadingViewport = new JViewport();
	private final JSideBySideDivider divider = new JSideBySideDivider();
	private final JViewport trailingViewport = new JViewport();

	private final JSideBySideScrollBar verticalScrollBar = new JSideBySideScrollBar( JScrollBar.VERTICAL );
	private final JSideBySideScrollBar horizontalScrollBar = new JSideBySideScrollBar( JScrollBar.HORIZONTAL );

	private final ChangeListener viewportListener = new ChangeListener() {
		@Override
		public void stateChanged( ChangeEvent e ) {
			updateScrollBars();
		}
	};

	private final ChangeListener verticalScrollListener = new ChangeListener() {
		@Override
		public void stateChanged( ChangeEvent e ) {
			BoundedRangeModel model = (BoundedRangeModel)( e.getSource() );
			int leadingY = model.getValue(); //todo
			int trailingY = model.getValue(); //todo
			updateViewY( leadingViewport, leadingY );
			updateViewY( trailingViewport, trailingY );
		}
	};
	private final ChangeListener horizontalScrollListener = new ChangeListener() {
		@Override
		public void stateChanged( ChangeEvent e ) {
			BoundedRangeModel model = (BoundedRangeModel)( e.getSource() );
			int leadingX = model.getValue(); //todo
			int trailingX = model.getValue(); //todo
			updateViewX( leadingViewport, leadingX );
			updateViewX( trailingViewport, trailingX );
		}
	};

	private final MouseWheelListener mouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved( MouseWheelEvent e ) {
			handleMouseWheelMoved( e );
		}
	};

	public static void main( String[] args ) {
		class OvalPanel extends JPanel {
			@Override
			protected void paintComponent( Graphics g ) {
				super.paintComponent( g );
				Dimension size = this.getPreferredSize();
				g.fillOval( 0, 0, size.width, size.height );
			}
		}
		JPanel leadingView = new OvalPanel();
		leadingView.setPreferredSize( new Dimension( 1000, 600 ) );

		JPanel trailingView = new OvalPanel();
		trailingView.setPreferredSize( new Dimension( 600, 1000 ) );

		leadingView.setBackground( Color.RED );
		trailingView.setBackground( Color.WHITE );

		JSideBySideScrollPane jSideBySideScrollPane = new JSideBySideScrollPane( leadingView, trailingView );
		jSideBySideScrollPane.setBackground( Color.GREEN );

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( jSideBySideScrollPane );
		frame.setSize( 400, 300 );
		//frame.pack();
		frame.setVisible( true );
	}
}
