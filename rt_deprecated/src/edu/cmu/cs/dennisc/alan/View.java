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
public abstract class View {
	private static final float DEFAULT_INSET = 2;
	// todo: reduce visibility to private
	protected float m_leftInset = DEFAULT_INSET * 2;
	protected float m_rightInset = DEFAULT_INSET;
	protected float m_topInset = DEFAULT_INSET;
	protected float m_bottomInset = DEFAULT_INSET;

	// todo: reduce visibility to private
	protected java.awt.geom.Rectangle2D.Float m_localBounds = new java.awt.geom.Rectangle2D.Float();
	// todo: reduce visibility to private
	protected java.awt.geom.Rectangle2D.Float m_absoluteBounds = new java.awt.geom.Rectangle2D.Float();

	private boolean m_isLayoutRequired = true;

	private CompositeView m_parent = null;

	public CompositeView getParent() {
		return m_parent;
	}
	

	private java.util.List< edu.cmu.cs.dennisc.alan.event.HierarchyListener > m_hierarchyListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.alan.event.HierarchyListener >();

	public void addHierarchyListener( edu.cmu.cs.dennisc.alan.event.HierarchyListener l ) {
		synchronized( m_hierarchyListeners ) {
			m_hierarchyListeners.add( l );
		}
	}
	public void removeHierarchyListener( edu.cmu.cs.dennisc.alan.event.HierarchyListener l ) {
		synchronized( m_hierarchyListeners ) {
			m_hierarchyListeners.remove( l );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.alan.event.HierarchyListener > getHierarchyListeners() {
		synchronized( m_hierarchyListeners ) {
			return m_hierarchyListeners;
		}
	}
	protected void fireHierarchyChanging( edu.cmu.cs.dennisc.alan.event.HierarchyEvent e ) {
		synchronized( m_hierarchyListeners ) {
			for( edu.cmu.cs.dennisc.alan.event.HierarchyListener l : m_hierarchyListeners ) {
				l.hierarchyChanging( e );
			}
		}
	}
	protected void fireHierarchyChanged( edu.cmu.cs.dennisc.alan.event.HierarchyEvent e ) {
		synchronized( m_hierarchyListeners ) {
			for( edu.cmu.cs.dennisc.alan.event.HierarchyListener l : m_hierarchyListeners ) {
				l.hierarchyChanged( e );
			}
		}
	}

	/*package-private*/void setParent( CompositeView parent ) {
		//todo: descend tree
		edu.cmu.cs.dennisc.alan.event.HierarchyEvent e = new edu.cmu.cs.dennisc.alan.event.HierarchyEvent( this );
		fireHierarchyChanging( e );
		m_parent = parent;
		fireHierarchyChanged( e );
		setLayoutRequired( true );
	}
	
	public View getRoot() {
		if( m_parent != null ) {
			return m_parent.getRoot();
		} else {
			return this;
		}
	}

	protected float getXAbsoluteTranslation( float xOrigin ) {
		float rv;
		if( m_parent != null ) {
			rv = m_parent.getXAbsoluteTranslation( xOrigin ) + m_xTranslation;
		} else {
			rv = xOrigin - getXMin( true );
		}
		return rv;
	}
	protected float getYAbsoluteTranslation( float yOrigin ) {
		float rv;
		if( m_parent != null ) {
			rv = m_parent.getYAbsoluteTranslation( yOrigin ) + m_yTranslation;
		} else {
			rv = yOrigin - getYMin( true );
		}
		return rv;
	}

	private boolean m_isInteractive = false;

	public boolean isInteractive() {
		return m_isInteractive;
	}
	public void setInteractive( boolean isInteractive ) {
		m_isInteractive = isInteractive;
	}

	private boolean m_isHighlighted;

	public boolean isHighlighted() {
		return m_isHighlighted;
	}
	public void setHighlighted( boolean isHighlighted, java.awt.event.MouseEvent mouseEvent ) {
		m_isHighlighted = isHighlighted;
	}

	private boolean m_isPressed;

	public boolean isPressed() {
		return m_isPressed;
	}
	public void setPressed( boolean isPressed, java.awt.event.MouseEvent mouseEvent ) {
		m_isPressed = isPressed;
	}

	private boolean m_isSelected;

	public boolean isSelected() {
		return m_isSelected;
	}
	public void setSelected( boolean isSelected, java.awt.event.MouseEvent mouseEvent ) {
		m_isSelected = isSelected;
	}

	private java.awt.Color m_clearColor = null;

	public java.awt.Color getClearColor() {
		if( m_clearColor != null ) {
			return m_clearColor;
		} else {
			if( m_parent != null ) {
				return m_parent.getClearColor();
			} else {
				return null;
			}
		}
	}
	public void setClearColor( java.awt.Color clearColor ) {
		m_clearColor = clearColor;
	}

	//	private BevelState m_bevelState = null;
	//	public BevelState getBevelState() {
	//		return m_bevelState;
	//	}
	//	public void setBevelState( BevelState bevelState ) {
	//		m_bevelState = bevelState;
	//	}

	public java.awt.geom.Rectangle2D.Float accessLocalBounds() {
		return m_localBounds;
	}
	//	private java.awt.geom.Rectangle2D.Float getLocalBounds( java.awt.geom.Rectangle2D.Float rv ) {
	//		rv.setRect( getXMin( false ), getYMin( false ), getWidth(), getHeight() );
	//		return rv;
	//	}
	//	private java.awt.geom.Rectangle2D.Float getLocalBounds() {
	//		return getLocalBounds( new java.awt.geom.Rectangle2D.Float() );
	//	}
	public java.awt.geom.Rectangle2D.Float accessAbsoluteBounds() {
		return m_absoluteBounds;
	}
	public java.awt.geom.Rectangle2D.Float getAbsoluteBounds( java.awt.geom.Rectangle2D.Float rv ) {
		rv.setRect( m_absoluteBounds );
		return rv;
	}
	public java.awt.geom.Rectangle2D.Float getAbsoluteBounds() {
		return getAbsoluteBounds( new java.awt.geom.Rectangle2D.Float() );
	}
	public java.awt.geom.Rectangle2D.Float getAbsoluteBoundsFromOrigin( java.awt.geom.Rectangle2D.Float rv, float xOrigin, float yOrigin ) {
		rv.setRect( m_absoluteBounds );
		//		rv.x += xOrigin - getXMin( true ); 
		//		rv.y += yOrigin - getYMin( true ); 
		//		getLocalBounds( rv );
		//		rv.x += getXAbsoluteTranslation( xOrigin );
		//		rv.y += getXAbsoluteTranslation( yOrigin );
		return rv;
	}
	public java.awt.geom.Rectangle2D.Float getAbsoluteBoundsFromOrigin( float xOrigin, float yOrigin ) {
		return getAbsoluteBoundsFromOrigin( new java.awt.geom.Rectangle2D.Float(), xOrigin, yOrigin );
	}

	protected void updateAbsoluteBounds( float x, float y ) {
		m_absoluteBounds.setRect( getXMin( true ) + x, getYMin( true ) + y, getWidth(), getHeight() );
	}

	//	protected float m_xTranslation = Float.NaN;
	//	protected float m_yTranslation = Float.NaN;
	protected float m_xTranslation = 0;
	protected float m_yTranslation = 0;

	protected void setTranslation( float xTranslation, float yTranslation ) {
		m_xTranslation = xTranslation;
		m_yTranslation = yTranslation;
	}
	//todo
	public float getXTranslation() {
		return m_xTranslation;
	}
	//todo
	public float getYTranslation() {
		return m_yTranslation;
	}

	public float getXMin( boolean isTranslationFromParentToBeAccountedFor ) {
		float xMin = m_localBounds.x - m_leftInset;
		if( isTranslationFromParentToBeAccountedFor ) {
			xMin += m_xTranslation;
		}
		return xMin;
	}
	public float getXMax( boolean isTranslationFromParentToBeAccountedFor ) {
		float xMax = m_localBounds.x + m_localBounds.width + m_rightInset;
		if( isTranslationFromParentToBeAccountedFor ) {
			xMax += m_xTranslation;
		}
		return xMax;
	}
	public float getYMin( boolean isTranslationFromParentToBeAccountedFor ) {
		float yMin = m_localBounds.y - m_topInset;
		if( isTranslationFromParentToBeAccountedFor ) {
			yMin += m_yTranslation;
		}
		return yMin;
	}
	public float getYMax( boolean isTranslationFromParentToBeAccountedFor ) {
		float yMax = m_localBounds.y + m_localBounds.height + m_bottomInset;
		if( isTranslationFromParentToBeAccountedFor ) {
			yMax += m_yTranslation;
		}
		return yMax;
	}
	public float getWidth() {
		return m_localBounds.width + m_leftInset + m_rightInset;
	}
	public float getHeight() {
		return m_localBounds.height + m_topInset + m_bottomInset;
	}

	protected abstract void calculateLocalBounds( java.awt.Graphics g );
	protected void updateLocalBounds() {
	}

	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		rv.setSize( getWidth(), getHeight() );
		return rv;
	}
	public java.awt.Dimension getSize() {
		return getSize( new java.awt.Dimension() );
	}
	protected void paintBackground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		//		g2.setColor( java.awt.Color.RED );
		//		g2.fill( m_bounds );
	}
	protected void paintChildren( java.awt.Graphics2D g2 ) {
	}
	protected void paintForeground( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		//		g2.setColor( java.awt.Color.GREEN );
		//		g2.draw( m_bounds );
	}
	protected void paint( java.awt.Graphics2D g2, float x, float y, float width, float height ) {
		paintBackground( g2, x, y, width, height );
		paintChildren( g2 );
		paintForeground( g2, x, y, width, height );
	}
	public final void paint( java.awt.Graphics2D g2 ) {
		g2.translate( m_xTranslation, m_yTranslation );

		float x = getXMin( false );
		float y = getYMin( false );
		float width = getWidth();
		float height = getHeight();

		java.awt.Rectangle bounds = g2.getClipBounds();
		if( bounds == null || bounds.intersects( x, y, width, height ) ) {
			paint( g2, x, y, width, height );
		}

		g2.translate( -m_xTranslation, -m_yTranslation );
	}

	public boolean isLayoutRequired() {
		return m_isLayoutRequired;
	}
	public void setLayoutRequired( boolean isLayoutRequired ) {
		m_isLayoutRequired = isLayoutRequired;
		if( m_isLayoutRequired ) {
			if( m_parent != null ) {
				m_parent.setLayoutRequired( isLayoutRequired );
			}
		}
	}

	protected void leftJustifyVerticalLayouts() {
	}

	protected boolean containsLocalPt( float x, float y ) {
		float xMin = getXMin( false );
		float xMax = getXMax( false );
		float yMin = getYMin( false );
		float yMax = getYMax( false );
		return (xMin < x && x < xMax) && (yMin < y && y < yMax);
	}

	protected java.awt.geom.Point2D.Float getOffsetWithin( java.awt.geom.Point2D.Float rv, float x, float y ) {
		if( rv != null ) {
			rv.x = x - getXMin( false );
			rv.y = y - getYMin( false );
		}
		return rv;
	}
	//	protected < E extends View > E pickInternal( float x, float y, Class< E > cls, java.awt.geom.Point2D.Float offsetWithin ) {
	//		E rv;
	//		if( containsLocalPt( x, y ) ) {
	//			if( cls.isAssignableFrom( this.getClass() ) ) {
	//				rv = (E)this;
	//				getOffsetWithin( offsetWithin, x, y );
	//			} else {
	//				rv = null;
	//			}
	//		} else {
	//			rv = null;
	//		}
	//		return rv;
	//	}

	protected View pickInnerMost( float x, float y ) {
		if( containsLocalPt( x, y ) ) {
			return this;
		} else {
			return null;
		}
	}

	//	public final < E extends View > E pick( float x, float y, Class< E > cls, java.awt.geom.Point2D.Float offsetWithin ) {
	//		if( offsetWithin != null ) {
	//			offsetWithin.x = offsetWithin.y = Float.NaN;
	//		}
	//		return pickInternal( x - m_xTranslation, y - m_yTranslation, cls, offsetWithin );
	//	}

	protected boolean updateBoundsWithin( java.awt.geom.Point2D.Float out_offsetWithin, float x, float y, View view ) {
		if( view == this ) {
			getOffsetWithin( out_offsetWithin, x, y );
			return true;
		} else {
			return false;
		}
	}

	private <E extends View> boolean isMatch( Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< E >... criteria ) {
		if( cls.isAssignableFrom( getClass() ) ) {
			E e = (E)this;
			for( edu.cmu.cs.dennisc.pattern.Criterion< E > criterion : criteria ) {
				if( criterion.accept( e ) ) {
					//pass
				} else {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public final <E extends View> E pick( java.awt.geom.Point2D.Float out_offsetWithin, float x, float y, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< E >... criteria ) {
		if( out_offsetWithin != null ) {
			out_offsetWithin.x = out_offsetWithin.y = Float.NaN;
		}
		View view = pickInnerMost( x - m_xTranslation, y - m_yTranslation );
		while( view != null ) {
			if( view.isMatch( cls, criteria ) ) {
				break;
			} else {
				view = view.getParent();
			}
		}
		if( view != null ) {
			if( out_offsetWithin != null ) {
				updateBoundsWithin( out_offsetWithin, x - m_xTranslation, y - m_yTranslation, view );
			}
			return (E)view;
		} else {
			return null;
		}
	}

	public final void paintFromOrigin( java.awt.Graphics2D g2, float xOrigin, float yOrigin ) {
		float xDelta = xOrigin - getXMin( true );
		float yDelta = yOrigin - getYMin( true );
		if( Float.isNaN( xDelta ) ) {
			xDelta = 0;
		}
		if( Float.isNaN( yDelta ) ) {
			yDelta = 0;
		}
		g2.translate( +xDelta, +yDelta );
		paint( g2 );
		g2.translate( -xDelta, -yDelta );
	}
	public final <E extends View> E pickFromOrigin( java.awt.geom.Point2D.Float out_offsetWithin, float x, float y, float xOrigin, float yOrigin, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< E >... criteria ) {
		float xDelta = xOrigin - getXMin( true );
		float yDelta = yOrigin - getYMin( true );
		if( Float.isNaN( xDelta ) ) {
			xDelta = 0;
		}
		if( Float.isNaN( yDelta ) ) {
			yDelta = 0;
		}
		return pick( out_offsetWithin, x - xDelta, y - yDelta, cls, criteria );
	}

//	public void paintAbsoluteBounds( java.awt.Graphics2D g2 ) {
//		g2.draw( m_absoluteBounds );
//	}
}
