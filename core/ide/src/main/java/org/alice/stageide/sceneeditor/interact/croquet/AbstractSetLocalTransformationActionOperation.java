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
package org.alice.stageide.sceneeditor.interact.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSetLocalTransformationActionOperation extends AbstractFieldBasedManipulationActionOperation {

	public AbstractSetLocalTransformationActionOperation( org.lgna.croquet.Group group, java.util.UUID individualId, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, org.lgna.project.ast.UserField field, String editPresentationKey ) {
		super( group, individualId, isDoRequired, animator, field, editPresentationKey );
	}

	protected abstract edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGTransformable();

	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getPrevLocalTransformation();

	protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getNextLocalTransformation();

	private void setLocalTransformation( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable, edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt ) {
		if( this.getAnimator() != null ) {
			edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation povAnimation = new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( sgTransformable, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT, null, lt );
			povAnimation.setDuration( 0.5 );
			//this.animator.complete( null );
			this.getAnimator().invokeLater( povAnimation, null );
		} else {
			sgTransformable.setLocalTransformation( lt );
		}
	}

	private static final java.text.NumberFormat MILLI_FORMAT = new java.text.DecimalFormat( "0.000" );

	private static void appendPosition( StringBuilder sb, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		sb.append( "(" );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( m.translation.x, MILLI_FORMAT ) );
		sb.append( "," );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( m.translation.y, MILLI_FORMAT ) );
		sb.append( "," );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( m.translation.z, MILLI_FORMAT ) );
		sb.append( ")" );
	}

	private static void appendOrientation( StringBuilder sb, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion q = m.orientation.createUnitQuaternion();
		sb.append( "(" );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( q.x, MILLI_FORMAT ) );
		sb.append( "," );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( q.y, MILLI_FORMAT ) );
		sb.append( "," );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( q.z, MILLI_FORMAT ) );
		sb.append( "," );
		sb.append( edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( q.w, MILLI_FORMAT ) );
		sb.append( ")" );
	}

	@Override
	protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
		//		final edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = ;
		final edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevLT = this.getPrevLocalTransformation();
		final edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextLT = this.getNextLocalTransformation();

		assert prevLT != null;
		assert nextLT != null;
		assert prevLT.isNaN() == false;
		assert nextLT.isNaN() == false;
		step.commitAndInvokeDo( new org.lgna.croquet.edits.AbstractEdit( step ) {
			@Override
			protected final void doOrRedoInternal( boolean isDo ) {
				if( isDo && ( isDoRequired() == false ) ) {
					//pass
				} else {
					setLocalTransformation( AbstractSetLocalTransformationActionOperation.this.getSGTransformable(), nextLT );
				}
			}

			@Override
			protected final void undoInternal() {
				setLocalTransformation( AbstractSetLocalTransformationActionOperation.this.getSGTransformable(), prevLT );
			}

			@Override
			protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
				String name = getEditPresentationKey();
				rv.append( name );
				if( descriptionStyle.isDetailed() ) {
					org.lgna.story.SThing thing = org.lgna.story.implementation.EntityImp.getAbstractionFromSgElement( AbstractSetLocalTransformationActionOperation.this.getSGTransformable() );
					rv.append( " " );
					rv.append( thing );
					if( name.contains( "Move" ) ) {
						rv.append( " " );
						appendPosition( rv, prevLT );
						rv.append( " -> " );
						appendPosition( rv, nextLT );
					}
					if( name.contains( "Rotate" ) ) {
						rv.append( " " );
						appendOrientation( rv, prevLT );
						rv.append( " -> " );
						appendOrientation( rv, nextLT );
					}
				}
			}
		} );
	}
}
