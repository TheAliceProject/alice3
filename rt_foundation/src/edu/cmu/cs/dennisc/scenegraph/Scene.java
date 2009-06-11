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
 * the root of the scenegraph
 * @author Dennis Cosgrove
 */
public class Scene extends Composite {
	public final edu.cmu.cs.dennisc.property.InstanceProperty< Background > background = new edu.cmu.cs.dennisc.property.InstanceProperty< Background >( this, null );
	public final edu.cmu.cs.dennisc.property.FloatProperty globalBrightness = new edu.cmu.cs.dennisc.property.FloatProperty( this, 1.0f );
	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		rv.setIdentity();
		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		rv.setIdentity();
		return rv;
	}

	@Override
	public Composite getRoot() {
		return this;
	}

	//todo: check to see if other is a descendant
	public boolean isSceneOf( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame other ) {
		return true;
	}
}
