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
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class PlaceOnTopOfGroundActionOperation extends TransformableFieldTileActionOperation {
	public PlaceOnTopOfGroundActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.putValue( javax.swing.Action.NAME, "Place on top of Ground" );
	}
	@Override
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateNextAbsoluteTransformation( org.alice.apis.moveandturn.AbstractTransformable transformable ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv = new edu.cmu.cs.dennisc.math.AffineMatrix4x4();
		org.alice.apis.moveandturn.Model model = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( transformable, org.alice.apis.moveandturn.Model.class ); 
		if( model != null ) {
			org.alice.apis.moveandturn.Scene scene = model.getScene();
			assert scene != null;
			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = model.getAxisAlignedMinimumBoundingBox();
			edu.cmu.cs.dennisc.math.Point3 position = model.getPosition( scene );
			position.y = -bb.getYMinimum();
		} else {
			rv.setNaN();
		}
		return rv;
	}
	
}
