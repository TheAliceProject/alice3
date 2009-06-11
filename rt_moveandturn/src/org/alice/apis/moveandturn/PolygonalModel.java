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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.ClassTemplate;
import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */

@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired=true)
public class PolygonalModel extends Model {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< int[] > POLYGONS_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< int[] >( PolygonalModel.class, "Polygons" );

	private Polygons m_polygons;
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		if( m_polygons != null ) {
			return m_polygons.getSGITA();
		} else {
			return null;
		}
	}
	
	@PropertyGetterTemplate(visibility = Visibility.TUCKED_AWAY)
	public Polygons getPolygons() {
		return m_polygons;
	}
	public void setPolygons( Polygons polygons ) {
		m_polygons = polygons;
	}
}
