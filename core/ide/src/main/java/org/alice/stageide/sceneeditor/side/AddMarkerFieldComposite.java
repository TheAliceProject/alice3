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
package org.alice.stageide.sceneeditor.side;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.lgna.story.EmployeesOnly;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMarkerFieldComposite extends org.alice.ide.ast.declaration.AddPredeterminedValueTypeManagedFieldComposite {
	public AddMarkerFieldComposite( java.util.UUID migrationId, Class<? extends org.lgna.story.SMarker> cls ) {
		super( migrationId, cls );
	}

	private final org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> colorIdState = this.createInitialPropertyValueExpressionState( "colorIdState", org.lgna.story.Color.RED, org.lgna.story.SMarker.class, "setColorId", org.lgna.story.Color.class, null );

	public org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> getColorIdState() {
		return this.colorIdState;
	}

	protected abstract org.lgna.story.Color getInitialMarkerColor();

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		org.lgna.story.Color initialMarkerColor = this.getInitialMarkerColor();
		try {
			org.lgna.project.ast.Expression colorExpresion = org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator().createExpression( initialMarkerColor );
			this.colorIdState.setValueTransactionlessly( colorExpresion );
		} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
		}
		super.handlePreShowDialog( step );
	}

	protected abstract AffineMatrix4x4 getInitialMarkerTransform();

	@Override
	protected boolean isUserTypeDesired() {
		return false;
	}

	@Override
	protected boolean isNameGenerationDesired() {
		//todo?
		return true;
	}

	@Override
	protected boolean isNumberAppendedToNameOfFirstField() {
		return true;
	}

	private static org.lgna.project.ast.JavaMethod COLOR_ID_SETTER = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SMarker.class, "setColorId", org.lgna.story.Color.class );

	@Override
	protected org.alice.ide.ast.declaration.AddManagedFieldComposite.EditCustomization customize( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field, org.alice.ide.ast.declaration.AddManagedFieldComposite.EditCustomization rv ) {
		super.customize( step, declaringType, field, rv );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 initialMarkerTransform = this.getInitialMarkerTransform();
		rv.addDoStatement( org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterStatement(
				false, field,
				COLOR_ID_SETTER,
				this.colorIdState.getValue()
				) );
		try {
			org.lgna.project.ast.Statement orientationStatement = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createOrientationStatement(
					false, field,
					EmployeesOnly.createOrientation( initialMarkerTransform.orientation )
					);
			rv.addDoStatement( orientationStatement );
		} catch( CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
		}
		try {
			org.lgna.project.ast.Statement positionStatement = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createPositionStatement(
					false, field,
					EmployeesOnly.createPosition( initialMarkerTransform.translation )
					);
			rv.addDoStatement( positionStatement );
		} catch( CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
		}
		return rv;
	}

}
