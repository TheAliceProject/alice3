package org.alice.ide.declarationseditor.events.components;

class StickyLayout implements java.awt.LayoutManager2 {
	private final int vGap;
	
	public StickyLayout( int vGap ) {
		this.vGap = vGap;
	}
	
	public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
	}
	public void addLayoutComponent( String name, java.awt.Component comp ) {
		this.addLayoutComponent( comp, name );
	}

	public void removeLayoutComponent( java.awt.Component comp ) {
	}

	private javax.swing.JScrollPane getMain( java.awt.Container target ) {
		return (javax.swing.JScrollPane)target.getComponent( 0 );
	}
	private java.awt.Component getBottom( java.awt.Container target ) {
		return target.getComponent( 1 );
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
			
			javax.swing.JScrollPane mainComponent = this.getMain( target );
			if( mainComponent != null ) {
				java.awt.Dimension d = mainComponent.getMinimumSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			java.awt.Component bottomComponent = this.getBottom( target );
			if( bottomComponent != null ) {
				java.awt.Dimension d = bottomComponent.getMinimumSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			addInsets( dim, target );
			return dim;
		}
	}

	public java.awt.Dimension preferredLayoutSize( java.awt.Container target ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "preferredLayoutSize", target );
		synchronized( target.getTreeLock() ) {
			java.awt.Dimension dim = new java.awt.Dimension( 0, 0 );
			javax.swing.JScrollPane mainComponent = this.getMain( target );
			if( mainComponent != null ) {
				java.awt.Dimension d = mainComponent.getPreferredSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			java.awt.Component bottomComponent = this.getBottom( target );
			if( bottomComponent != null ) {
				java.awt.Dimension d = bottomComponent.getPreferredSize();
				dim.width = Math.max( d.width, dim.width );
				dim.height += d.height + vGap;
			}
			addInsets( dim, target );
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
			int right = target.getWidth() - insets.right;
			
			javax.swing.JScrollPane mainComponent = this.getMain( target );
			if( mainComponent != null ) {
				mainComponent.setSize( right - left, mainComponent.getHeight() );
				java.awt.Dimension d = mainComponent.getPreferredSize();
				if( d.width > right-left ) {
					d.height += mainComponent.getHorizontalScrollBar().getPreferredSize().height;
				}
				mainComponent.setBounds( left, top, right - left, d.height );
				top += d.height + vGap;
			}
			java.awt.Component bottomComponent = this.getBottom( target );
			if( bottomComponent != null ) {
				bottomComponent.setSize( right - left, bottomComponent.getHeight() );
				java.awt.Dimension d = bottomComponent.getPreferredSize();
				bottomComponent.setBounds( left, top, right - left, d.height );
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
}

public class StickyBottomPanel extends org.lgna.croquet.components.Panel {
	public StickyBottomPanel( org.lgna.croquet.components.ScrollPane top, org.lgna.croquet.components.JComponent<?> bottom ) {
		this.internalAddComponent( top, org.lgna.croquet.components.BorderPanel.Constraint.CENTER.getInternal() );
		this.internalAddComponent( bottom, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_END.getInternal() );
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new StickyLayout( 4 );
	}
}
