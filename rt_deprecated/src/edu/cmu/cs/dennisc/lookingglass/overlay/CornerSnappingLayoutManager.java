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

package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public class CornerSnappingLayoutManager extends AbstractLayoutManager {
	private Component m_topLeftComponent = null;
	private Component m_topRightComponent = null;
	private Component m_bottomLeftComponent = null;
	private Component m_bottomRightComponent = null;
	private float m_topPad = 10f;
	private float m_bottomPad = 10f;
	private float m_leftPad = 10f;
	private float m_rightPad = 10f;

	public Component getTopLeftComponent() {
		return m_topLeftComponent;
	}
	public void setTopLeftComponent( Component topLeftComponent ) {
		if( m_topLeftComponent != topLeftComponent ) {
			m_topLeftComponent = topLeftComponent;
			fireLayoutRequired();
		}
	}
	public Component getTopRightComponent() {
		return m_topRightComponent;
	}
	public void setTopRightComponent( Component topRightComponent ) {
		if( m_topRightComponent != topRightComponent ) {
			m_topRightComponent = topRightComponent;
			fireLayoutRequired();
		}
	}

	public Component getBottomLeftComponent() {
		return m_bottomLeftComponent;
	}
	public void setBottomLeftComponent( Component bottomLeftComponent ) {
		if( m_bottomLeftComponent != bottomLeftComponent ) {
			m_bottomLeftComponent = bottomLeftComponent;
			fireLayoutRequired();
		}
	}
	public Component getBottomRightComponent() {
		return m_bottomRightComponent;
	}
	public void setBottomRightComponent( Component bottomRightComponent ) {
		if( m_bottomRightComponent != bottomRightComponent ) {
			m_bottomRightComponent = bottomRightComponent;
			fireLayoutRequired();
		}
	}

	public float getBottomPad() {
		return m_bottomPad;
	}
	public void setBottomPad( float bottomPad ) {
		if( m_bottomPad != bottomPad ) {
			m_bottomPad = bottomPad;
			fireLayoutRequired();
		}
	}
	public float getLeftPad() {
		return m_leftPad;
	}
	public void setLeftPad( float leftPad ) {
		if( m_leftPad != leftPad ) {
			m_leftPad = leftPad;
			fireLayoutRequired();
		}
	}
	public float getRightPad() {
		return m_rightPad;
	}
	public void setRightPad( float rightPad ) {
		if( m_rightPad != rightPad ) {
			m_rightPad = rightPad;
			fireLayoutRequired();
		}
	}
	public float getTopPad() {
		return m_topPad;
	}
	public void setTopPad( float topPad ) {
		if( m_topPad != topPad ) {
			m_topPad = topPad;
			fireLayoutRequired();
		}
	}

	public void computeLocations( Composite composite, int width, int height ) {
		//assert getComposites().contains( composite );
		
		//todo: assert components valid
		if( m_topLeftComponent != null ) {
			m_topLeftComponent.setLocation( m_leftPad, m_topPad );
		}
		if( m_topRightComponent != null ) {
			m_topRightComponent.setLocation( width - m_topRightComponent.getWidth() - m_rightPad, m_topPad );
		}
		if( m_bottomLeftComponent != null ) {
			m_bottomLeftComponent.setLocation( m_leftPad, height - m_bottomLeftComponent.getHeight() - m_bottomPad );
		}
		if( m_bottomRightComponent != null ) {
			m_bottomRightComponent.setLocation( width - m_bottomRightComponent.getWidth() - m_rightPad, height - m_bottomRightComponent.getHeight() - m_bottomPad );
		}
	}
}
