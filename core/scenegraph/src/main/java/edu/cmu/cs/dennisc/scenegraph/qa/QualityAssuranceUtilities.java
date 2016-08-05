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
package edu.cmu.cs.dennisc.scenegraph.qa;

/**
 * @author Dennis Cosgrove
 */
public class QualityAssuranceUtilities {
	private QualityAssuranceUtilities() {
		throw new RuntimeException();
	}

	private static boolean isOrientationMendingRequired( edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt ) {
		return lt.orientation.isNaN() || lt.orientation.right.isWithinReasonableEpsilonOfZero() || lt.orientation.up.isWithinReasonableEpsilonOfZero() || lt.orientation.backward.isWithinReasonableEpsilonOfZero();
	}

	private static boolean isTranslationMendingRequired( edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt ) {
		return lt.translation.isNaN();
	}

	private static void appendProblems( java.util.List<Problem> problems, edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		if( sgComponent != null ) {
			if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Composite ) {
				edu.cmu.cs.dennisc.scenegraph.Composite sgComposite = (edu.cmu.cs.dennisc.scenegraph.Composite)sgComponent;
				if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.AbstractTransformable ) {
					edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = (edu.cmu.cs.dennisc.scenegraph.AbstractTransformable)sgComponent;
					edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt = sgTransformable.getLocalTransformation();
					boolean isOrientationMendingRequired = isOrientationMendingRequired( lt );
					boolean isTranslationMendingRequired = isTranslationMendingRequired( lt );
					if( isOrientationMendingRequired || isTranslationMendingRequired ) {
						problems.add( new BadLocalTransformation( sgTransformable, isOrientationMendingRequired, isTranslationMendingRequired ) );
					}
				}
				for( edu.cmu.cs.dennisc.scenegraph.Component sgChild : sgComposite.getComponents() ) {
					appendProblems( problems, sgChild );
				}
			} else if( sgComponent instanceof edu.cmu.cs.dennisc.scenegraph.Visual ) {
				edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = (edu.cmu.cs.dennisc.scenegraph.Visual)sgComponent;
				edu.cmu.cs.dennisc.math.Matrix3x3 scale = sgVisual.scale.getValue();
				if( scale.isNaN() ) { //todo: check isZero()?
					problems.add( new BadScale( sgVisual ) );
				}
				if( sgVisual instanceof edu.cmu.cs.dennisc.scenegraph.SkeletonVisual ) {
					edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual = (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)sgVisual;
					appendProblems( problems, sgSkeletonVisual.skeleton.getValue() );
				}
			}
		}
	}

	public static java.util.List<Problem> inspect( edu.cmu.cs.dennisc.scenegraph.Component sgComponent ) {
		java.util.List<Problem> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		appendProblems( rv, sgComponent );
		return rv;
	}

	public static void inspectAndMendIfNecessary( edu.cmu.cs.dennisc.scenegraph.Component sgComponent, Mender mender ) {
		java.util.List<Problem> problems = inspect( sgComponent );
		for( Problem problem : problems ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( problem );
			problem.mend( mender );
		}
	}
}
