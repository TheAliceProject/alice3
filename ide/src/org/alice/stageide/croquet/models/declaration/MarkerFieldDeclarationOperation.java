/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.croquet.models.declaration;

import org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException;
import org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation;
import org.lgna.project.ast.UserField;
import org.lgna.story.ImplementationAccessor;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;

/**
 * @author dculyba
 *
 */
public abstract class MarkerFieldDeclarationOperation extends ManagedFieldDeclarationOperation {
	
	private UserField selectedField = null;
	private AffineMatrix4x4 initialMarkerTransform = null;
	private String colorFieldLabel = "";
	
	private PropertyListener nameChangeListener = new PropertyListener() {
		public void propertyChanging(PropertyEvent e) {
		}
		public void propertyChanged(PropertyEvent e) {
			localize();
		}
	};

	public MarkerFieldDeclarationOperation(java.util.UUID id, Class<? extends org.lgna.story.Marker> markerCls) {
		super( 
				id, 
				org.lgna.project.ast.JavaType.getInstance( markerCls ), false, 
				false, false, 
				"", true, 
				org.lgna.project.ast.AstUtilities.createInstanceCreation( markerCls ), false 
		);
	}
	
	protected abstract org.lgna.story.Color getInitialMarkerColor();
	protected abstract String getInitialMarkerName(org.lgna.story.Color color);
	protected abstract AffineMatrix4x4 getInitialMarkerTransform();
	
	protected UserField getSelectedField() {
		return this.selectedField;
	}
	
	protected void initializeState() {
		org.lgna.story.Color initialMarkerColor = getInitialMarkerColor();
		String initialMarkerName = getInitialMarkerName(initialMarkerColor);
		this.initialMarkerTransform = getInitialMarkerTransform();
		try {
			org.lgna.project.ast.Expression colorExpresion = org.alice.stageide.StageIDE.getActiveInstance().getApiConfigurationManager().getExpressionCreator().createExpression(initialMarkerColor);
			this.getColorIdState().setValueTransactionlessly(colorExpresion);
		}
		catch (CannotCreateExpressionException ccee) {
			ccee.printStackTrace();
		}
		this.getNameState().setValue(initialMarkerName);
	}
	
	
	public void setSelectedField(UserField field) {
		if (this.selectedField != null) {
			this.selectedField.name.removePropertyListener(this.nameChangeListener);
		}
		this.selectedField = field;
		if (this.selectedField != null) {
			this.selectedField.name.addPropertyListener(this.nameChangeListener);
		}
		this.localize();
	}

	@Override
	protected void localize() {
		super.localize();
		this.colorFieldLabel = this.findLocalizedText( "colorFieldLabel", MarkerFieldDeclarationOperation.class );
	}
	
	
	protected abstract org.alice.ide.croquet.components.declaration.FieldDeclarationPanel<? extends org.alice.stageide.croquet.models.declaration.MarkerFieldDeclarationOperation > createMainComponent();
	
	@Override
	protected org.alice.ide.croquet.components.declaration.FieldDeclarationPanel<? extends org.alice.stageide.croquet.models.declaration.MarkerFieldDeclarationOperation > createMainComponent( org.lgna.croquet.history.InputDialogOperationStep step ) {
		this.initializeState();
		return createMainComponent();
	}

	public org.alice.ide.croquet.models.ast.PropertyState getColorIdState() {
		return this.getStateForGetter( org.lgna.story.Marker.class, "getColorId" );
	}
	
	public String getColorFieldLabel() {
		return this.colorFieldLabel;
	}
	
	@Override
	protected org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization customize( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization rv ) {
		super.customize( step, declaringType, field, rv );
		org.alice.ide.croquet.models.ast.PropertyState colorState = this.getColorIdState();
		rv.addDoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterStatement( 
				false, field, 
				colorState.getSetter(), 
				colorState.getValue()
		) );
		try {
			org.lgna.project.ast.Statement orientationStatement = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createOrientationStatement( 
					false, field, 
					ImplementationAccessor.createOrientation(this.initialMarkerTransform.orientation)
			);
			rv.addDoStatement(orientationStatement);
		}
		catch ( CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
		}
		try {
			org.lgna.project.ast.Statement positionStatement = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createPositionStatement( 
					false, field, 
					ImplementationAccessor.createPosition(this.initialMarkerTransform.translation)
			);
			rv.addDoStatement(positionStatement);
		}
		catch ( CannotCreateExpressionException ccee ) {
			ccee.printStackTrace();
		}
		return rv;
	}

}
