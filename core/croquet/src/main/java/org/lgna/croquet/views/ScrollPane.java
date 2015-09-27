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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class ScrollPane extends SwingComponentView<javax.swing.JScrollPane> {
	public enum VerticalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		private int internal;

		private VerticalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}

	public enum HorizontalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		private int internal;

		private HorizontalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}

	public ScrollPane() {
		this( null, null, null );
	}

	public ScrollPane( AwtComponentView<?> viewportView ) {
		this( viewportView, null, null );
	}

	public ScrollPane( VerticalScrollbarPolicy verticalScrollbarPolicy, HorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		this( null, verticalScrollbarPolicy, horizontalScrollbarPolicy );
	}

	public ScrollPane( AwtComponentView<?> viewportView, VerticalScrollbarPolicy verticalScrollbarPolicy, HorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		if( viewportView != null ) {
			this.setViewportView( viewportView );
		}
		if( verticalScrollbarPolicy != null ) {
			this.setVerticalScrollbarPolicy( verticalScrollbarPolicy );
		}
		if( horizontalScrollbarPolicy != null ) {
			this.setHorizontalScrollbarPolicy( horizontalScrollbarPolicy );
		}
	}

	private static class RightToLeftFixScrollPanelLayout extends javax.swing.ScrollPaneLayout {
		@Override
		public void layoutContainer( java.awt.Container parent ) {
			super.layoutContainer( parent );
			javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)parent;
			if( scrollPane.getComponentOrientation().isLeftToRight() ) {
				//pass
			} else {
				//todo?
				javax.swing.JViewport viewport = scrollPane.getViewport();
				java.awt.Rectangle viewportBounds = viewport.getBounds();
				java.awt.Component view = viewport.getView();
				if( view != null ) {
					java.awt.Rectangle viewBounds = view.getBounds();
					if( viewBounds.width < viewportBounds.width ) {
						viewBounds.x = 0;
						viewBounds.width = viewportBounds.width;
						scrollPane.getViewport().getView().setBounds( viewBounds );
					}
				}
			}
		}
	}

	protected edu.cmu.cs.dennisc.javax.swing.components.JScrollPaneCoveringLinuxPaintBug createJScrollPane() {
		edu.cmu.cs.dennisc.javax.swing.components.JScrollPaneCoveringLinuxPaintBug rv = new edu.cmu.cs.dennisc.javax.swing.components.JScrollPaneCoveringLinuxPaintBug() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				return constrainPreferredSizeIfNecessary( rv );
			}
		};
		return rv;
	}

	@Override
	protected final javax.swing.JScrollPane createAwtComponent() {
		javax.swing.JScrollPane rv = this.createJScrollPane();
		rv.setOpaque( true );
		rv.setBorder( null );
		rv.setLayout( new RightToLeftFixScrollPanelLayout() );
		return rv;
	}

	public AwtComponentView<?> getViewportView() {
		return AwtComponentView.lookup( this.getAwtComponent().getViewport().getView() );
	}

	public void setViewportView( AwtComponentView<?> view ) {
		javax.swing.JScrollPane jScrollPane = this.getAwtComponent();
		if( view != null ) {
			final boolean IS_SCROLLABLE_HEEDED = false;
			if( IS_SCROLLABLE_HEEDED && ( view.getAwtComponent() instanceof javax.swing.Scrollable ) ) {
				//pass
			} else {
				if( jScrollPane.getHorizontalScrollBar().getUnitIncrement() == 1 ) {
					this.setBothScrollBarIncrements( 12, 24 );
				}
			}
			jScrollPane.setViewportView( view.getAwtComponent() );
		} else {
			jScrollPane.setViewportView( null );
		}
	}

	public void setVerticalScrollbarPolicy( VerticalScrollbarPolicy verticalScrollbarPolicy ) {
		assert verticalScrollbarPolicy != null : this;
		this.checkEventDispatchThread();
		this.getAwtComponent().setVerticalScrollBarPolicy( verticalScrollbarPolicy.internal );
	}

	public void setHorizontalScrollbarPolicy( HorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		assert horizontalScrollbarPolicy != null : this;
		this.checkEventDispatchThread();
		this.getAwtComponent().setHorizontalScrollBarPolicy( horizontalScrollbarPolicy.internal );
	}

	private void setScrollBarIncrements( javax.swing.JScrollBar scrollBar, int unitIncrement, int blockIncrement ) {
		this.checkEventDispatchThread();
		scrollBar.setUnitIncrement( unitIncrement );
		scrollBar.setBlockIncrement( blockIncrement );
	}

	public void setVerticalScrollBarIncrements( int unitIncrement, int blockIncrement ) {
		this.setScrollBarIncrements( this.getAwtComponent().getVerticalScrollBar(), unitIncrement, blockIncrement );
	}

	public void setHorizontalScrollBarIncrements( int unitIncrement, int blockIncrement ) {
		this.setScrollBarIncrements( this.getAwtComponent().getHorizontalScrollBar(), unitIncrement, blockIncrement );
	}

	public void setBothScrollBarIncrements( int unitIncrement, int blockIncrement ) {
		this.setHorizontalScrollBarIncrements( unitIncrement, blockIncrement );
		this.setVerticalScrollBarIncrements( unitIncrement, blockIncrement );
	}
}
