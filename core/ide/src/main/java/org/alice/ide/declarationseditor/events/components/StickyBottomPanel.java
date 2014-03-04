package org.alice.ide.declarationseditor.events.components;

class StickyLayout implements java.awt.LayoutManager2 {
	public StickyLayout( int vGap ) {
		this.vGap = vGap;
	}

	public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
		if( org.lgna.croquet.views.BorderPanel.Constraint.CENTER.getInternal().equals( constraints ) ) {
			this.mainComponent = comp;
		} else if( org.lgna.croquet.views.BorderPanel.Constraint.PAGE_END.getInternal().equals( constraints ) ) {
			this.bottomComponent = comp;
		} else {
			assert false : constraints;
		}
	}

	public void addLayoutComponent( String name, java.awt.Component comp ) {
		this.addLayoutComponent( comp, name );
	}

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
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "preferredLayoutSize", dim, target );
			return dim;
		}
	}

	public java.awt.Dimension maximumLayoutSize( java.awt.Container target ) {
		return new java.awt.Dimension( Integer.MAX_VALUE, Integer.MAX_VALUE );
	}

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

	public void invalidateLayout( java.awt.Container target ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "invalidateLayout", target );
	}

	public float getLayoutAlignmentX( java.awt.Container parent ) {
		return 0.0f;
	}

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
