/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.alan;

/**
 * @author Dennis Cosgrove
 */
public class CompositeView extends View {
	private java.util.List< View > m_children = new java.util.LinkedList< View >();
	private LayoutManager m_layoutManager = null;

	public void add( View child ) {
		add( m_children.size(), child );
	}

	public void add( int index, View child ) {
		synchronized( m_children ) {
			m_children.add( index, child );
			child.setParent( this );
			setLayoutRequired( true );
		}
	}

	public void remove( View child ) {
		synchronized( m_children ) {
			m_children.remove( child );
			child.setParent( null );
			setLayoutRequired( true );
		}
	}

	
	
	public void clear() {
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.setParent( null );
			}
			m_children.clear();
			setLayoutRequired( true );
		}
	}
	public Iterable< View > accessChildren() {
		return m_children;
	}
	public View getChild( int index ) {
		return m_children.get( index );
	}
	public <E> E getChild( int index, Class< E > cls ) {
		return (E)getChild( index );
	}
	public int getChildCount() {
		return m_children.size();
	}

	protected void setChildrenHighlighted( boolean isHighlighted, java.awt.event.MouseEvent mouseEvent ) {
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.setHighlighted( isHighlighted, mouseEvent );
			}
		}
	}
	protected void setChildrenSelected( boolean isSelected, java.awt.event.MouseEvent mouseEvent ) {
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.setSelected( isSelected, mouseEvent );
			}
		}
	}

	@Override
	public void setHighlighted( boolean isHighlighted, java.awt.event.MouseEvent mouseEvent ) {
		super.setHighlighted( isHighlighted, mouseEvent );
		setChildrenHighlighted( isHighlighted, mouseEvent );
	}
	@Override
	public void setSelected( boolean isSelected, java.awt.event.MouseEvent mouseEvent ) {
		super.setSelected( isSelected, mouseEvent );
		setChildrenSelected( isSelected, mouseEvent );
	}
	@Override
	protected final void calculateLocalBounds( java.awt.Graphics g ) {
		m_localBounds.setRect( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.calculateLocalBounds( g );
			}
		}
	}

	@Override
	protected void updateLocalBounds() {
		if( m_children.isEmpty() ) {
			m_localBounds.x = 0;
			m_localBounds.y = 0;
			m_localBounds.width = 0;
			m_localBounds.height = 0;
		} else {
			assert m_layoutManager != null : this;
			synchronized( m_children ) {
				m_layoutManager.layout( m_children );
				float xMin = Float.MAX_VALUE;
				float yMin = Float.MAX_VALUE;
				float xMax = Float.MIN_VALUE;
				float yMax = Float.MIN_VALUE;
				for( View child : m_children ) {
					xMin = Math.min( xMin, child.getXMin( true ) );
					yMin = Math.min( yMin, child.getYMin( true ) );
					xMax = Math.max( xMax, child.getXMax( true ) );
					yMax = Math.max( yMax, child.getYMax( true ) );
				}
				m_localBounds.x = xMin;
				m_localBounds.y = yMin;
				m_localBounds.width = (xMax - xMin);
				m_localBounds.height = (yMax - yMin);
			}
		}
	}
	@Override
	protected void updateAbsoluteBounds( float x, float y ) {
		super.updateAbsoluteBounds( x, y );
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.updateAbsoluteBounds( x + m_xTranslation, y + m_yTranslation );
			}
		}
	}

	protected float getIndentAmount( int index ) { 
		return 0.0f;
	}
	@Override
	protected void leftJustifyVerticalLayouts() {
		//todo: clean this up
		if( this instanceof VerticallyLaidOutView ) {
			if( m_children.isEmpty() ) {
				//pass
			} else {
				float xMin = Float.MAX_VALUE;
				synchronized( m_children ) {
					for( View child : m_children ) {
						child.leftJustifyVerticalLayouts();
						xMin = Math.min( xMin, child.getXMin( true ) );
					}
					for( View child : m_children ) {
						float xDelta = child.getXMin( true ) - xMin;
						child.m_xTranslation -= xDelta;
					}
					int i = 0;
					for( View child : m_children ) {
						child.m_xTranslation += getIndentAmount( i );
						i++;
					}
				}
			}
		}
	}

	public final void layout( java.awt.Graphics g ) {
		assert g != null;
		synchronized( m_children ) {
			calculateLocalBounds( g );
			updateLocalBounds();
			leftJustifyVerticalLayouts();
			updateAbsoluteBounds( -getXMin( true ), -getYMin( true ) );
			setLayoutRequired( false );
		}
	}

	protected float getChildXOrigin() {
		return Float.NaN;
	}
	protected float getChildYOrigin() {
		return Float.NaN;
	}

	@Override
	protected void paintChildren( java.awt.Graphics2D g2 ) {
		synchronized( m_children ) {
			for( View child : m_children ) {
				child.paint( g2 );
			}
		}
	}

	@Override
	protected boolean updateBoundsWithin( java.awt.geom.Point2D.Float out_offsetWithin, float x, float y, View view ) {
		if( super.updateBoundsWithin( out_offsetWithin, x, y, view ) ) {
			//pass
		} else {
			synchronized( m_children ) {
				for( View child : m_children ) {
					if( child.updateBoundsWithin( out_offsetWithin, x - child.m_xTranslation, y - child.m_yTranslation, view ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	@Override
	protected View pickInnerMost( float x, float y ) {
		if( containsLocalPt( x, y ) ) {
			synchronized( m_children ) {
				for( View child : m_children ) {
					View rv = child.pickInnerMost( x - child.m_xTranslation, y - child.m_yTranslation );
					if( rv != null ) {
						return rv;
					}
				}
			}
			return this;
		} else {
			return null;
		}
	}

//	public void paintAbsoluteBounds( java.awt.Graphics2D g2 ) {
//		super.paintAbsoluteBounds( g2 );
//		for( View child : m_children ) {
//			child.paintAbsoluteBounds( g2 );
//		}
//	}

	public LayoutManager getLayoutManager() {
		return m_layoutManager;
	}

	public void setLayoutManager( LayoutManager layoutManager ) {
		m_layoutManager = layoutManager;
	}

}
