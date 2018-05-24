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

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.TraditionalStyle;
import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;
import org.lgna.croquet.Group;

import edu.cmu.cs.dennisc.scenegraph.scale.Scalable;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.UserField;
import org.lgna.story.implementation.ModelImp;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class PredeterminedScaleActionOperation extends AbstractFieldBasedManipulationActionOperation {
	private Resizer resizer;
	private double redoScale;
	private double undoScale;
	private Criterion<Component> criterion;

	public PredeterminedScaleActionOperation( Group group, boolean isDoRequired, Animator animator, UserField scalableField, Resizer resizer, double previousScale, double currentScale, Criterion<Component> criterion, String editPresentationKey ) {
		super( group, UUID.fromString( "455cae50-c329-44e3-ba7c-9ef10f69d965" ), isDoRequired, animator, scalableField, editPresentationKey );
		this.resizer = resizer;

		this.redoScale = currentScale;
		this.undoScale = previousScale;

		assert redoScale != 0.0;
		assert undoScale != 0.0;

		this.criterion = criterion;
	}

	private Scalable getScalable() {
		ModelImp modelImp = (ModelImp)getEntityImp();
		return modelImp;
	}

	private Resizer getResizer() {
		return this.resizer;
	}

	private void scale( final double startScale, final double endScale ) {
		if( this.getAnimator() != null ) {
			class ScaleAnimation extends DoubleAnimation {
				public ScaleAnimation() {
					super( 0.5, TraditionalStyle.BEGIN_AND_END_GENTLY, startScale, endScale );
				}

				@Override
				protected void updateValue( Double v ) {
					Scalable scalable = getScalable();
					if( scalable != null ) {
						scalable.setValueForResizer( getResizer(), v );
					}
				}
			}
			this.getAnimator().invokeLater( new ScaleAnimation(), null );
		} else {
			Scalable scalable = getScalable();
			if( scalable != null ) {
				scalable.setValueForResizer( getResizer(), endScale );
			}
		}

	}

	@Override
	protected void perform( CompletionStep<?> step ) {
		step.commitAndInvokeDo( new AbstractEdit( step ) {
			@Override
			protected final void doOrRedoInternal( boolean isDo ) {
				if( isDo && ( isDoRequired() == false ) ) {
					//pass
				} else {
					scale( undoScale, redoScale );
				}
			}

			@Override
			protected final void undoInternal() {
				scale( redoScale, undoScale );
			}

			@Override
			protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
				rv.append( getEditPresentationKey() );
			}
		} );
	}
}
