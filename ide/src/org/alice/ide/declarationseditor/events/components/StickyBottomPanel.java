package org.alice.ide.declarationseditor.events.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.lgna.croquet.components.BorderPanel.Constraint;
import org.lgna.croquet.components.ScrollPane;

public class StickyBottomPanel extends org.lgna.croquet.components.Panel {

	private final boolean IS_BORDER_PANEL = false;

	@Override
	protected LayoutManager createLayoutManager(JPanel jPanel) {
		if (IS_BORDER_PANEL) {
			return new BorderLayout();
		} else {
			return new LayoutManager2() {
				
				int vgap = 0;
				JScrollPane top;
				JComponent bottom;

				public void addLayoutComponent(String name, Component comp) {
					this.addLayoutComponent(comp, name);
				}
				public void addLayoutComponent(Component comp, Object constraints) {
					synchronized (comp.getTreeLock()) {
						if (Constraint.PAGE_START.getInternal().equals(constraints)) {
							top = (JScrollPane)comp;
						} else if (Constraint.PAGE_END.getInternal().equals(constraints)) {
							bottom = (JComponent)comp;
						} else {
							throw new IllegalArgumentException( "cannot add to layout: unknown constraint: " + constraints);
						}
					}
				}

				public void removeLayoutComponent(Component comp) {
					synchronized (comp.getTreeLock()) {
						if (comp == top) {
							top = null;
						} else if (comp == bottom) {
							bottom = null;
						}
					}
				}

				public Dimension minimumLayoutSize(Container target) {
			    	//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "minimumLayoutSize", target );
					synchronized (target.getTreeLock()) {
						Dimension dim = new Dimension(0, 0);

						if (this.top != null) {
							Dimension d = this.top.getMinimumSize();
							dim.width = Math.max(d.width, dim.width);
							dim.height += d.height + vgap;
						}
						if (this.bottom != null) {
							Dimension d = this.bottom.getMinimumSize();
							dim.width = Math.max(d.width, dim.width);
							dim.height += d.height + vgap;
						}

						Insets insets = target.getInsets();
						dim.width += insets.left + insets.right;
						dim.height += insets.top + insets.bottom;

						return dim;
					}
				}

				public Dimension preferredLayoutSize(Container target) {
			    	//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "preferredLayoutSize", target );
					synchronized (target.getTreeLock()) {
						Dimension dim = new Dimension(0, 0);

						if (this.top != null) {
							Dimension d = this.top.getPreferredSize();
							dim.width = Math.max(d.width, dim.width);
							dim.height += d.height + vgap;
						}
						if (this.bottom != null) {
							Dimension d = this.bottom.getPreferredSize();
							dim.width = Math.max(d.width, dim.width);
							dim.height += d.height + vgap;
						}

						Insets insets = target.getInsets();
						dim.width += insets.left + insets.right;
						dim.height += insets.top + insets.bottom;

						return dim;
					}
				}

				public void layoutContainer(Container target) {
			    	//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "layoutContainer", target );
					synchronized (target.getTreeLock()) {
						Insets insets = target.getInsets();
						int top = insets.top;
						int bottom = target.getHeight() - insets.bottom;
						int left = insets.left;
						int right = target.getWidth() - insets.right;
						int breakHeight;// = this.top.height;
						int topHeight = this.top.getPreferredSize().height;
						int bottomHeight = this.bottom.getPreferredSize().height;
						if( this.bottom != null && this.top != null ){
							if ( topHeight < (bottom - bottomHeight) ){
								breakHeight = topHeight;
							} else {
								breakHeight = (bottom - bottomHeight);
							}
						}else {
							breakHeight = insets.bottom;
						}
						breakHeight += 10;
						if (this.top != null) {
							Component c = this.top;
							c.setSize(right - left, c.getHeight());
							c.setBounds(left, top, right - left, breakHeight);
							top += breakHeight + vgap;
						}
						if (this.bottom != null) {
							Component c = this.bottom;
							c.setSize(right - left, c.getHeight());
							Dimension d = c.getPreferredSize();
							c.setBounds(left, top, right - left,
									d.height);
							bottom -= d.height + vgap;
						}
					}
				}

			    public Dimension maximumLayoutSize(Container target) {
			    	//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "maximumLayoutSize", target );
			        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
			    }
			    public float getLayoutAlignmentX(Container parent) {
			        return 0.5f;
			    }
			    public float getLayoutAlignmentY(Container parent) {
			        return 0.5f;
			    }
			    public void invalidateLayout(Container target) {
			    	//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "invalidateLayout", target );
			    }
			};
		}
	}
	
	public void addTop( ScrollPane top ){
		internalAddComponent(top, Constraint.PAGE_START.getInternal());
	}
	
	public void addBottom( org.lgna.croquet.components.Component<?> bottom ){
		internalAddComponent(bottom, Constraint.PAGE_END.getInternal());
	}
	
//	public void addComponent( org.lgna.croquet.components.Component<?> component,
//			Constraint constraint) {
//		assert (constraint == Constraint.PAGE_END || constraint == Constraint.PAGE_START);
//		if (constraint == Constraint.PAGE_START) {
//			component = new ScrollPane(component);
//		}
//		internalAddComponent(component, constraint.getInternal());
//	}

}
