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
public class OrientToUprightActionOperation extends TransformableFieldTileActionOperation {
	public OrientToUprightActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( field );
		this.putValue( javax.swing.Action.NAME, "Orient to Upright" );
	}
	@Override
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateNextAbsoluteTransformation( org.alice.apis.moveandturn.AbstractTransformable transformable ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getSGAbstractTransformable().getAbsoluteTransformation();
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromStandUp( m.orientation );
		return new edu.cmu.cs.dennisc.math.AffineMatrix4x4( axes, m.translation );
	}
}
