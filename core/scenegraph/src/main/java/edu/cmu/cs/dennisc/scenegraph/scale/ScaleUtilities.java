/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.scenegraph.scale;

import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public class ScaleUtilities {
	private static void applyScale( edu.cmu.cs.dennisc.scenegraph.Component sgRoot, edu.cmu.cs.dennisc.scenegraph.Component sgComponent, edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.pattern.Criterion<edu.cmu.cs.dennisc.scenegraph.Component> inclusionCriterion ) {
		if( ( inclusionCriterion == null ) || inclusionCriterion.accept( sgComponent ) /* && !(sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Joint) */) {
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
				for( int i = 0; i < N; i++ ) {
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

	public static void applyScale( edu.cmu.cs.dennisc.scenegraph.Component sgComponent, edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.pattern.Criterion<edu.cmu.cs.dennisc.scenegraph.Component> inclusionCriterion ) {
		applyScale( sgComponent, sgComponent, axis, inclusionCriterion );
	}

	public static void exorciseTheDemonsOfScaledSpace( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTransformable.localTransformation.getValue();
		if( m.orientation.isWithinReasonableEpsilonOfUnitLengthSquared() ) {
			//pass
		} else {
			double xScale = m.orientation.right.calculateMagnitude();
			double yScale = m.orientation.up.calculateMagnitude();
			double zScale = m.orientation.backward.calculateMagnitude();

			applyScale( sgTransformable, new edu.cmu.cs.dennisc.math.Vector3( xScale, yScale, zScale ), null );

			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 inverseScale = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createIdentity();
			inverseScale.right.x = 1 / xScale;
			inverseScale.up.y = 1 / yScale;
			inverseScale.backward.z = 1 / zScale;
			m.orientation.applyMultiplication( inverseScale );

			assert m.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		}
		for( edu.cmu.cs.dennisc.scenegraph.Component sgChild : sgTransformable.getComponents() ) {
			if( sgChild instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
				edu.cmu.cs.dennisc.scenegraph.Transformable sgChildTransformable = (edu.cmu.cs.dennisc.scenegraph.Transformable)sgChild;
				exorciseTheDemonsOfScaledSpace( sgChildTransformable );
			}
		}
	}

	public static Visual getSGVisualForTransformable( edu.cmu.cs.dennisc.scenegraph.Transformable object )
	{
		if( object == null )
		{
			return null;
		}
		for( int i = 0; i < object.getComponentCount(); i++ )
		{
			Component c = object.getComponentAt( i );
			if( c instanceof Visual )
			{
				return (Visual)c;
			}
		}
		return null;
	}

}
