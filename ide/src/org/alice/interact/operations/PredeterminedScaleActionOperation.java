/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.interact.operations;

import org.lgna.croquet.Group;

/**
 * @author Dennis Cosgrove
 */
public class PredeterminedScaleActionOperation extends org.lgna.croquet.ActionOperation {
	private boolean isDoRequired;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	private edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable;
	private edu.cmu.cs.dennisc.math.Vector3 redoVector;
	private edu.cmu.cs.dennisc.math.Vector3 undoVector;
	private edu.cmu.cs.dennisc.pattern.Criterion< edu.cmu.cs.dennisc.scenegraph.Component > criterion;
	
	private String editPresentationKey;
	public PredeterminedScaleActionOperation( Group group, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable, edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.pattern.Criterion< edu.cmu.cs.dennisc.scenegraph.Component > criterion, String editPresentationKey ) {
		super( group, java.util.UUID.fromString( "455cae50-c329-44e3-ba7c-9ef10f69d965" ) );
		this.isDoRequired = isDoRequired;
		this.animator = animator;
		this.sgTransformable = sgTransformable;
		
		assert axis.isNaN() == false;
		assert axis.x != 0.0;
		assert axis.y != 0.0;
		assert axis.z != 0.0;

		this.redoVector = axis;
		this.undoVector = new edu.cmu.cs.dennisc.math.Vector3( 1.0/axis.x, 1.0/axis.y, 1.0/axis.z );
		assert this.undoVector.isNaN() == false;
		
		this.criterion = criterion;
		this.editPresentationKey = editPresentationKey;
	}
	private void scale( final edu.cmu.cs.dennisc.math.Vector3 axis ) {
		if( this.animator != null ) {
			class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
				private edu.cmu.cs.dennisc.math.Vector3 m_vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
				private edu.cmu.cs.dennisc.math.Vector3 m_vBuffer = new edu.cmu.cs.dennisc.math.Vector3();
				public ScaleAnimation() {
					super( 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), axis );
				}
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
					edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( m_vBuffer, v, m_vPrev );
					edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.applyScale( PredeterminedScaleActionOperation.this.sgTransformable, m_vBuffer, PredeterminedScaleActionOperation.this.criterion );
					m_vPrev.set( v );
				}
			}
			this.animator.invokeLater( new ScaleAnimation(), null );
		} else {
			edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.applyScale( this.sgTransformable, axis, this.criterion );
		}
		
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		step.commitAndInvokeDo( new org.alice.ide.ToDoEdit() {
			@Override
			protected final void doOrRedoInternal( boolean isDo ) {
				if( isDo && ( isDoRequired == false ) ) {
					//pass
				} else {
					scale( redoVector );
				}
			}
			@Override
			protected final void undoInternal() {
				scale( undoVector );
			}
			@Override
			protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
				rv.append( editPresentationKey );
				return rv;
			}
		} );
	}
}
