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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class IndexedPolygonArray extends VertexGeometry {
	public final edu.cmu.cs.dennisc.property.InstanceProperty< int[] > polygonData = new edu.cmu.cs.dennisc.property.InstanceProperty< int[] >( this, new int[ 0 ] ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.PropertyOwner owner, int[] value ) {
			assert value != null;
			super.setValue( owner, value );
		}
	};
	
	public abstract int getIndicesPerPolygon();

//	public int getPolygonCount() {
//		return polygonData.getValue().length/getIndicesPerPolygon();
//	}
//	public int getPolygonDataCount() {
//		return polygonData.getValue().length;
//	}
//	public int[] accessPolygonData() {
//		return polygonData.getValue();
//	}
//	public int[] getPolygonData( int[] rv ) {
//	    System.arraycopy( m_polygonData, 0, rv, 0, rv.length );
//	    return rv;
//	}
//	public int[] getPolygonData() {
//		return getPolygonData( new int[ getPolygonDataCount() ] );
//	}
//	public void setPolygonData( int[] polygonData ) {
//		if( m_polygonData.length != polygonData.length ) {
//			m_polygonData = new int[ polygonData.length ];
//		}
//	    System.arraycopy( polygonData, 0, m_polygonData, 0, m_polygonData.length );
//		firePolygonDataChange();
//	}
//	public void firePolygonDataChange() {
//	    firePropertyChanged( POLYGON_DATA_PROPERTY_NAME );
//	}
}
