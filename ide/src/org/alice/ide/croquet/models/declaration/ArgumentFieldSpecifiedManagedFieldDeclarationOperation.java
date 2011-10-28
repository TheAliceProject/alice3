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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentFieldSpecifiedManagedFieldDeclarationOperation extends InitializerManagedFieldDeclarationOperation {
	private static edu.cmu.cs.dennisc.map.MapToMap< org.lgna.project.ast.AbstractField, org.lgna.croquet.DropSite, ArgumentFieldSpecifiedManagedFieldDeclarationOperation > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	public static ArgumentFieldSpecifiedManagedFieldDeclarationOperation getInstance( org.lgna.project.ast.AbstractField field, org.lgna.croquet.DropSite dropSite ) {
		ArgumentFieldSpecifiedManagedFieldDeclarationOperation rv = mapToMap.get( field, dropSite );
		if( rv != null ) {
			//pass
		} else {
			rv = new ArgumentFieldSpecifiedManagedFieldDeclarationOperation( field, dropSite );
			mapToMap.put( field, dropSite, rv );
		}
		return rv;
	}
	
	private static org.lgna.project.ast.InstanceCreation createInstanceCreation( org.lgna.project.ast.AbstractField argumentField ) {
		org.lgna.project.ast.AbstractType< ?, ?, ? > valueType = argumentField.getValueType();
		org.lgna.project.ast.AbstractConstructor bogusConstructor = org.alice.ide.croquet.models.gallerybrowser.RootGalleryNode.getInstance().getConstructorForArgumentType( valueType );
		org.lgna.project.ast.NamedUserType namedUserType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( bogusConstructor.getDeclaringType().getFirstTypeEncounteredDeclaredInJava(), (org.lgna.project.ast.JavaField)argumentField );
		org.lgna.project.ast.AbstractConstructor constructor = namedUserType.constructors.get( 0 );
		return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, org.lgna.project.ast.AstUtilities.createStaticFieldAccess( argumentField ) );
	}
	private final org.lgna.project.ast.AbstractField field;
	private final org.lgna.croquet.DropSite dropSite;
	private ArgumentFieldSpecifiedManagedFieldDeclarationOperation( org.lgna.project.ast.AbstractField field, org.lgna.croquet.DropSite dropSite ) {
		super( 
				java.util.UUID.fromString( "a207504f-0f28-4e18-91ec-b7c3f26078fe" ), 
				createInstanceCreation( field )
		);
		//this.constructor = constructor;
		this.field = field;
		this.dropSite = dropSite;
	}
	
	@Override
	protected org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization customize( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation.EditCustomization rv ) {
		org.alice.stageide.sceneeditor.draganddrop.SceneDropSite sceneDropSite = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( this.dropSite, org.alice.stageide.sceneeditor.draganddrop.SceneDropSite.class );
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 initialTransform = sceneDropSite != null ? sceneDropSite.getTransform() : null;
		org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor = org.alice.ide.IDE.getActiveInstance().getMainComponent().getSceneEditor();
		org.lgna.project.ast.Statement[] doStatements = sceneEditor.getDoStatementsForAddField(field, initialTransform);
		for (org.lgna.project.ast.Statement s : doStatements) {
			rv.addDoStatement(s);
		}	
		org.lgna.project.ast.Statement[] undoStatements = sceneEditor.getUndoStatementsForAddField(field);
		for (org.lgna.project.ast.Statement s : undoStatements) {
			rv.addUndoStatement(s);
		}
		return rv;
	}
	
	@Override
	protected void localize() {
		super.localize();
		this.setName( this.field.getName() );
	}
}
