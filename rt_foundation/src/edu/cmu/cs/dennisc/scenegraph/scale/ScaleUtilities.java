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
package edu.cmu.cs.dennisc.scenegraph.scale;

/**
 * @author Dennis Cosgrove
 */
public class ScaleUtilities {
	private static void applyScale( edu.cmu.cs.dennisc.scenegraph.Component sgRoot, edu.cmu.cs.dennisc.scenegraph.Component sgComponent, edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.pattern.Criterion< edu.cmu.cs.dennisc.scenegraph.Component > inclusionCriterion ) {
		if( inclusionCriterion == null || inclusionCriterion.accept( sgComponent ) ) {
			if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
				edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = (edu.cmu.cs.dennisc.scenegraph.Composite)sgComponent;
				if( sgComposite instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
					edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = (edu.cmu.cs.dennisc.scenegraph.Transformable)sgComposite;
					if( sgRoot == sgTransformable ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTransformable.localTransformation.getValue();
						m.translation.multiply( axis );
						sgTransformable.localTransformation.setValue( m );
					}
				}
				final int N = sgComposite.getComponentCount();
				for( int i=0; i<N; i++ ) {
					applyScale( sgRoot, sgComposite.getComponentAt( i ), axis, inclusionCriterion );
				}
			} else if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Visual ) {
				edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = (edu.cmu.cs.dennisc.scenegraph.Visual)sgComponent;
				edu.cmu.cs.dennisc.math.Matrix3x3 scale = sgVisual.scale.getValue();
				edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( scale, axis );
				sgVisual.scale.setValue( scale );
			}
		}
	}
	public static void applyScale( edu.cmu.cs.dennisc.scenegraph.Component sgComponent, edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.pattern.Criterion< edu.cmu.cs.dennisc.scenegraph.Component > inclusionCriterion ) {
		applyScale( sgComponent, sgComponent, axis, inclusionCriterion );
	}
}
