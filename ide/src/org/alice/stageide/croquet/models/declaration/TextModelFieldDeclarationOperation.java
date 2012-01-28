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

package org.alice.stageide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class TextModelFieldDeclarationOperation extends org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation {
	private static class SingletonHolder {
		private static TextModelFieldDeclarationOperation instance = new TextModelFieldDeclarationOperation();
	}
	public static TextModelFieldDeclarationOperation getInstance() {
		return SingletonHolder.instance;
	}
	private String valueLabelText;
	private TextModelFieldDeclarationOperation() {
		super( 
				java.util.UUID.fromString( "d22b663b-966a-4a8e-a2ef-ca43523b4c1e" ), 
				org.lgna.project.ast.JavaType.getInstance( org.lgna.story.TextModel.class ), false, 
				false, false, 
				"", true, 
				org.lgna.project.ast.AstUtilities.createInstanceCreation( org.lgna.story.TextModel.class ), false 
		);
	}
	@Override
	protected void localize() {
		super.localize();
		this.valueLabelText = this.findLocalizedText( "valueLabel", TextModelFieldDeclarationOperation.class );
	}

	public String getValueLabelText() {
		return this.valueLabelText;
	}
	
	public org.alice.ide.croquet.models.ast.PropertyState getValueState() {
		return this.getStateForGetter( org.lgna.story.TextModel.class, "getValue" );
	}
	@Override
	protected org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization customize( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization rv ) {
		super.customize( step, declaringType, field, rv );
		org.alice.ide.croquet.models.ast.PropertyState valueState = this.getValueState();
		rv.addDoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterStatement( 
				false, field, 
				valueState.getSetter(), 
				valueState.getValue()
		) );
		return rv;
	}
	
	@Override
	protected org.alice.stageide.croquet.components.declaration.TextModelFieldDeclarationPanel createMainComponent( org.lgna.croquet.history.InputDialogOperationStep step ) {
		this.getValueState().setValue( new org.lgna.project.ast.StringLiteral( "" ) );
		return new org.alice.stageide.croquet.components.declaration.TextModelFieldDeclarationPanel( this );
	}
}
