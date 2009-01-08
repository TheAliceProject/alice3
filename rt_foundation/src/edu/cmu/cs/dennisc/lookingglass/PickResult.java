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

package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public class PickResult {
	private edu.cmu.cs.dennisc.scenegraph.Component m_sgSource;
	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual;
	private boolean m_isFrontFacing;
	private edu.cmu.cs.dennisc.scenegraph.Geometry m_sgGeometry;
	private int m_subElement;
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInSource = new edu.cmu.cs.dennisc.math.Point3();
	private edu.cmu.cs.dennisc.math.Point3 m_xyzInVisual = new edu.cmu.cs.dennisc.math.Point3();

	public PickResult() {
		setNaN();
	}
	public PickResult( edu.cmu.cs.dennisc.scenegraph.Component sgSource ) {
		set( sgSource );
	}
	public PickResult( edu.cmu.cs.dennisc.scenegraph.Component sgSource, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, boolean isFrontFacing, edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry, int subElement, edu.cmu.cs.dennisc.math.Point3 xyzInSource ) {
		set( sgSource, sgVisual, isFrontFacing, sgGeometry, subElement, xyzInSource );
	}

	public void set( edu.cmu.cs.dennisc.scenegraph.Component sgSource ) {
		setNaN();
		m_sgSource = sgSource;
	}
	public void set( edu.cmu.cs.dennisc.scenegraph.Component sgSource, edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, boolean isFrontFacing, edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry, int subElement, edu.cmu.cs.dennisc.math.Point3 xyzInSource ) {
		m_sgSource = sgSource;
		m_sgVisual = sgVisual;
		m_isFrontFacing = isFrontFacing;
		m_sgGeometry = sgGeometry;
		m_subElement = subElement;
		if( xyzInSource != null ) {
			m_xyzInSource.set( xyzInSource );
		} else {
			m_xyzInSource.setNaN();
		}
		m_xyzInVisual.setNaN();
	}
	public void setNaN() {
		set( null, null, false, null, -1, null );
	}

	public edu.cmu.cs.dennisc.scenegraph.Component getSource() {
		return m_sgSource;
	}
	public edu.cmu.cs.dennisc.scenegraph.Visual getVisual() {
		return m_sgVisual;
	}
	public edu.cmu.cs.dennisc.scenegraph.Geometry getGeometry() {
		return m_sgGeometry;
	}
	public boolean isFrontFacing() {
		return m_isFrontFacing;
	}
	public int getSubElement() {
		return m_subElement;
	}
	public edu.cmu.cs.dennisc.math.Point3 accessPositionInSource() {
		return m_xyzInSource;
	}
	public edu.cmu.cs.dennisc.math.Point3 getPositionInSource( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( accessPositionInSource() );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point3 getPositionInSource() {
		return getPositionInSource( new edu.cmu.cs.dennisc.math.Point3() );
	}
	
	public edu.cmu.cs.dennisc.math.Point3 accessPositionInVisual() {
		if( m_xyzInSource.isNaN() ) {
			if( m_xyzInVisual.isNaN() ) {
				//pass
			} else {
				assert m_sgVisual != null;
				m_sgVisual.transformFrom_AffectReturnValuePassedIn( m_xyzInVisual, m_sgSource );
			}
		}
		return m_xyzInVisual;
	}
	public edu.cmu.cs.dennisc.math.Point3 getPositionInVisual( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( accessPositionInVisual() );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Point3 getPositionInVisual() {
		return getPositionInVisual( new edu.cmu.cs.dennisc.math.Point3() );
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		//todo:
		sb.append( "[visual=" );
		sb.append( m_sgVisual );
		//		sb.append( ",geometry=" );
		//		sb.append( m_sgGeometry );
		sb.append( "]" );
		return sb.toString();
	}
}
