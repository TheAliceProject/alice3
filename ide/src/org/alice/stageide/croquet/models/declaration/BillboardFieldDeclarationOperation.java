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
public class BillboardFieldDeclarationOperation extends org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation {
	private static class SingletonHolder {
		private static BillboardFieldDeclarationOperation instance = new BillboardFieldDeclarationOperation();
	}
	public static BillboardFieldDeclarationOperation getInstance() {
		return SingletonHolder.instance;
	}
	private String frontPaintLabelText;
	private String backPaintLabelText;
	private BillboardFieldDeclarationOperation() {
		super( 
				java.util.UUID.fromString( "1ce5a991-d315-40d3-a0ad-d711835e8140" ), 
				org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Billboard.class ), false, 
				false, false, 
				"", true, 
				org.lgna.project.ast.AstUtilities.createInstanceCreation( org.lgna.story.Billboard.class ), false 
		);
	}
	@Override
	protected void localize() {
		super.localize();
		this.frontPaintLabelText = this.findLocalizedText( "paintLabel", BillboardFieldDeclarationOperation.class );
		this.backPaintLabelText = this.findLocalizedText( "backPaintLabel", BillboardFieldDeclarationOperation.class );
	}

	public String getFrontPaintLabelText() {
		return this.frontPaintLabelText;
	}
	public String getBackPaintLabelText() {
		return this.backPaintLabelText;
	}
	
	protected org.alice.ide.croquet.models.ast.PropertyState getStateForGetter( org.lgna.project.ast.JavaMethod getter ) {
		return org.alice.ide.croquet.models.ast.PropertyState.getInstanceForGetter( org.lgna.croquet.Application.INHERIT_GROUP, getter );
	}
	protected org.alice.ide.croquet.models.ast.PropertyState getStateForGetter( Class<?> cls, String name, Class<?>... parameterTypes ) {
		return getStateForGetter( org.lgna.project.ast.JavaMethod.getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( cls, name, parameterTypes ) ) );
	}

	public org.alice.ide.croquet.models.ast.PropertyState getFrontPaintState() {
		return this.getStateForGetter( org.lgna.story.Model.class, "getPaint" );
	}
	public org.alice.ide.croquet.models.ast.PropertyState getBackPaintState() {
		return this.getStateForGetter( org.lgna.story.Billboard.class, "getBackPaint" );
	}
	@Override
	protected org.alice.stageide.croquet.components.declaration.BillboardFieldDeclarationPanel createMainComponent( org.lgna.croquet.history.InputDialogOperationStep step ) {
		return new org.alice.stageide.croquet.components.declaration.BillboardFieldDeclarationPanel( this );
	}
	@Override
	protected org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization customize( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization rv ) {
		super.customize( step, declaringType, field, rv );
		org.alice.ide.croquet.models.ast.PropertyState frontState = this.getFrontPaintState();
		org.alice.ide.croquet.models.ast.PropertyState backState = this.getBackPaintState();
		rv.addDoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterStatement( 
				false, field, 
				frontState.getSetter(), 
				frontState.getValue()
		) );
		rv.addDoStatement(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetterStatement( 
				false, field, 
				backState.getSetter(), 
				backState.getValue()
		) );
		return rv;
	}
}
