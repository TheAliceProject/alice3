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
package org.alice.ide.declarationseditor.events.components;

class StickyLayout implements java.awt.LayoutManager2 {
	public StickyLayout( int vGap ) {
		this.vGap = vGap;
	}

	@Override
	public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
		if( org.lgna.croquet.views.BorderPanel.Constraint.CENTER.getInternal().equals( constraints ) ) {
			this.mainComponent = comp;
		} else if( org.lgna.croquet.views.BorderPanel.Constraint.PAGE_END.getInternal().equals( constraints ) ) {
			this.bottomComponent = comp;
		} else {
			assert false : constraints;
		}
	}

	@Override
	public void addLayoutComponent( String name, java.awt.Component comp ) {
		this.addLayoutComponent( comp, name );
	}

	@Override
	public void removeLayoutComponent( java.awt.Component comp ) {
		if( this.mainComponent == comp ) {
			this.mainComponent = null;
		} else if( this.bottomComponent == comp ) {
			this.bottomComponent = null;
		}
	}

	private static java.awt.Dimension addInsets( java.awt.Dimension rv, java.awt.Container target ) {
		java.awt.Insets insets = target.getInsets();
		rv.width += insets.left + insets.right;
		rv.height += insets.top + insets.bottom;
		return rv;
	}

	@Override
	public java.awt.Dimension minimumLayoutSize( java.awt.Container target ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "minimumLayoutSize", target );
		synchronized( target.getTreeLock() ) {
			java.awt.Dimension dim = new java.awt.Dimension( 0, 0 );

			if( this.mainComponent != null ) {
				java.awt.Dimension d = this.mainComponent.getMinimumSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			if( this.bottomComponent != null ) {
				java.awt.Dimension d = bottomComponent.getMinimumSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			addInsets( dim, target );
			return dim;
		}
	}

	@Override
	public java.awt.Dimension preferredLayoutSize( java.awt.Container target ) {
		synchronized( target.getTreeLock() ) {
			java.awt.Dimension dim = new java.awt.Dimension( 0, 0 );
			if( this.mainComponent != null ) {
				java.awt.Dimension d = this.mainComponent.getPreferredSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			if( this.bottomComponent != null ) {
				java.awt.Dimension d = this.bottomComponent.getPreferredSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			addInsets( dim, target );
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "preferredLayoutSize", dim, target );
			return dim;
		}
	}

	@Override
	public java.awt.Dimension maximumLayoutSize( java.awt.Container target ) {
		return new java.awt.Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
	}

	@Override
	public void layoutContainer( java.awt.Container target ) {
		synchronized( target.getTreeLock() ) {
			java.awt.Insets insets = target.getInsets();
			int top = insets.top;
			int left = insets.left;
			int bottom = target.getHeight() - insets.bottom;
			int right = target.getWidth() - insets.right;

			int width = right - left;
			int bottomHeight;
			if( bottomComponent != null ) {
				bottomComponent.setSize( width, bottomComponent.getHeight() );
				java.awt.Dimension d = bottomComponent.getPreferredSize();
				bottomHeight = d.height;
			} else {
				bottomHeight = 0;
			}

			if( this.mainComponent != null ) {
				this.mainComponent.setSize( width, this.mainComponent.getHeight() );
				java.awt.Dimension d = this.mainComponent.getPreferredSize();
				if( this.mainComponent instanceof javax.swing.JScrollPane ) {
					javax.swing.JScrollPane jScrollPane = (javax.swing.JScrollPane)this.mainComponent;
					if( d.width > ( right - left ) ) {
						d.height += jScrollPane.getHorizontalScrollBar().getPreferredSize().height;
					}
				}

				d.height = Math.min( d.height, bottom - top - bottomHeight );

				this.mainComponent.setBounds( left, top, width, d.height );
				top += d.height + vGap;
			}
			if( bottomComponent != null ) {
				bottomComponent.setBounds( left, top, width, bottomHeight );
			}
		}
	}

	@Override
	public void invalidateLayout( java.awt.Container target ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "invalidateLayout", target );
	}

	@Override
	public float getLayoutAlignmentX( java.awt.Container parent ) {
		return 0.0f;
	}

	@Override
	public float getLayoutAlignmentY( java.awt.Container parent ) {
		return 0.0f;
	}

	public java.awt.Component getMainComponent() {
		return this.mainComponent;
	}

	public java.awt.Component getBottomComponent() {
		return this.bottomComponent;
	}

	private final int vGap;
	private java.awt.Component mainComponent;
	private java.awt.Component bottomComponent;
}

public class StickyBottomPanel extends org.lgna.croquet.views.Panel {
	//	public StickyBottomPanel() {
	//		this.internalAddComponent( mainPanel, org.lgna.croquet.views.BorderPanel.Constraint.PAGE_START.getInternal() );
	//	}

	public void setTopView( org.lgna.croquet.views.SwingComponentView<?> top ) {
		synchronized( this.getTreeLock() ) {
			//			org.lgna.croquet.views.AwtComponentView<?> component = this.mainPanel.getCenterComponent();
			//			if( component != null ) {
			//				this.mainPanel.removeComponent( component );
			//			}
			//			this.mainPanel.addCenterComponent( top );
			StickyLayout stickyLayout = this.getLayout();
			java.awt.Component awtComponent = stickyLayout.getMainComponent();
			if( awtComponent != null ) {
				this.getAwtComponent().remove( awtComponent );
			}
			this.internalAddComponent( top, org.lgna.croquet.views.BorderPanel.Constraint.CENTER.getInternal() );
		}
	}

	public void setBottomView( org.lgna.croquet.views.SwingComponentView<?> bottom ) {
		synchronized( this.getTreeLock() ) {
			//			org.lgna.croquet.views.AwtComponentView<?> component = this.mainPanel.getPageEndComponent();
			//			if( component != null ) {
			//				this.mainPanel.removeComponent( component );
			//			}
			//			this.mainPanel.addPageEndComponent( bottom );
			StickyLayout stickyLayout = this.getLayout();
			java.awt.Component awtComponent = stickyLayout.getBottomComponent();
			if( awtComponent != null ) {
				this.getAwtComponent().remove( awtComponent );
			}
			this.internalAddComponent( bottom, org.lgna.croquet.views.BorderPanel.Constraint.PAGE_END.getInternal() );
		}
	}

	private StickyLayout getLayout() {
		return (StickyLayout)this.getAwtComponent().getLayout();
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new StickyLayout( 4 );
		//return new java.awt.BorderLayout();
	}

	//private final org.lgna.croquet.views.BorderPanel mainPanel = new org.lgna.croquet.views.BorderPanel();
}
